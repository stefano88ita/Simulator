package Test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;

public class GenericVisitorTest {

	static GenericVisitor visitor;
	
	@BeforeClass
	public static void initTest(){
		double[] doubleArray = new double[120];
		int[] positions = new int[]{1,3,5,7,9,12,15,19,27};
		for(int i=0; i<positions.length; i++){
			doubleArray[i]=1.5;
		}
		visitor = new GenericVisitor(12, doubleArray);
	}
	
	@Test
	public void testGenericVisitor() {
		assertTrue(true);
	}

	@Test
	public void testGetTimestamp() {
		assertTrue(visitor.getTimestamp()==12);
	}

	@Test
	public void testGetFeatures() {
		assertTrue(visitor.getFeatures().getClass().isArray());
	}

}
