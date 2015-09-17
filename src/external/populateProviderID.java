package external;

import features.FeaturizeToken;
import general.CSVParser;

import java.io.*;
import java.util.*;

public class populateProviderID {
	static String prefix="/host/heteroDatasets/hospitals2/";
	static String scoreFile=prefix+"sortedScores/C";
	static int top=Integer.MAX_VALUE; 
	static int db1=5;
	static int db2=1;
	static boolean populateFirst=true;
	
	public static void main(String[] args)throws IOException{
		//postProcessJoin();
		//addScore();
		join();
		
		
		
	}
	
	public static void addScore()throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+"sortedScores/C"));
		ArrayList<String> scores=new ArrayList<String>();
		while(in.hasNextLine()){
			String a=in.nextLine().split("\t")[1];
			scores.add(a);
		}
		in.close();
		
		in=new Scanner(new FileReader(prefix+"populated.csv"));
		PrintWriter out=new PrintWriter(new File(prefix+"populated1.csv"));
		int count=0;
		while(in.hasNextLine()){
			out.println(in.nextLine()+","+scores.get(count));
			count++;
		}
		in.close();
		out.close();
	}
	
	public static void populateColumn()throws IOException{
		HashMap<Integer,String> index1=hospitals.buildIndex(prefix+"Medaxiom_short.csv");
		HashMap<Integer,String> index2=hospitals.buildIndex(prefix+"NationalListofHospitalIDs_short.csv");
		PrintWriter out=new PrintWriter(new File(prefix+"populated.csv"));
		ArrayList<String> scores=scores();
		for(String a: scores){
			String[] line=a.split(" ");
			int a1=Integer.parseInt(line[0]);
			int a2=Integer.parseInt(line[1]);
			String[] b1=new CSVParser().parseLine(index1.get(a1));
			String[] b2=new CSVParser().parseLine(index2.get(a2));
			if(populateFirst){
				b1[db1]=b2[db2];
				
				
				out.println(composeCSVString(b1));
			}else{
				b2[db2]=b1[db1];
				
				out.println(composeCSVString(b2));
			}
			
		}
		out.close();
	}
	
	public static String composeCSVString(String[] b1){
		String res="";
		for(String t:b1)
			if(t.contains(","))
				res+=("\""+t+"\",");
			else
				res+=(t+",");
		return res.substring(0,res.length()-1);
	}
	
	public static ArrayList<String> scores()throws IOException{
		ArrayList<String> m=new ArrayList<String>();
		Scanner in=new Scanner(new FileReader(scoreFile));
		int count=0;
		while(count<top && in.hasNextLine()){
			m.add(in.nextLine().split("\t")[0]);
			count++;
		}
		return m;
	}
	
	public static void join()throws IOException{
		HashMap<String,HashSet<String>> index1=hospitals.buildIntColumnIndex(prefix+"populated1.csv",db1);
		HashMap<String,HashSet<String>> index2=hospitals.buildIntColumnIndex(prefix+"NationalListofHospitalIDs_short.csv",db2);
		PrintWriter out=new PrintWriter(new File(prefix+"join.csv"));
		for(String a:index2.keySet()){
			if(!index1.containsKey(a))
				continue;
			for(String a1:index2.get(a))
				for(String a2:index1.get(a))
					out.println(a2+","+a1);
		}
		out.close();
	}
	
	public static void postProcessJoin()throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+"join.csv"));
		PrintWriter out=new PrintWriter(new File(prefix+"processedJoin.csv"));
		while(in.hasNextLine()){
			String line=in.nextLine();
			String[] a=(new CSVParser()).parseLine(line);
			double b=FeaturizeToken.Jaccard(a[10],a[13]);
			if(b>0.01)
				out.println(line);
		}
		in.close();
		out.close();
	}
}
