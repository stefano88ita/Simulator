package Test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import exploChallenge.logs.LogLine;
import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericLogLine;
import exploChallenge.logs.GenericLogLineReader;
import exploChallenge.logs.GenericVisitor;

public class GenericLogLineReaderTest {
	//test class for generic log line reader
	static GenericLogLineReader logLineReader;
	
	@BeforeClass
	public static void initTest(){
		try {
			logLineReader = new GenericLogLineReader("Logs/log-test.txt", 136, 120);
		} catch (FileNotFoundException e) {
			System.out.println("Test file removed");
		}
	}
	
	@Test
	public void testGenericLogLineReader() {
		assertTrue(true);
	}

	@Test
	public void testRead() {
		LogLine<GenericVisitor, GenericAction, Boolean> line;
		try {
			line = logLineReader.read();
		} catch (IOException e) {
			line=null;
		}
		assertTrue(line instanceof GenericLogLine);
		assertTrue(!(line==null));
	}

	@Test
	public void testHasNext() {
		try {
			assertTrue(logLineReader.hasNext());
		} catch (IOException e) {
		}
	}

	@Test
	public void testClose() {
		assertTrue(true);
	}

	@Test
	public void testGetPossibleActions() {
		assertTrue(logLineReader.getPossibleActions().size()>0);
	}
}
