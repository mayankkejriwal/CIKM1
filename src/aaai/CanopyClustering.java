package aaai;

import java.io.*;
import java.util.*;


public class CanopyClustering {

	static double thresh=0.074;
	static String prefix="/host/heteroDatasets/icde_experiments/PR/";
	static String TF_file="sortedScores/TF";
	static String gold_file="goldStandard";
	
	static int n2=764784;
	public static void main(String[] args)throws IOException{
		printPerformance();
	}
	
	public static void printPerformance()throws IOException{
		HashMap<Integer,HashSet<Integer>> index=populateIndicesFromScoreFile();
		HashSet<String> gold=importGold();
		int total=0;
		int correct=0;
		for(int i: index.keySet()){
			total+=index.get(i).size();
			for(int j: index.get(i))
				if(gold.contains(i+" "+j))
					correct++;
		}
		System.out.println("Total: "+total);
		System.out.println("Correct: "+correct);
		System.out.println("Gold: "+gold.size());
		System.out.println("thresh,RR,PC,PQ");
		System.out.println(thresh+","+(100.0-total*100.0/n2)+","+(correct*100.0/gold.size())+","+(correct*100.0/total));
		
		
	}
	
	public static HashSet<String> importGold()throws IOException{
		HashSet<String> res=new HashSet<String>();
		Scanner in=new Scanner(new FileReader(prefix+gold_file));
		while(in.hasNextLine()){
			res.add(in.nextLine());
		}
		in.close();
		return res;
	}
	
	private static HashMap<Integer,HashSet<Integer>> populateIndicesFromScoreFile()throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+TF_file));
		HashMap<Integer,HashSet<Integer>> index=new HashMap<Integer,HashSet<Integer>>();
		while(in.hasNextLine()){
			String line=in.nextLine();
			int i1=Integer.parseInt(line.split("\t")[0].split(" ")[0]);
			int i2=Integer.parseInt(line.split("\t")[0].split(" ")[1]);
			double score=Double.parseDouble(line.split("\t")[1]);
			if(score<thresh)
				continue;
			if(!index.containsKey(i1)){
				index.put(i1, new HashSet<Integer>());
				
			}
			index.get(i1).add(i2);
			
		}
		in.close();
		return index;
	}
	
	
}
