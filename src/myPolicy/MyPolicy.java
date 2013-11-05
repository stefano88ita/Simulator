package myPolicy;
import java.util.List;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;

public class MyPolicy implements

	ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean> {
	
	public MyPolicy() {
	}
		  
	@Override
	public GenericAction getActionToPerform(GenericVisitor visitor, 
	                             List<GenericAction> possibleActions) {
		return possibleActions.get(0);
	}

	@Override
	public void updatePolicy(GenericVisitor c, GenericAction a, Boolean reward) {
	}
}
