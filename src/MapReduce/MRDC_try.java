package MapReduce;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;

import libsvm.svm;
import libsvm.svm_model;

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

import features.FeaturizeToken;
import general.CSVParser;
import general.Parameters;


//The file used for experiments. Only writes out composite and TF scores.
//Baseline file is separate

public class MRDC_try {

	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
	    
	    private Text k = new Text();
	    private Text v=new Text();
	    
	  //writes out featurized tokens as keys
	    //checks for forbidden words so can be used with property tables
	    //first column must be ID column. Does not consider that for tokenizing
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	    	Path[] cacheFiles =DistributedCache.getLocalCacheFiles(context.getConfiguration());
	    	svm_model m=svm.svm_load_model(cacheFiles[0].toString());
	    	
	    	k.set(cacheFiles[0].toString());
	    	v.set("0");
	    	context.write(v,k);
	    }
	 } 
	        
	 public static class Reduce extends Reducer<Text, Text, Text, Text> {

		 private Text k = new Text();
		 private Text v=new Text();
	    
		 public void reduce(Text key, Iterable<Text> values, Context context) 
	      throws IOException, InterruptedException {
	    	
	    	
	       k.set("0");
	        for (Text val : values){ 
	        	v.set(val);
	        	context.write(k,v);

	        }
	        
	    }
	 }
	        
	 public static void main(String[] args) throws Exception {
	    Configuration conf = new Configuration();
	        
	        Job job = new Job(conf, "mrdc_try");
	        
	        DistributedCache.addCacheFile(new URI(args[0]),job.getConfiguration());
	        job.setJarByClass(MRDC_try.class);
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
