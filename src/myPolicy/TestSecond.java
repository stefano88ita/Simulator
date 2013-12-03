package myPolicy;
import java.util.List;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;

public class TestSecond implements
//policy that return the testFirst action, only for test
	ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean> {
	
	public TestSecond() {}
		  
	@Override
	public GenericAction getActionToPerform(GenericVisitor visitor, List<GenericAction> possibleActions) {
		return possibleActions.get(1);
	}

	@Override
	public void updatePolicy(GenericVisitor c, GenericAction a, Boolean reward) {}
}
