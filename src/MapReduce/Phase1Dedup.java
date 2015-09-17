package MapReduce;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import features.FeaturizeToken;
import general.CSVParser;
import general.Parameters;


//The file used for experiments. Only writes out composite and TF scores.
//Baseline file is separate

public class Phase1Dedup {

	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
	    
	    private Text k = new Text();
	    private Text v=new Text();
	    
	  //writes out featurized tokens as keys
	    //checks for forbidden words so can be used with property tables
	    //first column must be ID column. Does not consider that for tokenizing
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	        String line = value.toString().toLowerCase();
	        String[] tokens=new CSVParser().parseLine(line);
	        v.set(line);
	        for(int i=1; i<tokens.length; i++){
	        	String[] splits=tokens[i].split(Parameters.splitstring);
	        	for(String split:splits){
	        		split=split.trim();
	        		for(String forb:Parameters.forbiddenwords)
	        			if(split.equals(forb)||split.length()==0)
	        				continue;
	        	HashSet<String> features=FeaturizeToken.featurize(split);
	        	for(String feat:features){
	        		k.set(feat);
	        		
	        		context.write(k, v);
	        	}
	        	}
	        }
	    }
	 } 
	        
	 public static class Reduce extends Reducer<Text, Text, Text, Text> {

		 private Text k = new Text();
		 private Text v=new Text();
	    
		 public void reduce(Text key, Iterable<Text> values, Context context) 
	      throws IOException, InterruptedException {
	    	HashSet<String> AA=new HashSet<String>();
	    	
	       
	        for (Text val : values) 
	        	
	        		AA.add(val.toString());
	        	
	        ArrayList<String> A=new ArrayList<String>(AA);
	        if(A.size()==0||A.size()*(A.size()-1)/2>Parameters.maxBucketPairs)
	        	;
	        else
	        	for(int i=0; i<A.size()-1; i++)
	        		for(int j=i+1; j<A.size(); j++){
	        				double tf=FeaturizeToken.TF(A.get(i),A.get(j));
	        				double jaccard=FeaturizeToken.Jaccard(A.get(i),A.get(j));
	        				double dice=FeaturizeToken.Dice(A.get(i),A.get(j));
	        				
	        				String l1=new CSVParser().parseLine(A.get(i))[0];
	        				String l2=new CSVParser().parseLine(A.get(j))[0];
	        				
	        				k.set(l1+" "+l2);
	        				//v.set(Double.toString(tf)+" TF");
	        				//context.write(k,v);
	        				
	        				
	        				
	        				v.set(Double.toString(FeaturizeToken.computeComposite(dice,jaccard,tf))+" C");
	        				context.write(k,v);
	        		}
	        
	    }
	 }
	        
	 public static void main(String[] args) throws Exception {
	    Configuration conf = new Configuration();
	        
	        Job job = new Job(conf, "phase1dedup");
	        job.setJarByClass(Phase1Dedup.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	        
	    job.setMapperClass(Map.class);
	    job.setReducerClass(Reduce.class);
	        
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	        
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	        
	    job.waitForCompletion(true);
	 }
	        
}
