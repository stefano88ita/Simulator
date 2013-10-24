package myPolicy;
import java.util.List;

import exploChallenge.logs.GenericArticle;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;

public class MyPolicy implements

	ContextualBanditPolicy<GenericVisitor, GenericArticle, Boolean> {
	
	public MyPolicy() {
	}
		  
	@Override
	public GenericArticle getActionToPerform(GenericVisitor visitor, 
	                             List<GenericArticle> possibleActions) {
		return possibleActions.get(0);
	}

	@Override
	public void updatePolicy(GenericVisitor c, GenericArticle a, Boolean reward) {
	}
}
