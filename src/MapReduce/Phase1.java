package MapReduce;
import java.io.IOException;
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


//This file needs to be updated!! Consult the deduplication version

public class Phase1 {

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
	        		for(String forb:Parameters.forbiddenwords)
	        			if(split.equals(forb))
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
	    	HashSet<String> A=new HashSet<String>();
	    	HashSet<String> B=new HashSet<String>();
	       
	        for (Text val : values) {
	        	if(new CSVParser().parseLine(val.toString())[0].contains("A"))
	        		A.add(val.toString());
	        	else
	        		B.add(val.toString());
	        }
	        if(A.size()==0||B.size()==0||A.size()*B.size()>Parameters.maxBucketPairs)
	        	;
	        else
	        	for(String a:A)
	        		for(String b:B){
	        				double tf=FeaturizeToken.TF(a,b);
	        				String l1=new CSVParser().parseLine(a)[0];
	        				String l2=new CSVParser().parseLine(b)[0];
	        				l1=l1.substring(1,l1.length());
	        				l2=l2.substring(1,l2.length());
	        				k.set(l1+" "+l2);
	        				v.set(Double.toString(tf));
	        				context.write(k,v);
	        		}
	        
	    }
	 }
	        
	 public static void main(String[] args) throws Exception {
	    Configuration conf = new Configuration();
	        
	        Job job = new Job(conf, "phase1");
	        job.setJarByClass(Phase1.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	        
	    job.setMapperClass(Map.class);
	    job.setReducerClass(Reduce.class);
	        
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	        
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	        
	    job.waitForCompletion(true);
	 }
	        
}
