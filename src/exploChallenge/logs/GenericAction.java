package exploChallenge.logs;

public class GenericAction {

	private int id;
	private double[] features;
	private double reward;
	
	public GenericAction(int id, double[] features, double reward) {
		this.id = id;
		this.features = features;
		this.reward = reward;
	}

	public int getID() {
		return id;
	}
	
	public double getReward() {
		return reward;
	}
	
	public double[] getFeatures() {
		return features;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof GenericAction)
			return ((GenericAction) o).getID() == id;
		return false;
	} 

}
