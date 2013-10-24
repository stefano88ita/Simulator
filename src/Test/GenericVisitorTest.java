package Test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import exploChallenge.logs.GenericArticle;
import exploChallenge.logs.GenericVisitor;

public class GenericVisitorTest {

	static GenericVisitor visitor;
	
	@BeforeClass
	public static void initTest(){
		byte[] byteArray = new byte[120];
		int[] positions = new int[]{1,3,5,7,9,12,15,19,27};
		for(int i=0; i<positions.length; i++){
			byteArray[i]=1;
		}
		visitor = new GenericVisitor(12, byteArray);
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
