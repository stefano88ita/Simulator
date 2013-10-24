package exploChallenge.logs;

import java.util.ArrayList;

import exploChallenge.logs.LogLine;

public class GenericLogLine extends LogLine<GenericVisitor, GenericArticle, Boolean> {
	ArrayList<GenericArticle> possibleActions;
	
	public GenericLogLine(GenericVisitor visitor, ArrayList<GenericArticle> possibleActions ) {
		super(visitor, null, null);
		this.possibleActions=possibleActions;	
	} 
	
	public Double getReward(GenericArticle article){
		for(int i=0; i<possibleActions.size(); i++){
			if(article.equals(possibleActions.get(i))){
				return possibleActions.get(i).getReward();
			}
		}
		return null;
	}
	
	public Double getRegret(GenericArticle article){
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
