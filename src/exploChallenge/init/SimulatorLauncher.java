package exploChallenge.init;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicLong;

import exploChallenge.eval.EvaluationPolicy;
import exploChallenge.eval.Evaluator;
import exploChallenge.eval.MyEvaluationPolicy;
import exploChallenge.logs.FromFileLogLineGenerator;
import exploChallenge.logs.LogLineGenerator;
import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericLogLineReader;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;


public class SimulatorLauncher {
	//init
	String policyToUse;
	String dataset;
	ArrayList<String> params;
	
	@SuppressWarnings("unchecked")
	public SimulatorLauncher(String policyToUse, ArrayList<String> params, String dataset, int numFeaturesUser,int numFeaturesArticle) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		//this class parse the input params and call the right policy with right parameters, it transform method name in method invocation
		this.policyToUse=policyToUse;
		this.dataset=dataset;
		this.params=params;
		Class<?> c = Class.forName("myPolicy."+this.policyToUse);	//get the policy class by name
		long t = System.currentTimeMillis();						//get a unique id to output the result csv
		GenericLogLineReader reader = null;
		try {
			reader = new GenericLogLineReader(dataset, numFeaturesUser, numFeaturesArticle); //instantiation of a log line reader
		} catch (Exception e) {
			throw new FileNotFoundException();
		}		
		
		
		LogLineGenerator<GenericVisitor, GenericAction, Boolean> generator;
		generator = new FromFileLogLineGenerator<GenericVisitor, GenericAction, Boolean>(reader);	//this object read the dataset

		ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean> policy;
		if(params.size()>0){
			try {
				policy = (ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean>) c.getConstructor(params.getClass()).newInstance(params);
			} catch (Exception e) {
				System.out.println("Exception:"+e);
				policy = (ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean>) c.newInstance();
			}
		}else{
			policy = (ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean>) c.newInstance();
		}
		//policy is the class that predict the action to take
		EvaluationPolicy<GenericVisitor, GenericAction, Boolean> evalPolicy;
		String datasetName="";
		StringTokenizer stringTokenizer = new StringTokenizer(dataset,"/");
		while(stringTokenizer.hasMoreTokens()) datasetName = stringTokenizer.nextToken();
		//String filename=policyToUse+"--"+datasetName+"("+uniqueCurrentTimeMS()+").csv";
		if(!(new File("Results/"+datasetName).exists())){
			(new File("Results/"+datasetName)).mkdir();
		}
		String filename = datasetName+"/"+policyToUse+".csv";
		evalPolicy = new MyEvaluationPolicy<GenericVisitor, GenericAction>(filename);	//evalPolicy is the class that estimate the goodness of policy

		Evaluator<GenericVisitor, GenericAction, Boolean> eval;
		eval = new Evaluator<GenericVisitor, GenericAction, Boolean>(generator,evalPolicy, policy);	//evaluator class evaluate the policy using the eval policy

		eval.runEvaluation();
		System.out.println(this.policyToUse+": "+(System.currentTimeMillis() - t) +" ms");
		
		((MyEvaluationPolicy<GenericVisitor, GenericAction>) evalPolicy).closeCsv();
	}
	
	//this static method is for generate a unique timestamp in java
	private static final AtomicLong LAST_TIME_MS = new AtomicLong();
	public static long uniqueCurrentTimeMS() {
	    long now = System.currentTimeMillis();
	    while(true) {
	        long lastTime = LAST_TIME_MS.get();
	        if (lastTime >= now)
	            now = lastTime+1;
	        if (LAST_TIME_MS.compareAndSet(lastTime, now))
	            return now;
	    }
	}
	
	
}
