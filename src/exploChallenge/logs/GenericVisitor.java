package exploChallenge.logs;

public class GenericVisitor {

	private long timestamp;
	private double[] features;
 
	public GenericVisitor(long timestamp, double[] features) {
		this.features = features;
		this.timestamp = timestamp;
	}
 
	public long getTimestamp() {
		return timestamp;
	}

	public double[] getFeatures() {
		return features;
	}

}
