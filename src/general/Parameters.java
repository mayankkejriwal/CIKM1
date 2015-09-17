package general;


public class Parameters {
	
	public static String splitstring="://|\\[|\\]|\\(|\\)|: |\t| |,|:|/|-|_|\\. |\\.|\\&|\"|#|;|; ";
	public static int num_feats=28; 
	public static int svm_num_feats=38;
	public static int maxBucketPairs=Integer.MAX_VALUE;
	public static double recall=0.95; //epsilon: more is better, the less noisy the sample
	public static double eta=0.05; //less the better, less noisy the sample
	public static String[] forbiddenwords={"null"}; //interpreted as case insensitive
	public static boolean DNF=false;
	public static int maxpairs=6;	//try to keep all pairs parameters equal
	public static double posNegTheta=0.1;

}
