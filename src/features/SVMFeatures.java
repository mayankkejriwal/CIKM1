package features;

import com.wcohen.ss.Levenstein;

public class SVMFeatures {

	public static double[] AffineGap(String[] token1, String[] token2){
		if(token1.length!=token2.length)
			System.out.println("Length Error in SVMFeatures.AffineGap!");
		double[] res=new double[token1.length];
		for(int i=0; i<token1.length; i++){
			res[i]=(new com.wcohen.ss.AffineGap()).score(new com.wcohen.ss.AffineGap().prepare(token1[i]),
					new com.wcohen.ss.AffineGap().prepare(token2[i]));
			if(Double.isNaN(res[i]))
				res[i]=0;
		}
		
		return res;
	}
	
	public static double[] DirichletJS(String[] token1, String[] token2){
		if(token1.length!=token2.length)
			System.out.println("Length Error in SVMFeatures.DirichletJS!");
		double[] res=new double[token1.length];
		for(int i=0; i<token1.length; i++){
			res[i]=(new com.wcohen.ss.DirichletJS()).score(new com.wcohen.ss.DirichletJS().prepare(token1[i]),
					new com.wcohen.ss.DirichletJS().prepare(token2[i]));
			if(Double.isNaN(res[i]))
				res[i]=0;}
		
		return res;
	}
	
	public static double[] Jaro(String[] token1, String[] token2){
		if(token1.length!=token2.length)
			System.out.println("Length Error in SVMFeatures.Jaro!");
		double[] res=new double[token1.length];
		for(int i=0; i<token1.length; i++){
			res[i]=(new com.wcohen.ss.Jaro()).score(new com.wcohen.ss.Jaro().prepare(token1[i]),
					new com.wcohen.ss.Jaro().prepare(token2[i]));
			if(Double.isNaN(res[i]))
				res[i]=0;}
		
		return res;
	}
	
	public static double[] Jaccard(String[] token1, String[] token2){
		if(token1.length!=token2.length)
			System.out.println("Length Error in SVMFeatures.Jaccard!");
		double[] res=new double[token1.length];
		for(int i=0; i<token1.length; i++){
			res[i]=(new com.wcohen.ss.Jaccard()).score(new com.wcohen.ss.Jaccard().prepare(token1[i]),
					new com.wcohen.ss.Jaccard().prepare(token2[i]));
			if(Double.isNaN(res[i]))
				res[i]=0;}
		
		return res;
	}
	
	public static double[] JaroWinkler(String[] token1, String[] token2){
		if(token1.length!=token2.length)
			System.out.println("Length Error in SVMFeatures.JaroWinkler!");
		double[] res=new double[token1.length];
		for(int i=0; i<token1.length; i++){
			res[i]=(new com.wcohen.ss.JaroWinkler()).score(new com.wcohen.ss.JaroWinkler().prepare(token1[i]),
					new com.wcohen.ss.JaroWinkler().prepare(token2[i]));
			if(Double.isNaN(res[i]))
				res[i]=0;}
		
		return res;
	}
	
	public static double[] MongeElkan(String[] token1, String[] token2){
		if(token1.length!=token2.length)
			System.out.println("Length Error in SVMFeatures.MongeElkan!");
		double[] res=new double[token1.length];
		for(int i=0; i<token1.length; i++){
			res[i]=(new com.wcohen.ss.MongeElkan()).score(new com.wcohen.ss.MongeElkan().prepare(token1[i]),
					new com.wcohen.ss.MongeElkan().prepare(token2[i]));
			if(Double.isNaN(res[i]))
				res[i]=0;}
		
		return res;
	}
	
	public static double[] Levenstein(String[] token1, String[] token2){
		if(token1.length!=token2.length)
			System.out.println("Length Error in SVMFeatures.Levenstein!");
		double[] res=new double[token1.length];
		for(int i=0; i<token1.length; i++){
			res[i]=(new Levenstein()).score(new Levenstein().prepare(token1[i]),
					new Levenstein().prepare(token2[i]));
			if(Double.isNaN(res[i]))
				res[i]=0;}
		
		return res;
	}
	
	public static double[] SmithWaterman(String[] token1, String[] token2){
		if(token1.length!=token2.length)
			System.out.println("Length Error in SVMFeatures.SmithWaterman!");
		double[] res=new double[token1.length];
		for(int i=0; i<token1.length; i++){
			res[i]=(new com.wcohen.ss.SmithWaterman()).score(new com.wcohen.ss.SmithWaterman().prepare(token1[i]),
					new com.wcohen.ss.SmithWaterman().prepare(token2[i]));
			if(Double.isNaN(res[i]))
				res[i]=0;}
		
		return res;
	}
	
	public static double[] NeedlemanWunsch(String[] token1, String[] token2){
		if(token1.length!=token2.length)
			System.out.println("Length Error in SVMFeatures.NeedlemanWunsch!");
		double[] res=new double[token1.length];
		for(int i=0; i<token1.length; i++){
			res[i]=(new com.wcohen.ss.NeedlemanWunsch()).score(new com.wcohen.ss.NeedlemanWunsch().prepare(token1[i]),
					new com.wcohen.ss.NeedlemanWunsch().prepare(token2[i]));
			if(Double.isNaN(res[i]))
				res[i]=0;}
		
		return res;
	}
	
	public static double[] JelinekMercerJS(String[] token1, String[] token2){
		if(token1.length!=token2.length)
			System.out.println("Length Error in SVMFeatures.JelinekMercerJS!");
		double[] res=new double[token1.length];
		for(int i=0; i<token1.length; i++){
			res[i]=(new com.wcohen.ss.JelinekMercerJS()).score(new com.wcohen.ss.JelinekMercerJS().prepare(token1[i]),
					new com.wcohen.ss.JelinekMercerJS().prepare(token2[i]));
			if(Double.isNaN(res[i]))
				res[i]=0;}
		
		return res;
	}
}
