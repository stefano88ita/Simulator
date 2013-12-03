package exploChallenge.logs;

public class LogLine<Context, Action, Reward> {
	//init
	private Reward reward;
	private Action action;
	private Context context;

	public LogLine() {
		//useless costructor
	} 

	public LogLine(Context context, Action action, Reward reward) {
		//constructor
		this.setContext(context);
		this.setAction(action);
		this.setReward(reward);
	}

	public Reward getReward() {
		//it returns reward
		return reward;
	} 

	public void setReward(Reward reward) {
		//set the reward
		this.reward = reward;
	}

	public Action getAction() {
		//get action
		return action;
	}

	public void setAction(Action action) {
		//set action
		this.action = action;
	}

	public Context getContext() {
		//get user
		return context;
	}

	public void setContext(Context context) {
		//set user
		this.context = context;
	}

}
