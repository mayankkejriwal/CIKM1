package features;


import general.CSVParser;
import general.Parameters;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import svm.svm_train;



public class GenerateFeaturesFile {

	public static void main(String[] args)throws IOException{
		
		long time1=System.currentTimeMillis();
		Journal_Experiments.trainSVMIter();
		long time2=System.currentTimeMillis();
		System.out.println("Time elapsed (secs): "+((time2-time1)*1.0/1000));
		
	}
	
	public static class Journal_Experiments{
		
		
		static String prefix="/host/heteroDatasets/journal/used_finally/Restaurants/";
		static String file1=prefix+"file1.csv";
		static String file2=prefix+"file2.csv";
		static String BK=prefix+"BK";
		static String scoreFile=prefix+"sortedScores/J";
		static String schemaFile=prefix+"schema_ours";
		static String svmFile=prefix+"svmlight";
		static String svmModel=prefix+"svmModel";
		static int num=500;
		
		//for supervised
		static int percent=10;
		static String dupfile=prefix+"svmSup"+percent;
		static String nondupfile=prefix+"svmSupNonDup"+percent;
		static String svmModelSup=prefix+"svmModelSup"+percent;
		static String svmFileSup=prefix+"svmlightSup"+percent;
		
		//for iterations
		
		static String iterFile=prefix+"svmIter50";
		static String iterNonDupFile=prefix+"svmNonDupIter50";
		static String iterModelSup=prefix+"svmModelIter";
		static String iterFileSup=prefix+"svmlightIter";
		
		//consider simple exhaustive SBPs.
		public static void writeBKBrute()throws IOException{
			
			int dup=num;
			int nondup=num;
			String t=GenerateFeaturesFile.Hetero.generateBKStringBrute(file1,
					file2,scoreFile,dup,nondup);
			System.out.println(t);
			PrintWriter out=new PrintWriter(new File(BK));
			out.println(t);
			out.close();
		}
		
		public static void writeBKUnsup()throws IOException{
			
			int dup=num;
			int nondup=num;
			String t=GenerateFeaturesFile.Hetero.generateBKStringPermute(file1,
					file2,scoreFile,schemaFile,dup,nondup);
			System.out.println(t);
			PrintWriter out=new PrintWriter(new File(BK));
			out.println(t);
			out.close();
		}
		
		public static void writeBKSetCover()throws IOException{
			
			int dup=num;
			int nondup=num;
			
			String t=GenerateFeaturesFile.Hetero.generateBKStringSetCover(file1,
					file2,scoreFile,schemaFile,dup,nondup);
			System.out.println(t);
			PrintWriter out=new PrintWriter(new File(BK));
			out.println(t);
			out.close();
		}
		
		public static void trainSVMSup()throws IOException{
			
			Hetero.generateSVMLightFileSup(file1,
					file2,schemaFile,dupfile,nondupfile, svmFileSup);
			
			
			
			svm_train t=new svm_train();
			String arg="-b 1 "+(svmFileSup)+" "+(svmModelSup);
			
			t.run(arg.split(" "));
		}

		public static void trainSVM()throws IOException{
			int dup=num;
			int nondup=num;
			Hetero.generateSVMLightFile(file1,
					file2,schemaFile,scoreFile,dup,nondup, svmFile);
			
			
			
			svm_train t=new svm_train();
			String arg="-b 1 "+(svmFile)+" "+(svmModel);
			
			t.run(arg.split(" "));
		}

		public static void trainSVMIter()throws IOException{
			Hetero.generateSVMLightFileSup(file1,
					file2,schemaFile,iterFile,iterNonDupFile, iterFileSup);
			
			
			
			svm_train t=new svm_train();
			String arg="-b 1 "+(iterFileSup)+" "+(iterModelSup);
			
			t.run(arg.split(" "));
		}
		
	}

	public static class scalability_experiments{
		static String prefix="/host/heteroDatasets/cikm_experiments/scalability/dataC";
		public static void writeBK(int num)throws IOException{
			prefix=prefix+num+"/";
			String records=prefix+"data"+num+".csv";
			int dup=num/5;
			
			//int dup=22000;
			int nondup=1000;
			String t=GenerateFeaturesFile.Homo.generateBKString(records,prefix+"sortedScores/C",dup,nondup);
			System.out.println(t);
			PrintWriter out=new PrintWriter(new File(prefix+"BK"));
			out.println(t);
			out.close();
		}
		
		public static void trainSVM(int num)throws IOException{
			prefix=prefix+num+"/";
			String records=prefix+"data"+num+".csv";
			int dup=num/5;
			int nondup=1000;
			Homo.generateSVMLightFile(records,prefix+"sortedScores/C",dup,nondup, prefix+"svmlight");
			
			
			
			svm_train t=new svm_train();
			String arg="-b 1 "+(prefix+"svmlight ")+(prefix+"svm_model");
			
			t.run(arg.split(" "));
		}
	}

	public static class Cora_experiments{
		static String prefix="/host/heteroDatasets/cikm_experiments/cora/";
		public static void writeBK()throws IOException{
			
			int dup=8600;
			int nondup=1000;
			String t=GenerateFeaturesFile.Homo.generateBKString(prefix+"cora.csv",prefix+"sortedScores/C",dup,nondup);
			System.out.println(t);
			PrintWriter out=new PrintWriter(new File(prefix+"BK"));
			out.println(t);
			out.close();
		}
		
		public static void trainSVM()throws IOException{
			int dup=7510;
			int nondup=1000;
			Homo.generateSVMLightFile(prefix+"cora.csv",prefix+"sortedScores/C",dup,nondup, prefix+"svmlight");
			
			
			
			svm_train t=new svm_train();
			String arg="-b 1 "+(prefix+"svmlight ")+(prefix+"svm_model_7510_1000_unsup");
			
			t.run(arg.split(" "));
			
			
		}
	}
	
	public static class Census5000_experiments{
		static String prefix="/host/heteroDatasets/cikm_experiments/dataC5000/";
		public static void writeBK()throws IOException{
			
			int dup=22000;
			int nondup=1000;
			String t=GenerateFeaturesFile.Homo.generateBKString(prefix+"dataC5000.csv",prefix+"sortedScores/C",dup,nondup);
			System.out.println(t);
			PrintWriter out=new PrintWriter(new File(prefix+"BK"));
			out.println(t);
			out.close();
		}
		
		public static void trainSVM()throws IOException{
			int dup=3660;
			int nondup=1000;
			Homo.generateSVMLightFile(prefix+"dataC5000.csv",prefix+"sortedScores/C",dup,nondup, prefix+"svmlight");
			
			
			
			svm_train t=new svm_train();
			String arg="-b 1 "+(prefix+"svmlight ")+(prefix+"svm_model_3660_unsup");
			
			t.run(arg.split(" "));
		}
	}
	
