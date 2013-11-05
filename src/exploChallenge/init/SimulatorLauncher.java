package exploChallenge.init;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
	String policyToUse;
	String dataset;
	ArrayList<String> params;
	
	@SuppressWarnings("unchecked")
	public SimulatorLauncher(String policyToUse, ArrayList<String> params, String dataset, int numFeaturesUser,int numFeaturesArticle) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.policyToUse=policyToUse;
		this.dataset=dataset;
		this.params=params;
		Class<?> c = Class.forName("myPolicy."+this.policyToUse);
		long t = System.currentTimeMillis();
		GenericLogLineReader reader = null;
		try {
			reader = new GenericLogLineReader(dataset, numFeaturesUser, numFeaturesArticle);
		} catch (Exception e) {
			throw new FileNotFoundException();
		}		
		
		
		LogLineGenerator<GenericVisitor, GenericAction, Boolean> generator;
		generator = new FromFileLogLineGenerator<GenericVisitor, GenericAction, Boolean>(
				reader);

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
		EvaluationPolicy<GenericVisitor, GenericAction, Boolean> evalPolicy;
		String filename=policyToUse+":"+dataset+"("+uniqueCurrentTimeMS()+").csv";
		evalPolicy = new MyEvaluationPolicy<GenericVisitor, GenericAction>(filename);

		Evaluator<GenericVisitor, GenericAction, Boolean> eval;
		eval = new Evaluator<GenericVisitor, GenericAction, Boolean>(generator,evalPolicy, policy);

		eval.runEvaluation();
		System.out.println(this.policyToUse+": "+(System.currentTimeMillis() - t) +" ms");
		
		((MyEvaluationPolicy<GenericVisitor, GenericAction>) evalPolicy).closeCsv();
	}
	
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
