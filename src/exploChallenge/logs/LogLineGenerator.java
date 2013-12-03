package exploChallenge.logs;

import java.util.List;

 
 
public interface LogLineGenerator<Context,Action,Reward> {
	//interface for log line generator
	public LogLine<Context,Action,Reward> generateLogLine() ;
	public List<Action> getPossibleActions();
	public boolean hasNext();
	
}
 