	public static class Restaurant_experiments{
		static String prefix="/host/heteroDatasets/cikm_experiments/restaurant/";
		
		
		
		public static void writeBK()throws IOException{
			
			int dup=5000;
			int nondup=1000;
			String t=GenerateFeaturesFile.Homo.generateBKString(prefix+"restaurant.csv",prefix+"sortedScores/C",dup,nondup);
			System.out.println(t);
			PrintWriter out=new PrintWriter(new File(prefix+"BK"));
			out.println(t);
			out.close();
		}
		
		public static void trainSVM()throws IOException{
			int dup=2500;
			int nondup=1000;
			Homo.generateSVMLightFile(prefix+"restaurant.csv",prefix+"sortedScores/C",dup,nondup, prefix+"svmlight");
			
			
			
			svm_train t=new svm_train();
			String arg="-b 1 "+(prefix+"svmlight ")+(prefix+"svm_model");
			
			t.run(arg.split(" "));
		}
	}
	
	
	public static class Parks1_experiments{
		static String prefix="/host/heteroDatasets/icde_experiments/parks1/";
		
		
		//consider simple exhaustive SBPs.
		public static void writeBKBrute()throws IOException{
			
			int dup=500;
			int nondup=1000;
			String t=GenerateFeaturesFile.Hetero.generateBKStringBrute(prefix+"National_Parks_Plus.csv",
					prefix+"national_park_service.csv",prefix+"sortedScores/J",dup,nondup);
			System.out.println(t);
			PrintWriter out=new PrintWriter(new File(prefix+"BK"));
			out.println(t);
			out.close();
		}
		
