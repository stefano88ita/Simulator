package myPolicy;
import java.util.ArrayList;
import java.util.List;

import org.la4j.factory.CRSFactory;
import org.la4j.matrix.Matrix;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;

public class LinUCB implements ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean> {
	
	Matrix Minv, b, W;
	CRSFactory crs;
	double alpha;
	int d, t;
	
	public LinUCB(ArrayList<String> params){
		this.d=Integer.parseInt(params.get(0));
		crs = new CRSFactory();
		Minv=crs.createIdentityMatrix(d);
		b=crs.createMatrix(1,d);
		this.alpha=Double.parseDouble(params.get(1));
		t=0;
		W=b.multiply(Minv);
	}
	
	@Override
	public GenericAction getActionToPerform(GenericVisitor visitor, List<GenericAction> possibleActions) { 
		double maxScore=0; 
		GenericAction actionWithMaxScore = null;
		for(int i=0; i<possibleActions.size(); i++){
			GenericAction action = possibleActions.get(i);
			double[][] tmpMatrix= new double[1][action.getFeatures().length];
			tmpMatrix[0]=action.getFeatures();
			Matrix featuresVector = crs.createMatrix(tmpMatrix);
			double tmp1= featuresVector.multiply(W.transpose()).get(0, 0);
			double tmp2= alpha + Math.sqrt(featuresVector.multiply(Minv).multiply(featuresVector.transpose()).get(0, 0) * Math.log(t+1) );
			double actionScore=tmp1+tmp2;
			if(i==0||actionScore>maxScore){
				maxScore=actionScore;
				actionWithMaxScore=action;
			}
		}
		t++;
		return actionWithMaxScore;
	}

	@Override
	public void updatePolicy(GenericVisitor c, GenericAction a, Boolean reward) {
		double[][] tmpMatrix= new double[1][a.getFeatures().length];
		tmpMatrix[0]=a.getFeatures();
		Matrix featuresVector =  crs.createMatrix(tmpMatrix);
		b = b.add(featuresVector.multiply(a.getReward()));
		Matrix tmp = Minv.multiply(featuresVector.transpose());
		Minv = Minv.subtract(tmp.multiply(tmp.transpose()).div(1+featuresVector.multiply(tmp).get(0, 0)));;
	}
}
