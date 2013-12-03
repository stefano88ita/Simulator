package exploChallenge.logs;

import java.util.ArrayList;

import exploChallenge.logs.LogLine;

public class GenericLogLine extends LogLine<GenericVisitor, GenericAction, Boolean> {
	//init
	ArrayList<GenericAction> possibleActions;
	
	public GenericLogLine(GenericVisitor visitor, ArrayList<GenericAction> possibleActions ) {
		//constructor
		super(visitor, null, null);
		this.possibleActions=possibleActions;	
	} 
	
	public Double getReward(GenericAction article){
		//get reward of a specific action
		for(int i=0; i<possibleActions.size(); i++){
			if(article.equals(possibleActions.get(i))){
				return possibleActions.get(i).getReward();
			}
		}
		return null;	//return null if there's no action equals to the one send as param
	}
	
	public Double getRegret(GenericAction article){
		//get regret of a specific action
		Double maxReward=Double.MIN_VALUE;
		Double myReward=getReward(article);
		for(int i=0; i<possibleActions.size(); i++){
			if(possibleActions.get(i).getReward()>=maxReward){
				maxReward=possibleActions.get(i).getReward();
			}
		}
		return maxReward-myReward;	//regret for an action is the max reward in possible actions minus reward of chosen action
	}
}
