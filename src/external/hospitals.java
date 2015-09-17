package external;


import java.io.*;
import java.util.*;

import features.FeaturizeToken;
import general.CSVParser;

public class hospitals {

	
	static String prefix="/host/heteroDatasets/hospital3/";
	
	public static void main(String[] args)throws IOException{
		preprocessBlankFile();
		//findLastIndexInCFile();
		//getCFromTF();
		
		
	}
	
	public static void preprocessBlankFile()throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+"DB2.csv"));
		PrintWriter out=new PrintWriter(new File(prefix+"DB21.csv"));
		while(in.hasNextLine()){
			String line=in.nextLine();
			//String line=in.nextLine().trim();
			if(line.trim().length()>0)
				out.println(line);
		}
		in.close();
		out.close();
	}
	
	//we already have the TF file in sortedScores. Now we compute the composite distance
	//after this, make sure to call the appropriate function in post-process.
	public static void getCFromTF()throws IOException{
		HashMap<Integer,String> index1=buildIndex(prefix+"DB1.csv");
		HashMap<Integer,String> index2=buildIndex(prefix+"DB2.csv");
		
		Scanner in=new Scanner(new FileReader(prefix+"sortedScores/TF"));
		PrintWriter out=new PrintWriter(new File(prefix+"C"));
		while(in.hasNextLine()){
			String[] q=in.nextLine().split("\t")[0].split(" ");
			int a=Integer.parseInt(q[0]);
			int b=Integer.parseInt(q[1]);
			double dice=FeaturizeToken.Dice(index1.get(a), index2.get(b));
			double jaccard=FeaturizeToken.Jaccard(index1.get(a), index2.get(b));
			double tf=FeaturizeToken.TF(index1.get(a), index2.get(b));
			double composite=FeaturizeToken.computeComposite(dice,jaccard,tf);
			
				out.println(q[0]+" "+q[1]+"\t"+composite+" "+"C");
		}
		out.close();
		in.close();
	}
	
	public static HashMap<Integer,String> buildIndex(String file)throws IOException{
		Scanner in1=new Scanner(new FileReader(file));
		HashMap<Integer,String> result=new HashMap<Integer,String>();
		int count=0;
		while(in1.hasNextLine()){
			result.put(count, in1.nextLine());
			count++;
		}
		in1.close();
		return result;
	}
	
	public static HashMap<String,HashSet<String>> buildIntColumnIndex(String file, int col)throws IOException{
		Scanner in1=new Scanner(new FileReader(file));
		HashMap<String,HashSet<String>> result=new HashMap<String,HashSet<String>>();
		
		while(in1.hasNextLine()){
			String line=in1.nextLine();
			String q=(new CSVParser().parseLine(line)[col]);
			if(!result.containsKey(q))
				result.put(q, new HashSet<String>());
			result.get(q).add(line);
			
		}
		in1.close();
		return result;
	}
	
	public static void preprocessHospitals2()throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+"Medaxiom_short.csv"));
		PrintWriter out=new PrintWriter(new File(prefix+"DB1.csv"));
		while(in.hasNextLine()){
			String[] line=(new CSVParser()).parseLine(in.nextLine());
			out.println(composeCSVStringAndRemoveCols(line,0));
		}
		in.close();
		out.close();
		
		in=new Scanner(new FileReader(prefix+"NationalListofHospitalIDs_short.csv"));
		out=new PrintWriter(new File(prefix+"DB2.csv"));
		while(in.hasNextLine()){
			String[] line=(new CSVParser()).parseLine(in.nextLine());
			out.println(composeCSVStringAndRemoveCols(line,0,1));
		}
		in.close();
		out.close();
	}

	public static String composeCSVStringAndRemoveCols(String[] b1, int...col){
		String res="";
		HashSet<Integer> c=new HashSet<Integer>();
		for(int i:col)
			c.add(i);
		for(int i=0; i<b1.length; i++){
			if(c.contains(i))
				continue;
			String t=b1[i];
			if(t.contains(","))
				res+=("\""+t+"\",");
			else
				res+=(t+",");
		}
		return res.substring(0,res.length()-1);
	}
	
	public static void findLastIndexInCFile()throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+"sortedScores/oldC"));
		PrintWriter out=new PrintWriter(new File(prefix+"sortedScores/C"));
		HashSet<Integer> a=new HashSet<Integer>(2000);
		int count=0;
		while(in.hasNextLine()){
			String line=in.nextLine();
			int b=Integer.parseInt(line.split("\t")[0].split(" ")[0]);
			if(!a.contains(b)){
				a.add(b);
				out.println(line);
			}
			count++;
			if(a.size()==1040)
				break;
		}
		in.close();
		out.close();
		System.out.println(count);
	}
}
