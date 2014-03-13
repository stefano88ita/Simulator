package myPolicy;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.la4j.factory.CRSFactory;
import org.la4j.matrix.Matrix;

import com.csvreader.CsvWriter;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;

public class LinUCB implements ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean> {
	//init params
	public Matrix Minv, b, W;
	CRSFactory crs;
	double alpha;
	int d, t;
	
	public LinUCB(ArrayList<String> params){
		//initialization of the object, only the testFirst time (#user if it's called by indipendent lin ucb)
		this.d=Integer.parseInt(params.get(0));	//d is taken from the input
		crs = new CRSFactory(); //crs is the matrix generation library
		Minv=crs.createIdentityMatrix(d);
		b=crs.createMatrix(1,d);
		this.alpha=Double.parseDouble(params.get(1));	//alpha is taken from input
		t=0;
	}
	
	@Override
	public GenericAction getActionToPerform(GenericVisitor visitor, List<GenericAction> possibleActions) { 
		W=b.multiply(Minv);
		double maxScore=-9999; 
		GenericAction actionWithMaxScore = null;
		//prediction phase
		for(int i=0; i<possibleActions.size(); i++){
			GenericAction action = possibleActions.get(i);
			double[][] tmpMatrix= new double[1][action.getFeatures().length];
			tmpMatrix[0]=action.getFeatures();
			Matrix featuresVector = crs.createMatrix(tmpMatrix);
			double tmp1= featuresVector.multiply(W.transpose()).get(0, 0);
			double tmp2= alpha * Math.sqrt(featuresVector.multiply(Minv).multiply(featuresVector.transpose()).get(0, 0) * Math.log(t+1) );
			double actionScore=tmp1+tmp2;
			if(i==0||actionScore>maxScore){
				maxScore=actionScore;
				actionWithMaxScore=action;
			}
		}
		
		//end prediction phase
		t++;
		return actionWithMaxScore;
	}

	@Override
	public void updatePolicy(GenericVisitor c, GenericAction a, Boolean reward) {		
		//update phase, no tricks, just the procedure of paper
		double[][] tmpMatrix= new double[1][a.getFeatures().length];
		tmpMatrix[0]=a.getFeatures();
		Matrix featuresVector =  crs.createMatrix(tmpMatrix);
		b = b.add(featuresVector.multiply(a.getReward()));
		
		Matrix tmp = Minv.multiply(featuresVector.transpose());
		Minv = Minv.subtract(tmp.multiply(tmp.transpose()).div(1+featuresVector.multiply(tmp).get(0, 0)));;
		//update phase
		
		
	}
	
}
