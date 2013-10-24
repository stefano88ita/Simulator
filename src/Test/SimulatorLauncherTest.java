package Test;

import static org.junit.Assert.*;


import org.junit.Test;

import exploChallenge.init.SimulatorLauncher;

public class SimulatorLauncherTest {

	@Test
	public void testSimulatorLauncher() {
		assertTrue(true);
	}

	@Test
	public void testUniqueCurrentTimeMS() {
		long t1, t2;
		t1 = SimulatorLauncher.uniqueCurrentTimeMS();
		t2 = SimulatorLauncher.uniqueCurrentTimeMS();
		assertFalse(t1==t2);
	}

}
