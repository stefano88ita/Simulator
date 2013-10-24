package exploChallenge.logs;

public class GenericVisitor {

	private long timestamp;
	private byte[] features;
 
	public GenericVisitor(long timestamp, byte[] features) {
		this.features = features;
		this.timestamp = timestamp;
	}
 
	public long getTimestamp() {
		return timestamp;
	}

	public byte[] getFeatures() {
		return features;
	}

}
