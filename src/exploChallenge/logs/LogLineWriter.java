package exploChallenge.logs;

import java.io.IOException;

public abstract class LogLineWriter<Context, Action, Reward> {
	//interface for the log line writer
	public abstract void write(LogLine<Context, Action, Reward> line)
			throws IOException;

	public abstract void close() throws IOException;
 
}
 