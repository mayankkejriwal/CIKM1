package features;

import general.Parameters;

import java.io.IOException;
import java.util.*;

public class SetCovering {

	 public ArrayList<Integer> codes;
	 public ArrayList<Integer> attributes1;
	 public ArrayList<Integer> attributes2;
	 
	 public  ArrayList<ArrayList<Integer>> codesDNF;
	 public  ArrayList<ArrayList<Integer>> attributes1DNF;
	 public  ArrayList<ArrayList<Integer>> attributes2DNF; 
	
	 int num_atts1;
	 int num_atts2;
	 
	 ArrayList<ArrayList<Integer>> posFeatVecs;
	 ArrayList<ArrayList<Integer>> negFeatVecs;
	
	 
	 
	public SetCovering(ArrayList<ArrayList<Integer>> posFeats, ArrayList<ArrayList<Integer>> negFeats,int att1, int att2){
		num_atts1=att1;
		num_atts2=att2;
		posFeatVecs=posFeats;
		negFeatVecs=negFeats;
		if(!Parameters.DNF){
			codes=new ArrayList<Integer>();
			attributes1=new ArrayList<Integer>();
			attributes2=new ArrayList<Integer>();
			setCoveringDisjunct();
		}else{
			codesDNF=new ArrayList<ArrayList<Integer>>();
			attributes1DNF=new ArrayList<ArrayList<Integer>>();
			attributes2DNF=new ArrayList<ArrayList<Integer>>();
		}
	}
	
	private void setCoveringDisjunct(){
		HashSet<Integer> res=new HashSet<Integer>();
		HashMap<Integer,HashSet<Integer>> revMap=buildReverseMap(true); //for pos
		HashMap<Integer,HashSet<Integer>> negMap=buildReverseMap(false);
		HashMap<Double,Integer> scoresIndex=buildScores(revMap,negMap);
		
		while(true){
			ArrayList<Double> sc=new ArrayList<Double>(scoresIndex.keySet());
			if(sc.size()==0)
				break;
			Collections.sort(sc);
			int last=sc.size()-1;
			int maxIndex=scoresIndex.get(sc.get(last));
			res.add(maxIndex);
			revMap=removeCovered(maxIndex,revMap);
			if(revMap.keySet().size()==0)
				break;
			else
				scoresIndex=buildScores(revMap,negMap);
		}
		if(res.size()==0){
			System.out.println("PosNegTheta too high! No predicate chosen! Terminating program...");
			System.exit(-1);
		}
		for(int res1:res)
			addCode(res1);
	}
	
	private HashMap<Double,Integer> buildScores(HashMap<Integer,HashSet<Integer>> revMap,HashMap<Integer,HashSet<Integer>> negMap){
		HashMap<Double,Integer> res=new HashMap<Double,Integer>();
		int posCount=countDistinctElements(revMap);
		int negCount=negFeatVecs.size();
		for(int i:revMap.keySet()){
			double score=revMap.get(i).size()*1.0/posCount;
			if(negMap.containsKey(i))
				score=score-(negMap.get(i).size()*1.0/negCount);
			if(score<Parameters.posNegTheta)
				continue;
			
			res.put(score,i);
			
				
		}
		
		return res;
	}
	
	private int countDistinctElements(HashMap<Integer,HashSet<Integer>> revMap){
		HashSet<Integer> m=new HashSet<Integer>();
		for(int i:revMap.keySet()){
			for(int j: revMap.get(i))
				m.add(j);
		}
		return m.size();
	}
	
	
	
	@SuppressWarnings("unused")
	private int retMax(HashMap<Integer,HashSet<Integer>> revMap){
		double max=Integer.MIN_VALUE;
		int index=-1;
		for(int i:revMap.keySet()){
			if(//sumHashSetScores(revMap.get(i))*1.0/
					revMap.get(i).size()>max){
				max=revMap.get(i).size();
				index=i;
			}
		}
		return index;
	}
	
	
	private HashMap<Integer,HashSet<Integer>> removeCovered(int q, HashMap<Integer,HashSet<Integer>> revMap){
		HashMap<Integer,HashSet<Integer>> res=new HashMap<Integer,HashSet<Integer>>();
		HashSet<Integer> check=revMap.get(q);
		
		for(int i:revMap.keySet()){
			HashSet<Integer> d=new HashSet<Integer>();
			for(int j:revMap.get(i))
				if(!check.contains(j))
					d.add(j);
			if(d.size()>0)
				res.put(i, d);
		}
		
		return res;
	}
	
	//reverse featVecs so that predicate references vectors it covers
	private HashMap<Integer,HashSet<Integer>> buildReverseMap(boolean pos){
		HashMap<Integer,HashSet<Integer>> res=new HashMap<Integer,HashSet<Integer>>();
		ArrayList<ArrayList<Integer>> featVecs=posFeatVecs;
		if(!pos)
			featVecs=negFeatVecs;
		
		
		for(int i=0; i<featVecs.size(); i++){
			for(int j=0; j<featVecs.get(i).size(); j++){
				if(featVecs.get(i).get(j)==1){
					if(!res.containsKey(j))
						res.put(j, new HashSet<Integer>());
				
					res.get(j).add(i);
				}
			}
		}
		
		return res;
	}
	
	public ArrayList<String> codes()throws IOException{
		ArrayList<String> results=new ArrayList<String>();
		for(int i=0; i<codes.size(); i++){
			results.add(codes.get(i)+" "+attributes1.get(i)+" "+attributes2.get(i));
		}
		
		
		return results;
		
	}
	
	private void addCode(int q){
		codes.add(q/(num_atts2*num_atts1)+1);
		int[] atts=calcDBIndices(q);
		attributes1.add(atts[0]);
		attributes2.add(atts[1]);
	}
	
	//returns indices of db1 and db2 corresponding to pred index q in int[]
	private int[] calcDBIndices(int q){
		int[] res=new int[2];
		int FeatureBlockPos=q%(num_atts1*num_atts2);
		res[1]=FeatureBlockPos%num_atts2;
		res[0]=FeatureBlockPos/num_atts2;
		
		
		return res;
	}
}
