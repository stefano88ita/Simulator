package exploChallenge.init;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicLong;

import exploChallenge.eval.EvaluationPolicy;
import exploChallenge.eval.Evaluator;
import exploChallenge.eval.MyEvaluationPolicy;
import exploChallenge.logs.FromFileLogLineGenerator;
import exploChallenge.logs.LogLineGenerator;
import exploChallenge.logs.GenericArticle;
import exploChallenge.logs.GenericLogLineReader;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;


public class SimulatorLauncher {
	String policyToUse;
	String dataset;
	
	@SuppressWarnings("unchecked")
	public SimulatorLauncher(String policyToUse, String dataset, int numFeaturesUser,int numFeaturesArticle) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.policyToUse=policyToUse;
		this.dataset=dataset;
		Class<?> c = Class.forName("myPolicy."+this.policyToUse);
		long t = System.currentTimeMillis();
		GenericLogLineReader reader = null;
		try {
			reader = new GenericLogLineReader(dataset, numFeaturesUser, numFeaturesArticle);
		} catch (Exception e) {
			throw new FileNotFoundException();
		}		
		
		LogLineGenerator<GenericVisitor, GenericArticle, Boolean> generator;
		generator = new FromFileLogLineGenerator<GenericVisitor, GenericArticle, Boolean>(
				reader);

		ContextualBanditPolicy<GenericVisitor, GenericArticle, Boolean> policy;
		policy = (ContextualBanditPolicy<GenericVisitor, GenericArticle, Boolean>) c.newInstance();

		EvaluationPolicy<GenericVisitor, GenericArticle, Boolean> evalPolicy;
		String filename=policyToUse+":"+dataset+"("+uniqueCurrentTimeMS()+").csv";
		evalPolicy = new MyEvaluationPolicy<GenericVisitor, GenericArticle>(filename);

		Evaluator<GenericVisitor, GenericArticle, Boolean> eval;
		eval = new Evaluator<GenericVisitor, GenericArticle, Boolean>(generator,evalPolicy, policy);

		eval.runEvaluation();
		System.out.println(this.policyToUse+": "+(System.currentTimeMillis() - t) +" ms");
		
		((MyEvaluationPolicy<GenericVisitor, GenericArticle>) evalPolicy).closeCsv();
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
