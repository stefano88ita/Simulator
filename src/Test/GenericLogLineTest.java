package Test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import exploChallenge.logs.LogLine;
import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericLogLine;
import exploChallenge.logs.GenericLogLineReader;
import exploChallenge.logs.GenericVisitor;

public class GenericLogLineTest {
	//test class for generic log line
	static GenericLogLine line;
	static GenericLogLineReader logLineReader;
	
	@BeforeClass
	public static void initTest() throws IOException{
		try {
			logLineReader = new GenericLogLineReader("Logs/log-test.txt", 136, 120);
			line = (GenericLogLine) logLineReader.read();
		} catch (FileNotFoundException e) {
			System.out.println("Test file removed");
		}
	}
	
	@Test
	public void testGenericLogLine() {
		assertTrue(true);
	}

	@Test
	public void testGetRewardGenericArticle() {
		List<GenericAction> possibleActions = logLineReader.getPossibleActions();
		for(int i=0; i<possibleActions.size();i++){
			assertTrue(line.getReward(possibleActions.get(i))>=0);
		}
	}

	@Test
	public void testGetRegret() {
		List<GenericAction> possibleActions = logLineReader.getPossibleActions();
		for(int i=0; i<possibleActions.size();i++){
			assertTrue(line.getRegret(possibleActions.get(i))>=0);
		}
	}

}
