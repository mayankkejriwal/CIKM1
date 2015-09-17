package aaai;

import general.CSVParser;
import general.Parameters;

import java.io.*;
import java.util.*;

public class generateBK {

	public static void main(String[] args)throws IOException{
		
		attempt1_PR(SN.prefix+SN.db1,SN.prefix+SN.sn_file1);
	}
	
	private static void attempt1_PR(String input, String output)throws IOException{
		Scanner in=new Scanner(new FileReader(input));
		PrintWriter out=new PrintWriter(new File(output));
		int count=0;
		while(in.hasNextLine()){
			String line=in.nextLine();
			String[] f=new String[2];
			// f[0]=(new CSVParser()).parseLine(line)[1].toLowerCase();
			f[0]=(new CSVParser()).parseLine(line)[4].toLowerCase();
			 f[1]=(new CSVParser()).parseLine(line)[5].toLowerCase();
			 for(String f1:f)
				 if(!f1.equals("null")){
					 String[] tokens=f1.split(Parameters.splitstring);
			//ArrayList<String> b=new ArrayList<String>();
					 for(String a:tokens)
						 out.println(a+"\t"+count);
				}
			count++;
			
		}
		out.close();
		in.close();
	}

	//just initials
	private static void attempt1_game(String input, String output, int field)throws IOException{
		Scanner in=new Scanner(new FileReader(input));
		PrintWriter out=new PrintWriter(new File(output));
		int count=0;
		while(in.hasNextLine()){
			String line=in.nextLine();
			String name=(new CSVParser()).parseLine(line)[field].toLowerCase();
			String[] tokens=name.split(Parameters.splitstring);
			//ArrayList<String> b=new ArrayList<String>();
			for(String a:tokens){
				out.println(a+"\t"+count);
			}
			
			count++;
			
		}
		out.close();
		in.close();
	}

	private static void attempt1_persons(String input, String output)throws IOException{
		Scanner in=new Scanner(new FileReader(input));
		PrintWriter out=new PrintWriter(new File(output));
		int count=0;
		while(in.hasNextLine()){
			String line=in.nextLine();
			String[] f=new String[3];
			 f[0]=(new CSVParser()).parseLine(line)[1].toLowerCase();
			f[1]=(new CSVParser()).parseLine(line)[2].toLowerCase();
			 f[2]=(new CSVParser()).parseLine(line)[3].toLowerCase();
			 for(String f1:f)
				 if(!f1.equals("null")){
					 String[] tokens=f1.split(Parameters.splitstring);
			//ArrayList<String> b=new ArrayList<String>();
					 for(String a:tokens)
						 out.println(a+"\t"+count);
				}
			count++;
			
		}
		out.close();
		in.close();
	}
	
}
