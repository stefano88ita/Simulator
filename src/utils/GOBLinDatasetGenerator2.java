package utils;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import org.la4j.factory.CRSFactory;
import org.la4j.matrix.Matrix;

import com.csvreader.CsvWriter;

//please customize the params for different artificial dataset output
public class GOBLinDatasetGenerator2 {
	public static void main(String[] args) {
		//init parameters part I
		String filename="goblin-artificial.txt";
		int numberOfUser=32;
		int numberOfSquareCluster=4;
		String csvFilename="artificialMatrixL.csv";
		CsvWriter csvOutput=null;
		
		//generation of matrix L
		try {
			csvOutput = new CsvWriter(new FileWriter(csvFilename, true), ';');

			//generate L
			int valueDiagonal=numberOfUser/numberOfSquareCluster;
			
			for(int i=0; i<numberOfUser; i++){
				int current=i/valueDiagonal;
				for(int j=0; j<numberOfUser; j++){
					if(i==j){csvOutput.write(valueDiagonal+"");}
					if(i!=j){
						if(current==j/valueDiagonal)
							csvOutput.write("-1");
						else
							csvOutput.write("0");
						
					}
				}
				csvOutput.endRecord();
			}
			csvOutput.close();
		} catch (IOException e) {}
		
		
		//init parameters part I
		double limit=0;
		int k=10;
		int b=25;
		double sum=0;
		int T=10000;
		CRSFactory crs = new CRSFactory();
		int actionIndex=0;
		
		PrintWriter writer=null;
		try {
			writer = new PrintWriter(filename, "UTF-8");
		} catch (Exception e) {
		}
		
		//u generation
		ArrayList<double[][]> Us = new ArrayList<double[][]>();
		for(int j=0; j<numberOfSquareCluster;j++){
			double[][] u=new double[1][b];
			for(int i=0;i<b;i++){
				u[0][i]= -1 + (Math.random() * 2);
				sum+=(u[0][i]*u[0][i]);
			}
			sum=Math.sqrt(sum);
			for(int i=0;i<b;i++){
				u[0][i]/=sum;
			}
			Us.add(u);
		}
		//end u generation
		
		for(int time=0;time<T;time++){
			//random user generation
			int userId = 3;
			
			String res="t#"+time+",u#"+userId;
			
			//for every action
			for(int j=0; j<k; j++){
				res+=",a#"+(actionIndex++)+">";
				sum=0;
				//action features generation
				double[][] action=new double[1][b];
				for(int i=0;i<b;i++){
					action[0][i]= -1 + (Math.random() * 2);
					sum+=(action[0][i]*action[0][i]);
				}
				sum=Math.sqrt(sum);
				//norm
				for(int i=0;i<b;i++){
					action[0][i]/=sum;
					if(i==0){
						res+=i+":"+action[0][i];
					}else{
						res+=" "+i+":"+action[0][i];
					}
				}
				Matrix features = crs.createMatrix(action);
				Matrix factorU = crs.createMatrix(Us.get(userId/(numberOfUser/numberOfSquareCluster)));
				//calculate reward
				Double reward = features.multiply(factorU.transpose()).get(0,0);
				double random=-limit + (Math.random() / (1/(limit*2)));
				reward+=random;
				res+=">"+reward;
				//end action features generation	
			}
			writer.println(res);
			//end for every action
		}
		writer.close();
	}
}
