package utils;
import java.io.PrintWriter;
import org.la4j.factory.CRSFactory;
import org.la4j.matrix.Matrix;

public class DatasetGenerator {
	public static void main(String[] args) {
		String filename="artificial-1.txt";
		double limit=0;
		int k=10;
		int b=25;
		int Min=0;
		int Max=255;
		double sum=0;
		CRSFactory crs = new CRSFactory();
		int actionIndex=0;
		
		PrintWriter writer=null;
		try {
			writer = new PrintWriter(filename, "UTF-8");
		} catch (Exception e) {
		}
		
		for(int time=0;time<10000;time++){
			//u generation
			double[][] u=new double[1][b];
			for(int i=0;i<b;i++){
				u[0][i]=Min + (int)(Math.random() * ((Max - Min) + 1));
				sum+=(u[0][i]*u[0][i]);
			}
			sum=Math.sqrt(sum);
			for(int i=0;i<b;i++){
				u[0][i]/=sum;
			}
			//end u generation
			
			//unique user id is timestamp + 10000
			int userId = 10000+time;
			//end
			
			String res="t#"+time+",u#"+userId;
			
			//for every action
			for(int j=0; j<k; j++){
				res+=",a#"+(actionIndex++)+">";
				sum=0;
				//action features generation
				double[][] action=new double[1][b];
				for(int i=0;i<b;i++){
					action[0][i]=Min + (int)(Math.random() * ((Max - Min) + 1));
					sum+=(action[0][i]*action[0][i]);
				}
				sum=Math.sqrt(sum);
				for(int i=0;i<b;i++){
					action[0][i]/=sum;
					if(i==0){
						res+=i+":"+action[0][i];
					}else{
						res+=" "+i+":"+action[0][i];
					}
				}
				Matrix features = crs.createMatrix(action);
				Matrix factorU = crs.createMatrix(u);
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
