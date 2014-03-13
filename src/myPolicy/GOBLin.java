package myPolicy;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.la4j.decomposition.SingularValueDecompositor;
import org.la4j.factory.CRSFactory;
import org.la4j.matrix.Matrix;
import com.csvreader.CsvReader;
import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;

public class GOBLin implements ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean> {
	//init params
	Matrix Minv, b, W, L, Aat12, maxActionFeatures;
	CRSFactory crs;
	double alpha;
	int d, t, n;
	HashMap<Long, Integer> usersIndexes;
	Matrix AkronTmp;
	
	public GOBLin(ArrayList<String> params){
		this.d=Integer.parseInt(params.get(0)); //d is the dimension of action features vector
		crs = new CRSFactory();	//crs is the matrix generator library
		this.alpha=Double.parseDouble(params.get(1)); //alpha is get from startConfiguration.txt
		t=0;	//t is time of computation
		String pathOfL=params.get(2);	//path of matrix L is take from startConfiguration.txt
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
			L=crs.createMatrix(parsedCsv);	//calculating L
			n=L.rows(); //n is the number of users
			Minv=crs.createIdentityMatrix(n*d);
			b=crs.createMatrix(1,n*d);
		} catch (Exception e) {;}
		//calculating A^(-1/2)kroneker
		//Matrix A=L.add(crs.createIdentityMatrix(n));
		Matrix A=L; //already sum identity
		
		SingularValueDecompositor decomposer = new SingularValueDecompositor();
		Matrix[] svd = decomposer.decompose(A, crs);
		Matrix eigenvectors = svd[0];
		Matrix eigenvalues = svd[1];
		for(int i=0;i<eigenvalues.rows();i++){
			eigenvalues.set(i, i, 1/Math.sqrt(eigenvalues.get(i, i)));
		}
		
		
		Aat12 = eigenvectors.multiply(eigenvalues).multiply(eigenvectors.transpose());	//i use a trick, not the real kroneker :)
		
		usersIndexes = new HashMap<Long, Integer>(); //functional array to register index of users
	}
	
	@Override
	public GenericAction getActionToPerform(GenericVisitor visitor, List<GenericAction> possibleActions) { 
		//getting the user index in the list (if is its testFirst time it's added)
		if(!usersIndexes.containsKey(visitor.getId())){
			usersIndexes.put(visitor.getId(),usersIndexes.size());
		}
		
		int indexOfUser = usersIndexes.get(visitor.getId());;
		//kroneker trick
		AkronTmp=crs.createMatrix(n*d, d);
	
		for(int index1=0; index1<n*d; index1++){
			//System.out.println(index1/d);
			//AkronTmp.set(index1, index1%d, Aat12.get(indexOfUser,index1%n));
			AkronTmp.set(index1, index1%d, Aat12.get(indexOfUser,index1/d));
		}
		
		 
		W=b.multiply(Minv);
		double maxScore=-9999; 
		GenericAction actionWithMaxScore = null;
		for(int i=0; i<possibleActions.size(); i++){
			GenericAction action = possibleActions.get(i);
			//prediction phase, it's like linUCB but Minv and W has different size
			double[][] userTmp=new double[1][d];
			userTmp[0]=action.getFeatures();
			Matrix featuresVector = AkronTmp.multiply(crs.createMatrix(userTmp).transpose()).transpose();
			double tmp1= featuresVector.multiply(W.transpose()).get(0, 0);
			double tmp2= alpha * Math.sqrt(featuresVector.multiply(Minv).multiply(featuresVector.transpose()).get(0, 0) * Math.log(t+1) );
			double actionScore=tmp1+tmp2;
			if(i==0||actionScore>maxScore){
				maxScore=actionScore;
				actionWithMaxScore=action;
				maxActionFeatures=featuresVector;
			}
			
			
			//end prediction phase	
		}
		t++;
		if(t%100==0)
			System.out.println("GOBLin processed item: "+t);	//it's only a debug line
		
		return actionWithMaxScore;
	}

	@Override
	public void updatePolicy(GenericVisitor c, GenericAction a, Boolean reward) {
		
		//update, again like linUCB but different dimensions
		double[][] tmpMatrix= new double[1][d];
		tmpMatrix[0]=a.getFeatures();
		/*
		Matrix featuresVector =  crs.createMatrix(tmpMatrix);
		*/
		
		
		Matrix featuresVector = AkronTmp.multiply(crs.createMatrix(tmpMatrix).transpose()).transpose();
		featuresVector = maxActionFeatures;
		try{
		b = b.add(featuresVector.multiply(a.getReward()));
		
		Matrix tmp = Minv.multiply(featuresVector.transpose());
		Minv = Minv.subtract(tmp.multiply(tmp.transpose()).div(1+featuresVector.multiply(tmp).get(0, 0)));
		//end update
		//System.out.println("update done");	//it's only a debug line
		}catch(Exception e){System.out.println(b.columns()+" vs "+d);}
	}
}
