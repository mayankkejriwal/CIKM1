package svm;

import java.io.*;
import java.util.*;

import features.GenerateFeaturesFile;

public class SVM {

	//converts gold standard file to libsvm format
	public static void generateFromGold(String records,String gold, String output)throws IOException{
		Scanner in=new Scanner(new FileReader(records));
		ArrayList<String> recs=new ArrayList<String>();
		while(in.hasNextLine())
			recs.add(in.nextLine());
		
		in.close();
		
		in=new Scanner(new FileReader(gold));
		PrintWriter out=new PrintWriter(new File(output));
		while(in.hasNextLine()){
			String[] d=in.nextLine().split(" ");
			int index1=Integer.parseInt(d[0]);
			int index2=Integer.parseInt(d[1]);
			ArrayList<Integer> feat=GenerateFeaturesFile.Homo.getFeatureWeightsHomo(recs.get(index1),recs.get(index2));
			String t=new String("1");
			
			for(int j=0; j<feat.size(); j++)
				t+=(" "+(j+1)+":"+feat.get(j));
			out.println(t);
		}
		in.close();
		out.close();
	}
	
	//use this for testing an svm model on the gold standard alone
	public static void main(String[] args)throws IOException{
		String prefix="/host/heteroDatasets/cikm_experiments/restaurant/";
		int dup=100;
		int nondup=100;
		String records=prefix+"restaurant.csv";
		String gold=prefix+"GoldStandard.csv";
		//generateFromGold(records,gold,prefix+"gold_svm");
		String supervision=prefix+"sortedScores/C";
		String[] argv={prefix+"gold_svm",prefix+"svm_model_sup",prefix+"gold_output"};
		svm_predict.entry(argv);
		}
}
