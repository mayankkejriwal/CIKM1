package features;
import general.CSVParser;
import general.Parameters;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.codec.language.Caverphone1;
import org.apache.commons.codec.language.Caverphone2;
import org.apache.commons.codec.language.ColognePhonetic;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.MatchRatingApproachEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.Nysiis;
import org.apache.commons.codec.language.RefinedSoundex;
import org.apache.commons.codec.language.Soundex;




public class FeaturizeToken {

	static String[] vals={"soundex","caverphone1","caverphone2","colognephonetic","doublemetaphone"
			,"matchrating","metaphone","nysiis","refinedsoundex"};
	
	//compute normalized term frequency of two records
	public static double TF(String record1, String record2){
		double score=0.0;
		HashMap<String,Double> one=calculateTF(record1);
		HashMap<String,Double> two=calculateTF(record2);
		for(String key:one.keySet())
			if(two.containsKey(key))
				score+=one.get(key)*two.get(key);
		
		return score;
		
		
		
	}
	
	//compute Jaccard (between 0 and 1) of two records
	public static double Jaccard(String record1, String record2){
		String[] d1=record1.split(Parameters.splitstring);
		String[] d2=record2.split(Parameters.splitstring);
		HashSet<String> u1=new HashSet<String>();
		HashSet<String> u2=new HashSet<String>();
		for(String d:d1)
			u1.add(d);
		for(String d:d2)
			u2.add(d);
		HashSet<String> union=new HashSet<String>();
		HashSet<String> intersect=new HashSet<String>();
		for(String a:u1)
			if(u2.contains(a))
				intersect.add(a);
			else
				union.add(a);
			
		
			return 1.0*intersect.size()/(union.size()+u2.size());
	}
	
	//compute dice coefficient (between 0 and 1) of two records
	public static double Dice(String record1, String record2){
		String[] d1=record1.split(Parameters.splitstring);
		String[] d2=record2.split(Parameters.splitstring);
		HashSet<String> u1=new HashSet<String>();
		HashSet<String> u2=new HashSet<String>();
		for(String d:d1)
			u1.add(d);
		for(String d:d2)
			u2.add(d);
		//HashSet<String> union=new HashSet<String>();
		HashSet<String> intersect=new HashSet<String>();
		for(String a:u1)
			if(u2.contains(a))
				intersect.add(a);
					
		
			return 2.0*intersect.size()/(u1.size()+u2.size());
	}
	
	
	//average the input scores
	public static double computeComposite(double...a){
		double comp=0.0;
		for(double a1:a)
			comp+=a1;
		return comp/a.length;
	}
	
	//main helper for TF function
	private static HashMap<String,Double> calculateTF(String tuple){
		
		String[] words=null;
		try {
			words = (new CSVParser()).parseLine(tuple);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String,Integer> temp=new HashMap<String,Integer>();
		for(int j=0; j<words.length; j++){
			String[] tokens=words[j].split(Parameters.splitstring);
			for(int k=0; k<tokens.length; k++){
				tokens[k]=tokens[k].trim();
				if(tokens[k].length()==0)
					continue;
				if(temp.containsKey(tokens[k]))
					temp.put(tokens[k],temp.get(tokens[k])+1);
				else
					temp.put(tokens[k], 1);
			}
		}
		return normalize(temp);
}
	
	//normalize integer map
	private static HashMap<String,Double> normalize(HashMap<String,Integer> a){
		HashMap<String,Double> res=new HashMap<String,Double>();
		
		int sum=0;
		for(String key:a.keySet())
			sum+=a.get(key);
		
		for(String key:a.keySet())
			res.put(key,a.get(key)*1.0/sum);
		
		return res;
	}
	
	//return token with its soundex encoding 
	public static HashSet<String> featurize(String token){
		HashSet<String> result=new HashSet<String>();
		result.add(token);
		
		String t=phoneticEncode(token,"soundex");
		if(t!=null)
			result.add(t);
		
		
		
		return result;
	}
	
	//convert token to specified phonetic encoding
	public static String phoneticEncode(String token, String type){
		try{
			if(type.equals("soundex")){
				return new Soundex().encode(token);
			}
			else if(type.equals("caverphone1")){
				Caverphone1 d=new Caverphone1();
				return d.encode(token);
			}
			else if(type.equals("caverphone2")){
				Caverphone2 d=new Caverphone2();
				return d.encode(token);
			}
			else if(type.equals("colognephonetic")){
				ColognePhonetic d=new ColognePhonetic();
				return d.encode(token);
			}
			else if(type.equals("doublemetaphone")){
				DoubleMetaphone d=new DoubleMetaphone();
				return d.encode(token);
			}
			else if(type.equals("matchrating")){
				MatchRatingApproachEncoder d=new MatchRatingApproachEncoder();
				return d.encode(token);
					
			}
			else if(type.equals("metaphone")){
				Metaphone d=new Metaphone();
				return d.encode(token);
			}
			else if(type.equals("nysiis")){
				Nysiis d=new Nysiis();
				return d.encode(token);
			}
			else if(type.equals("refinedsoundex")){
				RefinedSoundex d=new RefinedSoundex();
				return d.encode(token);
			}
		}
		catch(Exception e){
			return null;
		}
		return null;
	}
	
	/*
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}*/
}
