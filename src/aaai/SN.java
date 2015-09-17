package aaai;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class SN {
	
	static int w=200;
	static String prefix="/host/heteroDatasets/icde_experiments/PR/";
	static String sn_file1="sortedScores/SN1";
	static String sn_file2="sortedScores/SN2";
	static String gold_file="goldStandard";
	
	static String db1="restaurant1.csv";
	static String db2="restaurant2.csv";
	
	static int n2=764784;
	
	public static void main(String[] args)throws IOException{
		printPerformance();
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
	
	public static void printPerformance()throws IOException{
		
		HashMap<Integer,HashSet<Integer>> index=populateIndicesFromSNFile();
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
		System.out.println("w,RR,PC,PQ");
		System.out.println(w+","+(100.0-total*100.0/n2)+","+(correct*100.0/gold.size())+","+(correct*100.0/total));
		
	}
	
	public static HashMap<Integer,HashSet<Integer>> populateIndicesFromSNFile()throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+sn_file1));
		HashMap<Integer,HashSet<Integer>> index=new HashMap<Integer,HashSet<Integer>>();
		
		HashMap<String,HashSet<Integer>> a=new HashMap<String,HashSet<Integer>>();
		HashMap<String,HashSet<Integer>> b=new HashMap<String,HashSet<Integer>>();
		while(in.hasNextLine()){
			String[] p=in.nextLine().split("\t");
			if(!a.containsKey(p[0]))
				a.put(p[0], new HashSet<Integer>());
			a.get(p[0]).add(Integer.parseInt(p[1]));
		}
		in.close();
		in=new Scanner(new FileReader(prefix+sn_file2));
		while(in.hasNextLine()){
			String[] p=in.nextLine().split("\t");
			if(!b.containsKey(p[0]))
				b.put(p[0], new HashSet<Integer>());
			b.get(p[0]).add(Integer.parseInt(p[1]));
		}
		in.close();
	
		for(String key:a.keySet()){
			if(b.containsKey(key)){
				
				ArrayList<Integer> d1=new ArrayList<Integer>(a.get(key));
				ArrayList<Integer> d2=new ArrayList<Integer>(b.get(key));
				populateIndex(d1,d2,index);
			/*if(key.equals("'imnrsy")){
					System.out.println(d1+" "+d2);
					System.out.println(index.get(14502));
				}*/
				
				
			}
				
		}
		
		return index;
	}
	
	private static void populateIndex(ArrayList<Integer> a, ArrayList<Integer> b, HashMap<Integer,HashSet<Integer>> c){
		
		if(a.size()<=w&&b.size()<=w)
			{combine(a,b,c); return;}
		else if(a.size()<=w){
			ArrayList<Integer> q=new ArrayList<Integer>();
			for(int i=0; i<a.size(); i++)
				q.add(b.get(i));
			combine(a,q,c); return;
		}
		else if(b.size()<=w){
			ArrayList<Integer> q=new ArrayList<Integer>();
			for(int i=0; i<b.size(); i++)
				q.add(a.get(i));
			combine(q,b,c); return;
		}
		
		ArrayList<Integer> small=a;
		ArrayList<Integer> big=b;
		boolean reverse=false;
		if(a.size()>b.size())
		{
			small=b;
			big=a;
			reverse=true;
		}
		
		for(int i=0; i<(small.size()-w)+1; i++){
			ArrayList<Integer> q1=new ArrayList<Integer>(w);
			ArrayList<Integer> q2=new ArrayList<Integer>(w);
			for(int k=0; k<w; k++){
				q1.add(small.get(i+k));
				q2.add(big.get(i+k));
			}
			if(reverse)
				combine(q2,q1,c);
			else
				combine(q1,q2,c);
			
		}
	}
	
	private static void combine(ArrayList<Integer> a, ArrayList<Integer> b, HashMap<Integer,HashSet<Integer>> c){
		for(int i:a){
			if(!c.containsKey(i))
				c.put(i, new HashSet<Integer>());
			for(int j:b)
				c.get(i).add(j);
		}
	}
}
