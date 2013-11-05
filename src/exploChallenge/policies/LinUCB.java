package exploChallenge.policies;

import org.la4j.*;
import org.la4j.matrix.Matrices;
import org.la4j.matrix.Matrix;
import org.la4j.vector.Vector;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;


public class LinUCB implements ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean>{
	Matrix A, M, LM, Ax, AxInv;
	Vector ucb,b,w;
	int numeroAds,d;
	HashMap<String, Integer> users, ads;
	org.la4j.factory.CRSFactory crs;
	
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
	
	public LinUCB(){
		try{
			  FileInputStream fstream = new FileInputStream("Dataset/output.txt");
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  Boolean flag=false;
			  int numeroColonne=0;
			  int numeroRighe=0;
			  while ((strLine = br.readLine()) != null)   {
				  if(!flag){
					  flag=true;
					  StringTokenizer st = new StringTokenizer(strLine, ","); 
					  while(st.hasMoreTokens()) { 
						  st.nextToken();
						  numeroColonne++;
					  }
				  }
				  numeroRighe++;  
			  }
			  double[][] matrice=new double[numeroRighe][numeroColonne];
			  in.close();
			  System.out.println("Parsing: 1/4");
			  fstream = new FileInputStream("Dataset/output.txt");
			  in = new DataInputStream(fstream);
			  br = new BufferedReader(new InputStreamReader(in));
			  int colonna=0;
			  int riga=0;
			  while ((strLine = br.readLine()) != null)   {
				  StringTokenizer st = new StringTokenizer(strLine, ","); 
				  colonna=0;
				  while(st.hasMoreTokens()) { 
					  double token=Double.parseDouble(st.nextToken());
					  matrice[riga][colonna]=token;
					  colonna++;
				  }
				  riga++; 
			  }
			  in.close();
			  System.out.println("Parsing: 1/2");
			  
			  users=new HashMap<String,Integer>();
			  fstream = new FileInputStream("Dataset/output_users.txt");
			  in = new DataInputStream(fstream);
			  br = new BufferedReader(new InputStreamReader(in));
			  colonna=0;
			  riga=0;
			  while ((strLine = br.readLine()) != null)   {
				  StringTokenizer st = new StringTokenizer(strLine, ","); 
				  colonna=0;
				  while(st.hasMoreTokens()) { 
					  String token=st.nextToken();
					  users.put(token, colonna);
					  colonna++;
				  }
				  riga++; 
			  }
			  in.close();
			  System.out.println("Parsing: 3/4");
			  
			  ads=new HashMap<String,Integer>();
			  fstream = new FileInputStream("Dataset/output_ads.txt");
			  in = new DataInputStream(fstream);
			  br = new BufferedReader(new InputStreamReader(in));
			  colonna=0;
			  riga=0;
			  while ((strLine = br.readLine()) != null)   {
				  StringTokenizer st = new StringTokenizer(strLine, ","); 
				  colonna=0;
				  while(st.hasMoreTokens()) { 
					  String token=st.nextToken();
					  ads.put(token, colonna);
					  colonna++;
				  }
				  riga++; 
			  }
			  in.close();
			  System.out.println("Parsing: Completo");
			  

			  crs = new org.la4j.factory.CRSFactory();
				 
			  numeroAds=numeroColonne;
			  d=numeroAds;
			  
			  M = crs.createMatrix(matrice);
			  System.out.println("M: Creata");
			  A=crs.createIdentityMatrix(numeroAds).add(M.transpose().multiply(M));
			  System.out.println("A: Creata");
			  //posso fare identitˆ * A alla - 1/2
			  Ax=crs.createMatrix(kronecker(crs.createIdentityMatrix(d),A));
			  System.out.println("Ax: Creata");
			  A=null;
			  System.gc();
			  AxInv=Ax.inverse(Matrices.DEFAULT_INVERTOR);
			  Ax=null;
			  System.gc();
			  System.out.println("Ax: Invertita");
			  ucb=crs.createVector(numeroAds);
			  System.out.println("ucb: Creata");
			  w=crs.createVector(numeroAds*d);
			  System.out.println("x: Creata");
			  b=crs.createVector(numeroAds);
			  System.out.println("b: Creata");
			  LM=crs.createIdentityMatrix(w.length());
			  System.out.println("LM: Creata");
			  
			  System.out.println("Inizializzazione completata");
		}catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		}
	}
	
	
	@Override
	public GenericAction getActionToPerform(GenericVisitor visitor, List<GenericAction> possibleActions) {
		String user="user ";
		for(int i=1; i<=visitor.getFeatures().length; i++){
			if(visitor.getFeatures()[i-1]==1){
				user+=(i+" ");
			}
		}
		if(users.containsKey(user)){ //check if user is in users' set
			Vector x=M.getRow(users.get(user)); //take rows of this user
			for(int i=0;i<possibleActions.size();i++){	//for every possible actions
				GenericAction azione=possibleActions.get(i); //i take the action
				if(ads.containsKey("id-"+azione.getID())){ //if the ads set contains this action
					int posAd=ads.get("id-"+azione.getID()); //i take the position
					Vector fixa=crs.createVector(numeroAds*d);
					for(int j=0;j<d;j++){
						fixa.set(posAd*numeroAds + j, x.get(j));
					}
					//Vector fiCapxa=fixa.multiply(Ax);
				}
			}
			
		}else{
			System.out.println("Not in training set");
		}
		System.out.println("");
		return null;
	}

	@Override
	public void updatePolicy(GenericVisitor visitor, GenericAction action, Boolean reward) {		
	}

}
