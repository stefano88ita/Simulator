package exploChallenge.eval;

import exploChallenge.logs.LogLine;

//interface for evaluation policy
public interface EvaluationPolicy<Context, Action, Reward> {
 
	public void evaluate(LogLine<Context, Action, Reward> logLine,
			Action chosenAction);

	public double getResult();

	public void log();

}  