		public static void writeBKUnsup()throws IOException{
			
			int dup=161;
			int nondup=161;
			String t=GenerateFeaturesFile.Hetero.generateBKStringPermute(prefix+"National_Parks_Plus.csv",
					prefix+"national_park_service.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
			System.out.println(t);
			PrintWriter out=new PrintWriter(new File(prefix+"BK"));
			out.println(t);
			out.close();
		}

		public static void writeBKSetCover(int num)throws IOException{
			
			int dup=num;
			int nondup=num;
			
			
			String t=GenerateFeaturesFile.Hetero.generateBKStringSetCover(prefix+"National_Parks_Plus.csv",
					prefix+"national_park_service.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
			System.out.println(t);
			PrintWriter out=new PrintWriter(new File(prefix+"BK"));
			out.println(t);
			out.close();
		}
		
	}

	//for homogeneous schemas
	public static class Homo{
	
		//records is the original record (csv) file (without schema header)
		//supervision is the sorted file that contains scored pairs
		//dup (nondup) is number of top (bottom) pairs to train on
		//Method will print warning if dups and nondups overlap
	public static String generateBKString(String records, String supervision, int dup, int nondup)throws IOException{
		Scanner in=new Scanner(new FileReader(records));
		ArrayList<String> recs=new ArrayList<String>();
		while(in.hasNextLine())
			recs.add(in.nextLine());
		
		in.close();
		
		
		in=new Scanner(new FileReader(supervision));
		ArrayList<String> superv=new ArrayList<String>();
		while(in.hasNextLine())
			superv.add(in.nextLine());
		
		in.close();
		
		if(dup>superv.size())
			dup=superv.size();
		
		if(nondup>superv.size())
			nondup=superv.size();
		
		if(dup+nondup>superv.size())
			System.out.println("Warning: Overlap of "+((dup+nondup)-superv.size()));
		
		ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i=0; i<dup; i++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			dupFeatures.add(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
		for(int i=superv.size()-1,j=0; j<nondup; i--,j++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			nondupFeatures.add(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)));
		}
		
		return generateBKFileHomo(dupFeatures,nondupFeatures);
		
	}
	
	//returns blocking key based on input feature vectors
	@SuppressWarnings("unchecked")
	public static String generateBKFileHomo(ArrayList<ArrayList<Integer>> dupFeatures, 
			ArrayList<ArrayList<Integer>> nondupFeatures)throws IOException{
		String res="";
		Fisher c=new Fisher(dupFeatures.get(0).size(),dupFeatures,nondupFeatures);
		c.computeStatistics();
		FeatureAnalysis d=new FeatureAnalysis(c);
		
		//compute  Blocking Keys
		LearnDisjunctHomo e=null;
		int num_atts=dupFeatures.get(0).size()/Parameters.num_feats;
		
		
		
		e=new LearnDisjunctHomo(d, 1, 2, num_atts, Parameters.recall);
		ArrayList<String> codes=null;
		
			e.populateDisjunction_Features();
			codes=e.codes();
		
		//print to file
		
		for(int i=0; i<codes.size()-1; i++)
			res+=(codes.get(i)+"\t");
		
		res=res+codes.get(codes.size()-1);
		
		return res;
		
	}
	
	//returns feature vector of records
	public static ArrayList<Integer> getFeatureWeightsHomo(String record1, String record2){
				int[] weight=null;
				String r1=null;
				String r2=null;
				for(String forb:Parameters.forbiddenwords)
				{
					r1=record1.replace(forb,"");
					r2=record2.replace(forb,"");
				}
				String[] tokens1=null;
				String[] tokens2=null;
				
				try {
				 tokens1=(new CSVParser()).parseLine(r1);
				 
					tokens2=(new CSVParser()).parseLine(r2);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				//System.out.println(tokens1.length);
				ArrayList<Integer> result=new ArrayList<Integer>();
				for(int code=1;code<=Parameters.num_feats;code++){
				if(code==1)
					weight=HomoBK.ExactMatch(tokens1, tokens2);
				else if(code==2)
					weight=HomoBK.CommonToken(tokens1, tokens2);
				else if(code==3)
					weight=HomoBK.CommonInteger(tokens1, tokens2);
				else if(code==4)
					weight=HomoBK.CommonOrOffByOneInteger(tokens1, tokens2);
				else if(code==5)
					weight=HomoBK.CommonNFirst(tokens1, tokens2,3);
				else if(code==6)
					weight=HomoBK.CommonNFirst(tokens1, tokens2,5);
				else if(code==7)
					weight=HomoBK.CommonNFirst(tokens1, tokens2,7);
				else if(code==8)
					weight=HomoBK.CommonTokenNGram(tokens1, tokens2,2);
				else if(code==9)
					weight=HomoBK.CommonTokenNGram(tokens1, tokens2,4);
				else if(code==10)
					weight=HomoBK.CommonTokenNGram(tokens1, tokens2,6);
				else if(code>10&&code<=18){
					int p=code-11;
					String[] vals={"000","001","010","011","100","101","110","111"};
					String val=vals[p];
					boolean reverse= val.charAt(0)=='0' ? false : true;
					boolean mod= val.charAt(1)=='0' ? false : true;
					boolean four= val.charAt(2)=='0' ? false : true;
					weight=HomoBK.soundex(tokens1, tokens2, reverse, mod, four);
					
				}
				else if(code==19){
					weight=HomoBK.CommonAlphaNumeric(tokens1, tokens2);
				}
				else if(code>19&&code<=28){
					int p=code-20;
					String[] vals={"soundex","caverphone1","caverphone2","colognephonetic","doublemetaphone"
							,"matchrating","metaphone","nysiis","refinedsoundex"};
					weight=HomoBK.phonetic(tokens1,tokens2,vals[p]);
				}
				
					
				concatenate(result,weight);
				}
				return result;
			}
	
	//writes libSVM file on which SVM will be trained. Parameters similar to generateBKString
	public static void generateSVMLightFile(String records, String supervision, int dup, int nondup, String output)throws IOException{
		Scanner in=new Scanner(new FileReader(records));
		ArrayList<String> recs=new ArrayList<String>();
		while(in.hasNextLine())
			recs.add(in.nextLine());
		
		in.close();
		
		
		in=new Scanner(new FileReader(supervision));
		ArrayList<String> superv=new ArrayList<String>();
		while(in.hasNextLine())
			superv.add(in.nextLine());
		
		in.close();
		
		if(dup>superv.size())
			dup=superv.size();
		
		if(nondup>superv.size())
			nondup=superv.size();
		
		if(dup+nondup>superv.size())
			System.out.println("Warning: Overlap of "+((dup+nondup)-superv.size()));
		
		ArrayList<ArrayList<Double>> dupFeatures=new ArrayList<ArrayList<Double>>();
		for(int i=0; i<dup; i++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			dupFeatures.add(getSVMFeatures(recs.get(index1),recs.get(index2)));
			//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
		}
		
		ArrayList<ArrayList<Double>> nondupFeatures=new ArrayList<ArrayList<Double>>();
		for(int i=superv.size()-1,j=0; j<nondup; i--,j++){
			String[] index=superv.get(i).split("\t")[0].split(" ");
			int index1=Integer.parseInt(index[0]);
			int index2=Integer.parseInt(index[1]);
			nondupFeatures.add(getSVMFeatures(recs.get(index1),recs.get(index2)));
		}
		
		generateSVMLightFile(output,dupFeatures,nondupFeatures);
	}
	
	//Write out feature vectors in SVM file in libsvm format
	public static void generateSVMLightFile(String output, ArrayList<ArrayList<Double>> dups, ArrayList<ArrayList<Double>> nondups)throws IOException{
		PrintWriter out=new PrintWriter(new File(output));
		
		
		for(int i=0; i<dups.size(); i++){
			
			String t=new String("1");
			
			for(int j=0; j<dups.get(i).size(); j++)
				t+=(" "+(j+1)+":"+dups.get(i).get(j));
			out.println(t);
		}
		
		for(int i=0; i<nondups.size(); i++){
			
			String t=new String("-1");
			
			for(int j=0; j<nondups.get(i).size(); j++)
				t+=(" "+(j+1)+":"+nondups.get(i).get(j));
			out.println(t);
		}
		
		out.close();
}

	//returns SVM feature vector of records
	public static ArrayList<Double> getSVMFeatures(String record1, String record2){
				int[] weight=null;
				double[] weight2=null;
				String r1=null;
				String r2=null;
				for(String forb:Parameters.forbiddenwords)
				{
					r1=record1.replace(forb,"");
					r2=record2.replace(forb,"");
				}
				String[] tokens1=null;
				String[] tokens2=null;
				
				try {
				 tokens1=(new CSVParser()).parseLine(r1);
				 
					tokens2=(new CSVParser()).parseLine(r2);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				//System.out.println(tokens1.length);
				ArrayList<Double> result=new ArrayList<Double>();
				for(int code=1;code<=Parameters.num_feats;code++){
				if(code==1)
					weight=HomoBK.ExactMatch(tokens1, tokens2);
				else if(code==2)
					weight=HomoBK.CommonToken(tokens1, tokens2);
				else if(code==3)
					weight=HomoBK.CommonInteger(tokens1, tokens2);
				else if(code==4)
					weight=HomoBK.CommonOrOffByOneInteger(tokens1, tokens2);
				else if(code==5)
					weight=HomoBK.CommonNFirst(tokens1, tokens2,3);
				else if(code==6)
					weight=HomoBK.CommonNFirst(tokens1, tokens2,5);
				else if(code==7)
					weight=HomoBK.CommonNFirst(tokens1, tokens2,7);
				else if(code==8)
					weight=HomoBK.CommonTokenNGram(tokens1, tokens2,2);
				else if(code==9)
					weight=HomoBK.CommonTokenNGram(tokens1, tokens2,4);
				else if(code==10)
					weight=HomoBK.CommonTokenNGram(tokens1, tokens2,6);
				else if(code>10&&code<=18){
					int p=code-11;
					String[] vals={"000","001","010","011","100","101","110","111"};
					String val=vals[p];
					boolean reverse= val.charAt(0)=='0' ? false : true;
					boolean mod= val.charAt(1)=='0' ? false : true;
					boolean four= val.charAt(2)=='0' ? false : true;
					weight=HomoBK.soundex(tokens1, tokens2, reverse, mod, four);
					
				}
				else if(code==19){
					weight=HomoBK.CommonAlphaNumeric(tokens1, tokens2);
				}
				else if(code>19&&code<=28){
					int p=code-20;
					String[] vals={"soundex","caverphone1","caverphone2","colognephonetic","doublemetaphone"
							,"matchrating","metaphone","nysiis","refinedsoundex"};
					weight=HomoBK.phonetic(tokens1,tokens2,vals[p]);
				}	//num feats end
				//svm specific feats beginCommonAlphaNumeric
				else if(code==29){
					weight2=SVMFeatures.AffineGap(tokens1, tokens2);
				}
				else if(code==30){
					weight2=SVMFeatures.DirichletJS(tokens1, tokens2);
				}
				else if(code==31){
					weight2=SVMFeatures.Jaro(tokens1, tokens2);
				}
				else if(code==32){
					weight2=SVMFeatures.Jaccard(tokens1, tokens2);
				}
				else if(code==33){
					weight2=SVMFeatures.JaroWinkler(tokens1, tokens2);
				}
				else if(code==34){
					weight2=SVMFeatures.MongeElkan(tokens1, tokens2);
				}
				else if(code==35){
					weight2=SVMFeatures.Levenstein(tokens1, tokens2);
				}
				else if(code==36){
					weight2=SVMFeatures.SmithWaterman(tokens1, tokens2);
				}
				else if(code==37){
					weight2=SVMFeatures.NeedlemanWunsch(tokens1, tokens2);
				}
				else if(code==38){
					weight2=SVMFeatures.JelinekMercerJS(tokens1, tokens2);
				}
					if(code<=Parameters.num_feats)
						concatenateDoubleInt(result,weight);
					else
						concatenateDoubleDouble(result,weight2);
				}
				return result;
			}
	
	}
			//for heterogeneous schemas
		public static class Hetero{
		
			//records is the original record (csv) file (without schema header)
			//supervision is the sorted file that contains scored pairs
			//dup (nondup) is number of top (bottom) pairs to train on
			//Method will print warning if dups and nondups overlap
			
			//Method is still unsupervised: see original heterogeneous blocking paper
		public static String generateBKStringBrute(String records1, String records2, String supervision, int dup, int nondup)throws IOException{
			Scanner in1=new Scanner(new FileReader(records1));
			Scanner in2=new Scanner(new FileReader(records2));
			ArrayList<String> recs1=new ArrayList<String>();
			ArrayList<String> recs2=new ArrayList<String>();
			while(in1.hasNextLine())
				recs1.add(in1.nextLine());
			
			in1.close();
			
			while(in2.hasNextLine())
				recs2.add(in2.nextLine());
			
			in2.close();
			
			int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
			int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
			
			Scanner in=new Scanner(new FileReader(supervision));
			ArrayList<String> superv=new ArrayList<String>();
			while(in.hasNextLine())
				superv.add(in.nextLine());
			
			in.close();
			
			if(dup>superv.size())
				dup=superv.size();
			
			if(nondup>superv.size())
				nondup=superv.size();
			
			if(dup+nondup>superv.size())
				System.out.println("Warning: Overlap of "+((dup+nondup)-superv.size()));
			
			
			HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
			
			
			ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
			for(int i=0; i<dup; i++){
				String[] index=superv.get(i).split("\t")[0].split(" ");
				int index1=Integer.parseInt(index[0]);
				int index2=Integer.parseInt(index[1]);
				dupFeatures.add(getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
				//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
			}
			
			ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
			for(int i=superv.size()-1,j=0; j<nondup; i--,j++){
				String[] index=superv.get(i).split("\t")[0].split(" ");
				int index1=Integer.parseInt(index[0]);
				int index2=Integer.parseInt(index[1]);
				nondupFeatures.add(getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
			}
			
			return generateBKFileHetero(dupFeatures,nondupFeatures,att1,att2);
			
		}
		
		//records is the original record (csv) file (without schema header)
			//supervision is the sorted file that contains scored pairs
			//dup  is number of top  pairs to train on
			//nondup always assumed to be less than total permuts.
			
			
		public static String generateBKStringPermute(String records1, String records2, String supervision, String schema, int dup, int nondup)throws IOException{
			Scanner in1=new Scanner(new FileReader(records1));
			Scanner in2=new Scanner(new FileReader(records2));
			ArrayList<String> recs1=new ArrayList<String>();
			ArrayList<String> recs2=new ArrayList<String>();
			while(in1.hasNextLine())
				recs1.add(in1.nextLine());
			
			in1.close();
			
			while(in2.hasNextLine())
				recs2.add(in2.nextLine());
			
			in2.close();
			
			int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
			int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
			
			HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
			
			for(int i=0; i<att1; i++){
				HeteroBK.ignore.put(i,new HashSet<Integer>());
				for(int j=0; j<att2; j++)
					HeteroBK.ignore.get(i).add(j);
			}
			
			
			
			
			Scanner sc=new Scanner(new FileReader(schema));
			while(sc.hasNextLine()){
				String[] p=sc.nextLine().split(" ");
				int index1=Integer.parseInt(p[0]);
				int index2=Integer.parseInt(p[1]);
				if(HeteroBK.ignore.containsKey(index1))
					HeteroBK.ignore.get(index1).remove(index2);
				if(HeteroBK.ignore.get(index1)==null)
					HeteroBK.ignore.remove(index1);
						
				
			}
			//System.out.println(HeteroBK.ignore.get(2));
			sc.close();
			
			
			Scanner in=new Scanner(new FileReader(supervision));
			ArrayList<String> superv=new ArrayList<String>();
			while(in.hasNextLine())
				superv.add(in.nextLine());
			
			in.close();
			
			if(dup>superv.size())
				dup=superv.size();
			
			
			
			ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
			int[] dupInd1=new int[dup];
			int[] dupInd2=new int[dup];
			for(int i=0; i<dup; i++){
				String[] index=superv.get(i).split("\t")[0].split(" ");
				int index1=Integer.parseInt(index[0]);
				int index2=Integer.parseInt(index[1]);
				dupInd1[i]=index1;
				dupInd2[i]=index2;
				dupFeatures.add(getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
				//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
			}
			
			ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
			int[][] nondupInd=randomDuplicates(dupInd1, dupInd2, nondup);
			System.out.println("Nondups generatedby RandomDuplicates: "+nondupInd.length);
			for(int[] i:nondupInd){
				
				
				nondupFeatures.add(getFeatureWeightsHetero(recs1.get(i[0]),recs2.get(i[1])));
			}
			
			return generateBKFileHetero(dupFeatures,nondupFeatures,att1,att2);
			
		}

		
			
		//records is the original record (csv) file (without schema header)
			//supervision is the sorted file that contains scored pairs
			//dup  is number of top  pairs to train on
			//no nondups
			
			
		public static String generateBKStringSetCover(String records1, String records2, String supervision, String schema, int dup, int nondup)throws IOException{
			Scanner in1=new Scanner(new FileReader(records1));
			Scanner in2=new Scanner(new FileReader(records2));
			ArrayList<String> recs1=new ArrayList<String>();
			ArrayList<String> recs2=new ArrayList<String>();
			while(in1.hasNextLine())
				recs1.add(in1.nextLine());
			
			in1.close();
			
			while(in2.hasNextLine())
				recs2.add(in2.nextLine());
			
			in2.close();
			
			int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
			int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
			
			HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
			
			for(int i=0; i<att1; i++){
				HeteroBK.ignore.put(i,new HashSet<Integer>());
				for(int j=0; j<att2; j++)
					HeteroBK.ignore.get(i).add(j);
			}
			
			
			
			
			Scanner sc=new Scanner(new FileReader(schema));
			while(sc.hasNextLine()){
				String[] p=sc.nextLine().split(" ");
				int index1=Integer.parseInt(p[0]);
				int index2=Integer.parseInt(p[1]);
				if(HeteroBK.ignore.containsKey(index1))
					HeteroBK.ignore.get(index1).remove(index2);
				if(HeteroBK.ignore.get(index1)==null)
					HeteroBK.ignore.remove(index1);
						
				
			}
			//System.out.println(HeteroBK.ignore.get(2));
			sc.close();
			
			Scanner in=new Scanner(new FileReader(supervision));
			ArrayList<String> superv=new ArrayList<String>();
			while(in.hasNextLine())
				superv.add(in.nextLine());
			
			in.close();
			
			if(dup>superv.size())
				dup=superv.size();
			
			
			
			ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
			int[] dupInd1=new int[dup];
			int[] dupInd2=new int[dup];
			for(int i=0; i<dup; i++){
				String[] index=superv.get(i).split("\t")[0].split(" ");
				int index1=Integer.parseInt(index[0]);
				int index2=Integer.parseInt(index[1]);
				dupInd1[i]=index1;
				dupInd2[i]=index2;
				dupFeatures.add(getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
				//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
			}
			
			ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
			int[][] nondupInd=randomDuplicates(dupInd1, dupInd2, nondup);
			System.out.println("Nondups generatedby RandomDuplicates: "+nondupInd.length);
			for(int[] i:nondupInd){
				
				
				nondupFeatures.add(getFeatureWeightsHetero(recs1.get(i[0]),recs2.get(i[1])));
			}
			
			
			
			return generateBKFileSetCover(dupFeatures,nondupFeatures,att1,att2);
			
		}

		//returns blocking key based on input feature vectors
		@SuppressWarnings("unchecked")
		public static String generateBKFileHeteroBilenko(ArrayList<ArrayList<Integer>> dupFeatures, 
				ArrayList<ArrayList<Integer>> nondupFeatures, int att1, int att2)throws IOException{
			String res="";
			Fisher c=new Fisher(dupFeatures.get(0).size(),dupFeatures,nondupFeatures);
			c.computeStatistics();
			FeatureAnalysis d=new FeatureAnalysis(c);
			
			//compute  Blocking Keys
			LearnDisjunctHetero e=null;
			//int num_atts=dupFeatures.get(0).size()/Parameters.num_feats;
			
			
			
			e=new LearnDisjunctHetero(d, 1, 2, att1, att2, Parameters.recall);
			ArrayList<String> codes=null;
			
				e.populateDisjunction_Bilenko();
				codes=e.codes();
			
			//print to file
			
			for(int i=0; i<codes.size()-1; i++)
				res+=(codes.get(i)+"\t");
			
			res=res+codes.get(codes.size()-1);
			
			return res;
			
		}
		
		//returns feature vector of records without considering schema match
		public static ArrayList<Integer> getFeatureWeightsHetero(String record1, String record2){
					int[] weight=null;
					String r1=null;
					String r2=null;
					for(String forb:Parameters.forbiddenwords)
					{
						r1=record1.replace(forb,"");
						r2=record2.replace(forb,"");
					}
					String[] tokens1=null;
					String[] tokens2=null;
					
					try {
					 tokens1=(new CSVParser()).parseLine(r1);
					 
						tokens2=(new CSVParser()).parseLine(r2);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					//System.out.println(tokens1.length);
					ArrayList<Integer> result=new ArrayList<Integer>();
					for(int code=1;code<=Parameters.num_feats;code++){
					if(code==1)
						weight=HeteroBK.ExactMatch(tokens1, tokens2);
					else if(code==2)
						weight=HeteroBK.CommonToken(tokens1, tokens2);
					else if(code==3)
						weight=HeteroBK.CommonInteger(tokens1, tokens2);
					else if(code==4)
						weight=HeteroBK.CommonOrOffByOneInteger(tokens1, tokens2);
					else if(code==5)
						weight=HeteroBK.CommonNFirst(tokens1, tokens2,3);
					else if(code==6)
						weight=HeteroBK.CommonNFirst(tokens1, tokens2,5);
					else if(code==7)
						weight=HeteroBK.CommonNFirst(tokens1, tokens2,7);
					else if(code==8)
						weight=HeteroBK.CommonTokenNGram(tokens1, tokens2,2);
					else if(code==9)
						weight=HeteroBK.CommonTokenNGram(tokens1, tokens2,4);
					else if(code==10)
						weight=HeteroBK.CommonTokenNGram(tokens1, tokens2,6);
					else if(code>10&&code<=18){
						int p=code-11;
						String[] vals={"000","001","010","011","100","101","110","111"};
						String val=vals[p];
						boolean reverse= val.charAt(0)=='0' ? false : true;
						boolean mod= val.charAt(1)=='0' ? false : true;
						boolean four= val.charAt(2)=='0' ? false : true;
						weight=HeteroBK.soundex(tokens1, tokens2, reverse, mod, four);
						
					}
					else if(code==19){
						weight=HeteroBK.CommonAlphaNumeric(tokens1, tokens2);
					}
					else if(code>19&&code<=28){
						int p=code-20;
						String[] vals={"soundex","caverphone1","caverphone2","colognephonetic","doublemetaphone"
								,"matchrating","metaphone","nysiis","refinedsoundex"};
						weight=HeteroBK.phonetic(tokens1,tokens2,vals[p]);
					}
					
						
					concatenate(result,weight);
					}
					return result;
				}
		
		//return n by 2 matrix, such that for no i<=n, a[i]=res[i][0] and b[i]=res[i][1]
		private static int[][] randomDuplicates(int[] a, int[] b, int n){
			if(a.length!=b.length||a.length==0)
				return null;
			int[][] res=new int[n][2];
			ArrayList<String> forbidden=new ArrayList<String>();
			for(int i=0; i<a.length; i++)
				forbidden.add(new String(a[i]+" "+b[i]));
			Random p=new Random(System.currentTimeMillis());
			int count=0;
			while(count<n){
				int q1=p.nextInt(n);
				int q2=p.nextInt(n);
				String t=a[q1]+" "+b[q2];
				if(forbidden.contains(t))
					continue;
				else{
					
					forbidden.add(t);
					res[count][0]=a[q1];
					res[count][1]=b[q2];
					count++;
				}
			}
			
			
			return res;
			
		}

		//records is the original record (csv) file (without schema header)
			//supervision is the sorted file that contains scored pairs
			//dup (nondup) is number of top (bottom) pairs to train on
			//Method will print warning if dups and nondups overlap
			
			
		public static String generateBKString(String records1, String records2, String supervision, String schema, int dup, int nondup)throws IOException{
			Scanner in1=new Scanner(new FileReader(records1));
			Scanner in2=new Scanner(new FileReader(records2));
			ArrayList<String> recs1=new ArrayList<String>();
			ArrayList<String> recs2=new ArrayList<String>();
			while(in1.hasNextLine())
				recs1.add(in1.nextLine());
			
			in1.close();
			
			while(in2.hasNextLine())
				recs2.add(in2.nextLine());
			
			in2.close();
			
			int att1=(new CSVParser()).parseLine(recs1.get(0)).length;
			int att2=(new CSVParser()).parseLine(recs2.get(0)).length;
			
			HeteroBK.ignore=new HashMap<Integer,HashSet<Integer>>();
			
			for(int i=0; i<att1; i++){
				HeteroBK.ignore.put(i,new HashSet<Integer>());
				for(int j=0; j<att2; j++)
					HeteroBK.ignore.get(i).add(j);
			}
			
			
			
			
			Scanner sc=new Scanner(new FileReader(schema));
			while(sc.hasNextLine()){
				String[] p=sc.nextLine().split(" ");
				int index1=Integer.parseInt(p[0]);
				int index2=Integer.parseInt(p[1]);
				if(HeteroBK.ignore.containsKey(index1))
					HeteroBK.ignore.get(index1).remove(index2);
				if(HeteroBK.ignore.get(index1)==null)
					HeteroBK.ignore.remove(index1);
						
				
			}
			//System.out.println(HeteroBK.ignore.get(2));
			sc.close();
			
			Scanner in=new Scanner(new FileReader(supervision));
			ArrayList<String> superv=new ArrayList<String>();
			while(in.hasNextLine())
				superv.add(in.nextLine());
			
			in.close();
			
			if(dup>superv.size())
				dup=superv.size();
			
			if(nondup>superv.size())
				nondup=superv.size();
			
			if(dup+nondup>superv.size())
				System.out.println("Warning: Overlap of "+((dup+nondup)-superv.size()));
			
			ArrayList<ArrayList<Integer>> dupFeatures=new ArrayList<ArrayList<Integer>>();
			for(int i=0; i<dup; i++){
				String[] index=superv.get(i).split("\t")[0].split(" ");
				int index1=Integer.parseInt(index[0]);
				int index2=Integer.parseInt(index[1]);
				dupFeatures.add(getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
				//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
			}
			
			ArrayList<ArrayList<Integer>> nondupFeatures=new ArrayList<ArrayList<Integer>>();
			for(int i=superv.size()-1,j=0; j<nondup; i--,j++){
				String[] index=superv.get(i).split("\t")[0].split(" ");
				int index1=Integer.parseInt(index[0]);
				int index2=Integer.parseInt(index[1]);
				nondupFeatures.add(getFeatureWeightsHetero(recs1.get(index1),recs2.get(index2)));
			}
			
			return generateBKFileHetero(dupFeatures,nondupFeatures,att1,att2);
			
		}

		//returns blocking key based on input feature vectors
		@SuppressWarnings("unchecked")
		public static String generateBKFileHetero(ArrayList<ArrayList<Integer>> dupFeatures, 
				ArrayList<ArrayList<Integer>> nondupFeatures, int att1, int att2)throws IOException{
			String res="";
			Fisher c=new Fisher(dupFeatures.get(0).size(),dupFeatures,nondupFeatures);
			c.computeStatistics();
			FeatureAnalysis d=new FeatureAnalysis(c);
			
			//compute  Blocking Keys
			LearnDisjunctHetero e=null;
			//int num_atts=dupFeatures.get(0).size()/Parameters.num_feats;
			
			
			
			e=new LearnDisjunctHetero(d, 1, 2, att1, att2, Parameters.recall);
			ArrayList<String> codes=null;
			
			if(Parameters.DNF){
				e.populateDNF_Features(2);
				codes=e.codesDNF();
			}else{
				e.populateDisjunction_Features();
				codes=e.codes();
			}
			//print to file
			
			for(int i=0; i<codes.size()-1; i++)
				res+=(codes.get(i)+"\t");
			
			res=res+codes.get(codes.size()-1);
			
			return res;
			
		}

		//returns blocking key based on input feature vectors
		public static String generateBKFileSetCover(ArrayList<ArrayList<Integer>> dupFeatures, ArrayList<ArrayList<Integer>> nondupFeatures,
				int att1, int att2)throws IOException{
			String res="";
			SetCovering t=new SetCovering(dupFeatures,nondupFeatures,att1,att2);
			ArrayList<String> codes=null;
			if(!Parameters.DNF){
				codes=t.codes();
			}else{
				
			}
			//print to file
			
			for(int i=0; i<codes.size()-1; i++)
				res+=(codes.get(i)+"\t");
			
			res=res+codes.get(codes.size()-1);
			
			
			return res;
			
		}

		//writes libSVM file on which SVM will be trained. Parameters similar to generateBKString
		public static void generateSVMLightFile(String records1, String records2, String schema, String supervision, int dup, int nondup, String output)throws IOException{
			Scanner in1=new Scanner(new FileReader(records1));
			Scanner in2=new Scanner(new FileReader(records2));
			ArrayList<String> recs1=new ArrayList<String>();
			ArrayList<String> recs2=new ArrayList<String>();
			while(in1.hasNextLine())
				recs1.add(in1.nextLine());
			
			in1.close();
			
			while(in2.hasNextLine())
				recs2.add(in2.nextLine());
			
			in2.close();
			
			
			HeteroBKSVM.column1=new ArrayList<Integer>();
			HeteroBKSVM.column2=new ArrayList<Integer>();
			
		
			
			Scanner sc=new Scanner(new FileReader(schema));
			while(sc.hasNextLine()){
				String[] p=sc.nextLine().split(" ");
				int index1=Integer.parseInt(p[0]);
				int index2=Integer.parseInt(p[1]);
				HeteroBKSVM.column1.add(index1);
				HeteroBKSVM.column2.add(index2);
						
				
			}
			
			sc.close();
			
			
			Scanner in=new Scanner(new FileReader(supervision));
			ArrayList<String> superv=new ArrayList<String>();
			while(in.hasNextLine())
				superv.add(in.nextLine());
			
			in.close();
			
			if(dup>superv.size())
				dup=superv.size();
			
			if(nondup>superv.size())
				nondup=superv.size();
			
			if(dup+nondup>superv.size())
				System.out.println("Warning: Overlap of "+((dup+nondup)-superv.size()));
			
			ArrayList<ArrayList<Double>> dupFeatures=new ArrayList<ArrayList<Double>>();
			for(int i=0; i<dup; i++){
				String[] index=superv.get(i).split("\t")[0].split(" ");
				int index1=Integer.parseInt(index[0]);
				int index2=Integer.parseInt(index[1]);
				dupFeatures.add(getSVMFeatures(recs1.get(index1),recs2.get(index2)));
				//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
			}
			
			ArrayList<ArrayList<Double>> nondupFeatures=new ArrayList<ArrayList<Double>>();
			for(int i=superv.size()-1,j=0; j<nondup; i--,j++){
				String[] index=superv.get(i).split("\t")[0].split(" ");
				int index1=Integer.parseInt(index[0]);
				int index2=Integer.parseInt(index[1]);
				nondupFeatures.add(getSVMFeatures(recs1.get(index1),recs2.get(index2)));
			}
			
			//we can re-use the function
			Homo.generateSVMLightFile(output,dupFeatures,nondupFeatures);
		}

		//writes libSVM file on which SVM will be trained. Parameters similar to generateBKString
		public static void generateSVMLightFileSup(String records1, String records2, String schema, String dup, String nondup, String output)throws IOException{
			Scanner in1=new Scanner(new FileReader(records1));
			Scanner in2=new Scanner(new FileReader(records2));
			ArrayList<String> recs1=new ArrayList<String>();
			ArrayList<String> recs2=new ArrayList<String>();
			while(in1.hasNextLine())
				recs1.add(in1.nextLine());
			
			in1.close();
			
			while(in2.hasNextLine())
				recs2.add(in2.nextLine());
			
			in2.close();
			
			HeteroBKSVM.column1=new ArrayList<Integer>();
			HeteroBKSVM.column2=new ArrayList<Integer>();
			
		
			
			Scanner sc=new Scanner(new FileReader(schema));
			while(sc.hasNextLine()){
				String[] p=sc.nextLine().split(" ");
				int index1=Integer.parseInt(p[0]);
				int index2=Integer.parseInt(p[1]);
				HeteroBKSVM.column1.add(index1);
				HeteroBKSVM.column2.add(index2);
						
				
			}
			
			sc.close();
			
			Scanner in=new Scanner(new FileReader(dup));
			ArrayList<String> dups=new ArrayList<String>();
			while(in.hasNextLine())
				dups.add(in.nextLine());
			in.close();
			
			in=new Scanner(new FileReader(nondup));
			ArrayList<String> nondups=new ArrayList<String>();
			while(in.hasNextLine())
				nondups.add(in.nextLine());
			in.close();
			
			ArrayList<ArrayList<Double>> dupFeatures=new ArrayList<ArrayList<Double>>();
			for(int i=0; i<dups.size(); i++){
				String[] index=dups.get(i).split(" ");
				int index1=Integer.parseInt(index[0]);
				int index2=Integer.parseInt(index[1]);
				dupFeatures.add(getSVMFeatures(recs1.get(index1),recs2.get(index2)));
				//System.out.println(getFeatureWeightsHomo(recs.get(index1),recs.get(index2)).size());
			}
			
			ArrayList<ArrayList<Double>> nondupFeatures=new ArrayList<ArrayList<Double>>();
			for(int i=0; i<nondups.size(); i++){
				String[] index=nondups.get(i).split(" ");
				int index1=Integer.parseInt(index[0]);
				int index2=Integer.parseInt(index[1]);
				nondupFeatures.add(getSVMFeatures(recs1.get(index1),recs2.get(index2)));
			}
			
			//we can re-use the function
			Homo.generateSVMLightFile(output,dupFeatures,nondupFeatures);
		}

		//returns SVM feature vector of records
		public static ArrayList<Double> getSVMFeatures(String record1, String record2){
					int[] weight=null;
					double[] weight2=null;
					String r1=null;
					String r2=null;
					for(String forb:Parameters.forbiddenwords)
					{
						r1=record1.replace(forb,"");
						r2=record2.replace(forb,"");
					}
					String[] tokens1=null;
					String[] tokens2=null;
					
					try {
					 tokens1=(new CSVParser()).parseLine(r1);
					 
						tokens2=(new CSVParser()).parseLine(r2);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					//System.out.println(tokens1.length);
					ArrayList<Double> result=new ArrayList<Double>();
					for(int code=1;code<=Parameters.num_feats;code++){
					if(code==1)
						weight=HeteroBKSVM.ExactMatch(tokens1, tokens2);
					else if(code==2)
						weight=HeteroBKSVM.CommonToken(tokens1, tokens2);
					else if(code==3)
						weight=HeteroBKSVM.CommonInteger(tokens1, tokens2);
					else if(code==4)
						weight=HeteroBKSVM.CommonOrOffByOneInteger(tokens1, tokens2);
					else if(code==5)
						weight=HeteroBKSVM.CommonNFirst(tokens1, tokens2,3);
					else if(code==6)
						weight=HeteroBKSVM.CommonNFirst(tokens1, tokens2,5);
					else if(code==7)
						weight=HeteroBKSVM.CommonNFirst(tokens1, tokens2,7);
					else if(code==8)
						weight=HeteroBKSVM.CommonTokenNGram(tokens1, tokens2,2);
					else if(code==9)
						weight=HeteroBKSVM.CommonTokenNGram(tokens1, tokens2,4);
					else if(code==10)
						weight=HeteroBKSVM.CommonTokenNGram(tokens1, tokens2,6);
					else if(code>10&&code<=18){
						int p=code-11;
						String[] vals={"000","001","010","011","100","101","110","111"};
						String val=vals[p];
						boolean reverse= val.charAt(0)=='0' ? false : true;
						boolean mod= val.charAt(1)=='0' ? false : true;
						boolean four= val.charAt(2)=='0' ? false : true;
						weight=HeteroBKSVM.soundex(tokens1, tokens2, reverse, mod, four);
						
					}
					else if(code==19){
						weight=HeteroBKSVM.CommonAlphaNumeric(tokens1, tokens2);
					}
					else if(code>19&&code<=28){
						int p=code-20;
						String[] vals={"soundex","caverphone1","caverphone2","colognephonetic","doublemetaphone"
								,"matchrating","metaphone","nysiis","refinedsoundex"};
						weight=HeteroBKSVM.phonetic(tokens1,tokens2,vals[p]);
					}	//num feats end
					//svm specific feats beginCommonAlphaNumeric: not implemented for hetero!
				/*	else if(code==29){
						weight2=SVMFeatures.AffineGap(tokens1, tokens2);
					}
					else if(code==30){
						weight2=SVMFeatures.DirichletJS(tokens1, tokens2);
					}
					else if(code==31){
						weight2=SVMFeatures.Jaro(tokens1, tokens2);
					}
					else if(code==32){
						weight2=SVMFeatures.Jaccard(tokens1, tokens2);
					}
					else if(code==33){
						weight2=SVMFeatures.JaroWinkler(tokens1, tokens2);
					}
					else if(code==34){
						weight2=SVMFeatures.MongeElkan(tokens1, tokens2);
					}
					else if(code==35){
						weight2=SVMFeatures.Levenstein(tokens1, tokens2);
					}
					else if(code==36){
						weight2=SVMFeatures.SmithWaterman(tokens1, tokens2);
					}
					else if(code==37){
						weight2=SVMFeatures.NeedlemanWunsch(tokens1, tokens2);
					}
					else if(code==38){
						weight2=SVMFeatures.JelinekMercerJS(tokens1, tokens2);
					}*/
						if(code<=Parameters.num_feats)
							concatenateDoubleInt(result,weight);
						else
							concatenateDoubleDouble(result,weight2);
					}
					return result;
				}

		

		//records is the original record (csv) file (without schema header)
			//supervision is the sorted file that contains scored pairs
			//dup (nondup) is number of top (bottom) pairs to train on
			//Method will print warning if dups and nondups overlap

		
		}
			public static class Libraries_experiments{
			static String prefix="/host/heteroDatasets/icde_experiments/libraries/";
			
			
			//consider simple exhaustive SBPs.
			public static void writeBKBrute()throws IOException{
				
				int dup=8395;
				int nondup=8395;
				String t=GenerateFeaturesFile.Hetero.generateBKStringBrute(prefix+"PublicLibraries.csv",
						prefix+"public_libraries.csv",prefix+"sortedScores/TF",dup,nondup);
				System.out.println(t);
				PrintWriter out=new PrintWriter(new File(prefix+"BK"));
				out.println(t);
				out.close();
			}
			
			public static void writeBKUnsup()throws IOException{
				
				int dup=8395;
				int nondup=8395;
				String t=GenerateFeaturesFile.Hetero.generateBKStringPermute(prefix+"PublicLibraries.csv",
						prefix+"public_libraries.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
				System.out.println(t);
				PrintWriter out=new PrintWriter(new File(prefix+"BK"));
				out.println(t);
				out.close();
			}
			
			public static void writeBKSetCover(int num)throws IOException{
				
				int dup=num;
				int nondup=num;
				
				String t=GenerateFeaturesFile.Hetero.generateBKStringSetCover(prefix+"PublicLibraries.csv",
						prefix+"public_libraries.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
				System.out.println(t);
				PrintWriter out=new PrintWriter(new File(prefix+"BK"));
				out.println(t);
				out.close();
			}
			
		}
			public static class Game2_experiments{
				static String prefix="/host/heteroDatasets/icde_experiments/game2/";
				
				
				//consider simple exhaustive SBPs.
				public static void writeBKBrute()throws IOException{
					
					int dup=374;
					int nondup=374;
					String t=GenerateFeaturesFile.Hetero.generateBKStringBrute(prefix+"ibm.csv",
							prefix+"dbpedia.csv",prefix+"sortedScores/TF",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}
				
				public static void writeBKUnsup()throws IOException{
					
					int dup=374;
					int nondup=374;
					String t=GenerateFeaturesFile.Hetero.generateBKStringPermute(prefix+"ibm.csv",
							prefix+"dbpedia.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}

				public static void writeBKSetCover(int num)throws IOException{
					
					int dup=num;
					int nondup=num;
					String t=GenerateFeaturesFile.Hetero.generateBKStringSetCover(prefix+"ibm.csv",
							prefix+"dbpedia.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}
				
			}
			public static class Game3_experiments{
				static String prefix="/host/heteroDatasets/icde_experiments/game3/";
				
				
				//consider simple exhaustive SBPs.
				public static void writeBKBrute()throws IOException{
					
					int dup=1967;
					int nondup=1967;
					String t=GenerateFeaturesFile.Hetero.generateBKStringBrute(prefix+"ibm.csv",
							prefix+"vgchartz.csv",prefix+"sortedScores/TF",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}
				
				public static void writeBKUnsup()throws IOException{
					
					int dup=1967;
					int nondup=1967;
					String t=GenerateFeaturesFile.Hetero.generateBKStringPermute(prefix+"ibm.csv",
							prefix+"vgchartz.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}

				public static void writeBKSetCover(int num)throws IOException{
					
					int dup=num;
					int nondup=num;
					String t=GenerateFeaturesFile.Hetero.generateBKStringSetCover(prefix+"ibm.csv",
							prefix+"vgchartz.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}
				
			}
			public static class Game1_experiments{
				static String prefix="/host/heteroDatasets/icde_experiments/game/";
				
				
				//consider simple exhaustive SBPs.
				public static void writeBKBrute()throws IOException{
					
					int dup=5000;
					int nondup=5000;
					String t=GenerateFeaturesFile.Hetero.generateBKStringBrute(prefix+"vgchartz.csv",
							prefix+"dbpedia.csv",prefix+"sortedScores/TF",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}
				
				public static void writeBKUnsup()throws IOException{
					
					int dup=5000;
					int nondup=5000;
					String t=GenerateFeaturesFile.Hetero.generateBKStringPermute(prefix+"vgchartz.csv",
							prefix+"dbpedia.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}

				public static void writeBKSetCover(int num)throws IOException{
					
					int dup=num;
					int nondup=num;
					String t=GenerateFeaturesFile.Hetero.generateBKStringSetCover(prefix+"vgchartz.csv",
							prefix+"dbpedia.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}
				
			}
			public static class PR_experiments{
				static String prefix="/host/heteroDatasets/icde_experiments/PR/";
				
				
				
				
				public static void writeBKUnsup()throws IOException{
					
					int dup=50;
					int nondup=50;
					String t=GenerateFeaturesFile.Hetero.generateBKStringPermute(prefix+"restaurant1.csv",
							prefix+"restaurant2.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}
			
				public static void writeBKSetCover(int num)throws IOException{
					
					int dup=num;
					int nondup=num;
					
					
					String t=GenerateFeaturesFile.Hetero.generateBKStringSetCover(prefix+"restaurant1.csv",
							prefix+"restaurant2.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}
				
			}
			public static class Persons_experiments{
				static String prefix="/host/heteroDatasets/icde_experiments/Persons/";
				
				
				
				
				public static void writeBKUnsup()throws IOException{
					
					int dup=250;
					int nondup=250;
					String t=GenerateFeaturesFile.Hetero.generateBKStringPermute(prefix+"person1.csv",
							prefix+"person2.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}
			
				public static void writeBKSetCover(int num)throws IOException{
					
					int dup=num;
					int nondup=num;
					
					
					String t=GenerateFeaturesFile.Hetero.generateBKStringSetCover(prefix+"person1.csv",
							prefix+"person2.csv",prefix+"sortedScores/TF",prefix+"schema_dumas",dup,nondup);
					System.out.println(t);
					PrintWriter out=new PrintWriter(new File(prefix+"BK"));
					out.println(t);
					out.close();
				}
				
			}
			//used by getFeatureWeights
			private static void concatenate(ArrayList<Integer> d,int[] weight){
				for(int i=0; i<weight.length; i++)
					d.add(weight[i]);
					
			}
			
			//used by svm getFeatureWeights
			private static void concatenateDoubleInt(ArrayList<Double> d,int[] weight){
				for(int i=0; i<weight.length; i++)
					d.add((double)weight[i]);
					
			}
			
			//used by svm getFeatureWeights
			private static void concatenateDoubleDouble(ArrayList<Double> d,double[] weight){
				for(int i=0; i<weight.length; i++)
					d.add(weight[i]);
					
			}
			
		
}
