package MapReduce;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
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

import features.GenerateFeaturesFile.Homo;
import general.Parameters;


//The file used for experiments. Only writes out composite and TF scores.
//Baseline file is separate

public class Phase2Dedup {
//first id column must not be included
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
	    
	    private Text k = new Text();
	    private Text v=new Text();
	    
	    static String BK="3 10";
	    
	  //writes out featurized tokens as keys
	    //checks for forbidden words so can be used with property tables
	    //first column must be ID column. Does not consider that for tokenizing
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	    	
	    	
	    	String[] bks=BK.split("\t");
	    	ArrayList<String> BKs=new ArrayList<String>();
	    	for(String b:bks)
	    		BKs.add(b);
	    	String line = value.toString().toLowerCase();
	    	
	    	GenerateBlocks block_obj=new GenerateBlocks(BKs, null);
	    	block_obj.setLine(line,true);
	    	
	    	for(int j=0; j<block_obj.num_clauses; j++){
				
				HashSet<String> keys=block_obj.block(j);
				if(keys==null)
					continue;
				for(String k1:keys)
					if(k1==null)
						continue;
					else{
						k.set(k1);
						v.set(line);
					}
				
	    	context.write(k,v);
	    }
	    }
	 } 
	        
	 public static class Reduce extends Reducer<Text, Text, Text, Text> {

		 private Text k = new Text();
		 private Text v=new Text();
	    
		 public void reduce(Text key, Iterable<Text> values, Context context) 
	      throws IOException, InterruptedException {
	    	
			Path[] cacheFiles =DistributedCache.getLocalCacheFiles(context.getConfiguration());
		    svm_model m=svm.svm_load_model(cacheFiles[0].toString());
	    HashSet<String> AA=new HashSet<String>();
    	k.set(key);
	       
        for (Text val : values){ 
        	AA.add(val.toString());
        	
        }
        		
        	
        ArrayList<String> A=new ArrayList<String>(AA);
        if(A.size()<=1||A.size()*(A.size()-1)/2>Parameters.maxpairs)
        	;
        else
        	for(int i=0; i<A.size()-1; i++)
        		for(int j=i+1; j<A.size(); j++){
        			ArrayList<Double> feat=Homo.getSVMFeatures(A.get(i), A.get(j));
        			svm_node[] n=new svm_node[feat.size()];
        			for(int l=0; l<feat.size(); l++){
        				n[l]=new svm_node();
        				n[l].index=l+1;
        				n[l].value=feat.get(l);
        			}
        			double[] prob=new double[2];
        			svm.svm_predict_probability(m,n,prob);
        			String s1=new Double(prob[0]).toString();
        			String s2=new Double(prob[1]).toString();
        			v.set(s1+"\t"+s2);
        			k.set(A.get(i)+"\n"+A.get(j));
        			context.write(k,v);
        		}
	        
	    }
	 }
	        
	 public static void main(String[] args) throws Exception {
	    Configuration conf = new Configuration();
	        
	        Job job = new Job(conf, "phase2dedup");
	        
	        DistributedCache.addCacheFile(new URI(args[0]),job.getConfiguration());
	        job.setJarByClass(Phase2Dedup.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	        
	    job.setMapperClass(Map.class);
	    job.setReducerClass(Reduce.class);
	        
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	        
	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	        
	    job.waitForCompletion(true);
	 }
	        
}
