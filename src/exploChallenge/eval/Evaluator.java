package exploChallenge.eval;

import exploChallenge.logs.LogLine;
import exploChallenge.logs.LogLineGenerator;
import exploChallenge.policies.ContextualBanditPolicy;

public class Evaluator<Context, Action, Reward> {
	//init
	private LogLineGenerator<Context, Action, Reward> generator;
	private EvaluationPolicy<Context, Action, Reward> evalPolicy;
	private ContextualBanditPolicy<Context, Action, Reward> policy;
	private int linesToSkip;

	public Evaluator(LogLineGenerator<Context, Action, Reward> generator,
			EvaluationPolicy<Context, Action, Reward> eval,
			ContextualBanditPolicy<Context, Action, Reward> policy) {
		//costructor 1
		this.generator = generator;
		this.evalPolicy = eval;
		this.policy = policy;
	}
	
	public Evaluator(LogLineGenerator<Context, Action, Reward> generator,
			EvaluationPolicy<Context, Action, Reward> eval,
			ContextualBanditPolicy<Context, Action, Reward> policy, int linesToSkip) {
		//costructor 2
		this(generator, eval,policy);
		this.linesToSkip = linesToSkip;
	}

	public double runEvaluation() {
		while (generator.hasNext()) { //if there's other log line to read
			LogLine<Context, Action, Reward> logLine = generator.generateLogLine();	//take the line
			Action a = policy.getActionToPerform(logLine.getContext(),
					generator.getPossibleActions());	//ask to policy which action 
			if (!generator.getPossibleActions().contains(a))	//check for errors
				throw new IllegalChoiceOfArticleException();
			evalPolicy.evaluate(logLine, a);	//call the evaluation on the choosen action
			policy.updatePolicy(logLine.getContext(), a, logLine.getReward());	//call the update funcion of the policy
		}
		return evalPolicy.getResult();	//print timestamp
	}

}
