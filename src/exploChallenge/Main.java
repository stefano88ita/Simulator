package exploChallenge;

import java.io.FileNotFoundException;

import exploChallenge.init.PolicyThread;

public class Main { 
	public static void main(String[] args) throws FileNotFoundException {
		//init
		String[] politiche={"MyPolicy","MyPolicy"};
		String[] dataset={"log-test.txt","log-test.txt"};
		int[] datasetUserDimensions={136, 136};
		int[] datasetArticleDimensions={120, 120};
		
		//starting threads
		for(int i=0; i<politiche.length; i++){
			try {
				Thread t = new Thread(new PolicyThread(politiche[i],dataset[i],datasetUserDimensions[i],datasetArticleDimensions[i]));
				t.start();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
}
