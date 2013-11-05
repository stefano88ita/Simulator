package Test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import exploChallenge.logs.GenericAction;

public class GenericArticleTest {

	static GenericAction article;
	
	@BeforeClass
	public static void initTest(){
		double[] doubleArray = new double[120];
		int[] positions = new int[]{1,3,5,7,9,12,15,19,27};
		for(int i=0; i<positions.length; i++){
			doubleArray[i]=1.2;
		}
		article = new GenericAction(12, doubleArray, 3.0);
	}
	
	@Test
	public void testGenericArticle() {
		assertTrue(true);
	}

	@Test
	public void testGetID() {
		assertTrue(article.getID()==12);
	}

	@Test
	public void testGetReward() {
		assertTrue(article.getReward()==3.0);
	}

	@Test
	public void testGetFeatures() {
		assertTrue(article.getFeatures().getClass().isArray());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(article.equals(article));
	}

}
