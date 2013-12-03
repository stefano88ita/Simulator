package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import myPolicy.LinUCB;

import org.junit.Test;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;

public class LinUCBTest {
	//test class for linucb
	@Test
	public void test() {
		ArrayList<String> params=new ArrayList<String>();
		params.add("6");
		params.add("0.05");
		LinUCB policy = new LinUCB(params);
		assertTrue(policy!=null);
		
		double[] userFeatures=new double[6];
		userFeatures[0]=2;
		userFeatures[1]=4;
		userFeatures[2]=6;
		userFeatures[3]=8;
		userFeatures[4]=10;
		userFeatures[5]=12;
		GenericVisitor user = new GenericVisitor(1, userFeatures);
		
		ArrayList<GenericAction> actions = new ArrayList<GenericAction>();
		
		double[] action1Features=new double[6];
		action1Features[0]=1;
		action1Features[1]=3;
		action1Features[2]=5;
		action1Features[3]=7;
		action1Features[4]=9;
		action1Features[5]=11;
		GenericAction action1 = new GenericAction(1, action1Features, 0.1);
		actions.add(action1);
		
		double[] action2Features=new double[6];
		action2Features[0]=2;
		action2Features[1]=4;
		action2Features[2]=6;
		action2Features[3]=8;
		action2Features[4]=10;
		action2Features[5]=12;
		GenericAction action2 = new GenericAction(2, action2Features, 0.2);
		actions.add(action2);
		
		double[] action3Features=new double[6];
		action3Features[0]=1;
		action3Features[1]=2;
		action3Features[2]=3;
		action3Features[3]=4;
		action3Features[4]=5;
		action3Features[5]=6;
		GenericAction action3 = new GenericAction(3, action3Features, 0.3);
		actions.add(action3);
		
		double[] action4Features=new double[6];
		action4Features[0]=3;
		action4Features[1]=4;
		action4Features[2]=5;
		action4Features[3]=6;
		action4Features[4]=7;
		action4Features[5]=8;
		GenericAction action4 = new GenericAction(4, action4Features, 0.4);
		actions.add(action4);
		
		double[] action5Features=new double[6];
		action5Features[0]=6;
		action5Features[1]=7;
		action5Features[2]=8;
		action5Features[3]=9;
		action5Features[4]=10;
		action5Features[5]=11;
		GenericAction action5 = new GenericAction(5, action5Features, 0.5);
		actions.add(action5);
		
		GenericAction chosedAction = policy.getActionToPerform(user, actions);
		double reward=0;
		double regret=0;
		if(chosedAction.equals(action1)){
			reward=0.1;
		}
		if(chosedAction.equals(action2)){
			reward=0.2;
		}
		if(chosedAction.equals(action3)){
			reward=0.3;
		}
		if(chosedAction.equals(action4)){
			reward=0.4;
		}
		if(chosedAction.equals(action5)){
			reward=0.5;
		}
		regret-=reward;
		
		policy.updatePolicy(user, chosedAction, null);
		System.out.println(chosedAction.getID()+": ");
		System.out.println(policy.Minv);
		System.out.println("===================================");
		
		chosedAction = policy.getActionToPerform(user, actions);
		policy.updatePolicy(user, chosedAction, null);
		System.out.println(chosedAction.getID()+": ");
		System.out.println(policy.Minv);
		System.out.println("===================================");
		
		System.out.println(policy.b);
	}

}
