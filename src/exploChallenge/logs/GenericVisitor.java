package exploChallenge.logs;

public class GenericVisitor {

	private long userid;	//unique id for visitor
	private double[] features;	//features vector for user
 
	public GenericVisitor(long userid, double[] features) {
		//constructor
		this.features = features;
		this.userid = userid;
	}
 
	public long getId() {
		//returns user id of visitor
		return userid;
	}

	public double[] getFeatures() {
		//returns user's features vector
		return features;
	}

}
