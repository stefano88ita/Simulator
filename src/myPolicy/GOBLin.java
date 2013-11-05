package myPolicy;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.la4j.decomposition.SingularValueDecompositor;
import org.la4j.factory.CRSFactory;
import org.la4j.matrix.Matrix;
import com.csvreader.CsvReader;
import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;

public class GOBLin implements ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean> {
	
	Matrix Minv, b, W, L, Aat12Kron;
	CRSFactory crs;
	double alpha;
	int d, t, n;
	
	public static double[][] kronecker(Matrix A, Matrix B){
		int m=A.rows();
		int p=A.columns();
		int n=B.rows();
		int q=B.columns();
		double result[][]=new double[m*n][p*q];
		for(int i=0;i<m*n;i++){
			for(int j=0;j<p*q;j++){
				result[i][j]=A.get(i/n,j/q)*B.get(i%n,j%q);
			}
		}
		return result;
	}
	
	public GOBLin(ArrayList<String> params){
		this.d=Integer.parseInt(params.get(0));
		crs = new CRSFactory();
		Minv=crs.createIdentityMatrix(d);
		b=crs.createMatrix(1,d);
		this.alpha=Double.parseDouble(params.get(1));
		t=0;
		W=b.multiply(Minv);
		String pathOfL=params.get(2);
		double[][] parsedCsv=null;
		int j=0;
		
		//get Matrix L from file
		try {
			CsvReader csvReader = new CsvReader(new FileReader(pathOfL),';');
			while(csvReader.readRecord()){
				double[] tmpArray=new double[csvReader.getColumnCount()];
				for(int i=0; i<csvReader.getColumnCount();i++){
					tmpArray[i]=Double.parseDouble(csvReader.get(i));
				}
				if(parsedCsv==null){
					parsedCsv = new double[csvReader.getColumnCount()][csvReader.getColumnCount()];
				}
				parsedCsv[j++]=tmpArray;
			}
			L=crs.createMatrix(parsedCsv);
			n=L.rows();
		} catch (Exception e) {;}
		
		//calculating A^(-1/2)kroneker
		Matrix A=L.add(crs.createIdentityMatrix(n));
		SingularValueDecompositor decomposer = new SingularValueDecompositor();
		Matrix[] svd = decomposer.decompose(A, crs);
		Matrix eigenvectors = svd[0];
		Matrix eigenvalues = svd[1];
		for(int i=0;i<eigenvalues.rows();i++){
			eigenvalues.set(i, i, 1/Math.sqrt(eigenvalues.get(i, i)));
		}
		Matrix Aat12 = eigenvectors.multiply(eigenvalues).multiply(eigenvectors.transpose());
		Aat12Kron=crs.createMatrix(kronecker(Aat12,crs.createIdentityMatrix(d)));
	}
	
	@Override
	public GenericAction getActionToPerform(GenericVisitor visitor, List<GenericAction> possibleActions) { 
		double maxScore=0; 
		GenericAction actionWithMaxScore = null;
		for(int i=0; i<possibleActions.size(); i++){
			GenericAction action = possibleActions.get(i);
			double[][] tmpMatrix= new double[1][action.getFeatures().length];
			tmpMatrix[0]=action.getFeatures();
			Matrix featuresVector = crs.createMatrix(tmpMatrix);
			double tmp1= featuresVector.multiply(W.transpose()).get(0, 0);
			double tmp2= alpha + Math.sqrt(featuresVector.multiply(Minv).multiply(featuresVector.transpose()).get(0, 0) * Math.log(t+1) );
			double actionScore=tmp1+tmp2;
			if(i==0||actionScore>maxScore){
				maxScore=actionScore;
				actionWithMaxScore=action;
			}
		}
		t++;
		return actionWithMaxScore;
	}

	@Override
	public void updatePolicy(GenericVisitor c, GenericAction a, Boolean reward) {
		double[][] tmpMatrix= new double[1][a.getFeatures().length];
		tmpMatrix[0]=a.getFeatures();
		Matrix featuresVector =  crs.createMatrix(tmpMatrix);
		b = b.add(featuresVector.multiply(a.getReward()));
		Matrix tmp = Minv.multiply(featuresVector.transpose());
		Minv = Minv.subtract(tmp.multiply(tmp.transpose()).div(1+featuresVector.multiply(tmp).get(0, 0)));;
	}
}
