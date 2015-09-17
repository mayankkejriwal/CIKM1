package external;


import general.CSVParser;
import java.io.*;
import java.util.*;

import utils.AttributeSim;

public class custom {

	static String prefix="/host/heteroDatasets/hospitals2/";
	static String scoreFile="sortedScores/oldC";
	static String file1="DB1.csv";
	static String file2="DB2.csv";
	static int db1=3;
	static int db2=0;
			
	static int k=5;
	
	public static void main(String[] args)throws IOException{
		printNewC();
	}
	
	//remember to run postprocess!
	public static void printNewC()throws IOException{
		HashMap<Integer, HashSet<Integer>> ds=populateIRDataStructure();
		HashMap<Integer,String> f1=hospitals.buildIndex(prefix+file1);
		HashMap<Integer,String> f2=hospitals.buildIndex(prefix+file2);
		PrintWriter out=new PrintWriter(prefix+"C");
		for(int i: ds.keySet()){
			String a=(new CSVParser()).parseLine(f1.get(i))[db1];
			for(int j: ds.get(i)){
				String b=(new CSVParser()).parseLine(f2.get(j))[db2];
				double d=AttributeSim.paddedBigramJaccard(a, b);
				out.println(i+" "+j+"\t"+d+" C");
			}
		}
		out.close();
		
	}
	
	
	
	private static HashMap<Integer, HashSet<Integer>> populateIRDataStructure()throws IOException{
		HashMap<Integer, HashSet<Integer>> a=new HashMap<Integer,HashSet<Integer>>(2000);
		Scanner in=new Scanner(new FileReader(prefix+scoreFile));
		while(in.hasNextLine()){
			String line=in.nextLine();
			int c=Integer.parseInt(line.split("\t")[0].split(" ")[0]);
			if(!a.containsKey(c))
				a.put(c, new HashSet<Integer>());
			if(a.get(c).size()<k)
				a.get(c).add(Integer.parseInt(line.split("\t")[0].split(" ")[1]));
		}
		in.close();
		System.out.println(a.keySet().size());
		
		return a;
	}
	
	
}
