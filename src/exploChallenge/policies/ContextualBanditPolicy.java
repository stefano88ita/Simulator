package exploChallenge.policies;

import java.util.List;

public interface ContextualBanditPolicy<Context, Action, Reward> {
	//interface for policy
	public Action getActionToPerform(Context ctx, List<Action> possibleActions);

	public void updatePolicy(Context c, Action a, Reward reward);
}
  