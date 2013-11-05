package exploChallenge.logs;

import java.util.ArrayList;

import exploChallenge.logs.LogLine;

public class GenericLogLine extends LogLine<GenericVisitor, GenericAction, Boolean> {
	ArrayList<GenericAction> possibleActions;
	
	public GenericLogLine(GenericVisitor visitor, ArrayList<GenericAction> possibleActions ) {
		super(visitor, null, null);
		this.possibleActions=possibleActions;	
	} 
	
	public Double getReward(GenericAction article){
		for(int i=0; i<possibleActions.size(); i++){
			if(article.equals(possibleActions.get(i))){
				return possibleActions.get(i).getReward();
			}
		}
		return null;
	}
	
	public Double getRegret(GenericAction article){
		Double maxReward=Double.MIN_VALUE;
		Double myReward=getReward(article);
		for(int i=0; i<possibleActions.size(); i++){
			if(possibleActions.get(i).getReward()>=maxReward){
				maxReward=possibleActions.get(i).getReward();
			}
		}
		return maxReward-myReward;
	}
}
