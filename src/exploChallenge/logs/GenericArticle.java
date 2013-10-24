package exploChallenge.logs;

public class GenericArticle {

	private int id;
	private byte[] features;
	private double reward;
	
	public GenericArticle(int id, byte[] features, double reward) {
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
	
	public byte[] getFeatures() {
		return features;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof GenericArticle)
			return ((GenericArticle) o).getID() == id;
		return false;
	} 

}
