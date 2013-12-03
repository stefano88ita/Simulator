package myPolicy;
import java.util.List;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;

public class TestFirst implements
	//policy that return the testFirst action, only for test
	ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean> {
	
	public TestFirst() {}
		  
	@Override
	public GenericAction getActionToPerform(GenericVisitor visitor, List<GenericAction> possibleActions) {
		return possibleActions.get(0);
	}

	@Override
	public void updatePolicy(GenericVisitor c, GenericAction a, Boolean reward) {}
}
