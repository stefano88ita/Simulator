package exploChallenge.logs;

public class GenericAction {

	private int id;	//unique id for action
	private double[] features;	//features vector
	private double reward;	//reward for this action
	
	public GenericAction(int id, double[] features, double reward) {
		//construct
		this.id = id;
		this.features = features;
		this.reward = reward;
	}

	public int getID() {
		//it returns the id of action
		return id;
	}
	
	public double getReward() {
		//it returns the reward of action
		return reward;
	}
	
	public double[] getFeatures() {
		//it returns the features vector of action
		return features;
	}

	@Override
	public boolean equals(Object o) {
		//override of method equals
		if (o instanceof GenericAction)
			return ((GenericAction) o).getID() == id;
		return false;
	} 

}
