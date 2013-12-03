package utils;
import java.io.PrintWriter;
import org.la4j.factory.CRSFactory;
import org.la4j.matrix.Matrix;

//please customize the params for different artificial dataset output
public class LinUCBDatasetGenerator {
	public static void main(String[] args) {
		//init params
		String filename="artificial-3.txt";
		double limit=0.25;
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
		double[][] u=new double[1][b];
		for(int i=0;i<b;i++){
			u[0][i]= -1 + (Math.random() * 2);
			sum+=(u[0][i]*u[0][i]);
		}
		sum=Math.sqrt(sum);
		for(int i=0;i<b;i++){
			u[0][i]/=sum;
		}
		//end u generation
		
		for(int time=0;time<T;time++){
			
			
			//unique user id is timestamp + 10000
			int userId = 1;
			//end
			
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
				//norm
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
				//calculating reward
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
