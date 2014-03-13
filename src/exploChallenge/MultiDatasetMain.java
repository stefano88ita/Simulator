package exploChallenge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import exploChallenge.init.PolicyThread;

public class MultiDatasetMain {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Scanner sc = new Scanner(new File("start_configurations.txt"));
		sc.useDelimiter("\n");
		while(sc.hasNext()){
			Thread t = new Thread(new MainInClass(sc.next()));
			t.start();
			t.join();
			System.out.println("primo simulatore terminato");
		}

	}
}
