package myPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.la4j.factory.CRSFactory;
import org.la4j.matrix.Matrix;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;

public class IndipendentLinUCB implements ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean> {
	ArrayList<String> params;
	HashMap<Long, LinUCB> policies;
	
	public IndipendentLinUCB(ArrayList<String> params){
		this.params = params;
		policies = new HashMap<Long, LinUCB>();
	}
	
	@Override
	public GenericAction getActionToPerform(GenericVisitor visitor, List<GenericAction> possibleActions) { 
		//if user has a LinUCB object i call it, else i instantiate a new one
		if(!policies.containsKey(visitor.getId())){
			policies.put(visitor.getId(), new LinUCB(params));
		}
		LinUCB currentPolicy = policies.get(visitor.getId());
		//everything it's like the original algo
		return currentPolicy.getActionToPerform(visitor, possibleActions);
	}

	@Override
	public void updatePolicy(GenericVisitor c, GenericAction a, Boolean reward) {
		LinUCB currentPolicy = policies.get(c.getId());
		//update too it's the same
		currentPolicy.updatePolicy(c, a, reward);
	}
	
}
