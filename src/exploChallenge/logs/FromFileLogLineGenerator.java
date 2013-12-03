package exploChallenge.logs;

import java.io.IOException;
import java.util.List;

public class FromFileLogLineGenerator<Context, Action, Reward> implements
		LogLineGenerator<Context, Action, Reward> {
	
	//init
	private LogLineReader<Context, Action, Reward> reader;

	public FromFileLogLineGenerator(LogLineReader<Context, Action, Reward> reader) {
		//constructor
		this.reader = reader;
	}
  
	@Override
	public LogLine<Context, Action, Reward> generateLogLine() {
		//read line
		LogLine<Context, Action, Reward> line = null;
		try {
			line = reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

	@Override
	public boolean hasNext() {
		//return true if there's another line avaible
		try {
			return reader.hasNext();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Action> getPossibleActions() {
		//get possible actions for a timestamp
		return this.reader.getPossibleActions();
	}

}
