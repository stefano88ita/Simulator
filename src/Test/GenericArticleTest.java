package Test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import exploChallenge.logs.GenericArticle;

public class GenericArticleTest {

	static GenericArticle article;
	
	@BeforeClass
	public static void initTest(){
		byte[] byteArray = new byte[120];
		int[] positions = new int[]{1,3,5,7,9,12,15,19,27};
		for(int i=0; i<positions.length; i++){
			byteArray[i]=1;
		}
		article = new GenericArticle(12, byteArray, 3.0);
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
