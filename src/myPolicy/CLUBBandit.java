package myPolicy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.la4j.factory.CRSFactory;
import org.la4j.matrix.Matrices;
import org.la4j.matrix.Matrix;
import org.la4j.vector.Vector;

import com.csvreader.CsvWriter;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.policies.ContextualBanditPolicy;

public class CLUBBandit implements ContextualBanditPolicy<GenericVisitor, GenericAction, Boolean> {
	//init params
	Matrix W, L, Aat12, maxActionFeatures, used, bu, b, w, wU, p_;
	Matrix[] M, UM_inv, Minv;
	CRSFactory crs;
	double alpha;
	int d, t, n, k, clusterMaster;
	HashMap<Long, Integer> usersIndexes;
	double p;
	int endsplit, previousNC;
	DirectedGraph<Integer, DefaultEdge> usersGraphT;
	List cc;
	double alpha2;	//this must be read from input file	
	
	public CLUBBandit(ArrayList<String> params) {
		d=Integer.parseInt(params.get(0)); 			//d is the dimension of action features vector
		k=Integer.parseInt(params.get(1));			//k is the number of vectors per step
		n=Integer.parseInt(params.get(2));			//n is the number of users
		crs = new CRSFactory();						//crs is the matrix generator library
		alpha=Double.parseDouble(params.get(3));	//alpha is get from startConfiguration.txt
		t=0;										//t is time of computation
		alpha2=Double.parseDouble(params.get(4));	//alpha2 is get from startConfiguration.txt
		
		System.out.println("init step 1");
		//init global vars
		//p=5*Math.log(n)/n;
		p=1;
		used = crs.createMatrix(1,n);
		Matrix usersGraph = crs.createRandomMatrix(n, n);
		for(int i=0; i<n;i++){
			for(int v=0;v<n;v++){
				double generated = Math.random();
				if(generated>p){
					usersGraph.set(i, v, 0);
					usersGraph.set(v, i, 0);
				}else{
					usersGraph.set(i, v, 1);
					usersGraph.set(v, i, 1);
				}
			}
		}
		
		//check
		for(int i=0; i<n;i++){
			for(int v=0;v<n;v++){
				if(usersGraph.get(i,v)!=usersGraph.get(v,i)){
					System.out.println("Errore"); //must be triu
				}
			}
		}
		
		System.out.println("init step 2");
		//Matrix tmp = usersGraph.triangularize();
		Matrix tmp = usersGraph;
		Matrix diagonalTmp=crs.createMatrix(n,n);
		
		
		
		usersGraphT = new SimpleDirectedGraph<Integer,DefaultEdge>(DefaultEdge.class);
		for(int i=0;i<n;i++){diagonalTmp.set(i, i, 0); usersGraphT.addVertex(i);}
		//diagonalTmp ha la diagonale a zero

		for(int i=0;i<n;i++){
			for(int v=0;v<n; v++){
				if(i!=v && usersGraph.get(i,v)>0){	//edge must be from A to B and from B to A to be strongly connected
					usersGraphT.addEdge(i, v);
				}
			}
		}
		System.out.println("init step 3");
		Minv = new Matrix[1];
		Minv[0]=crs.createMatrix(d,d);
		M = new Matrix[n];
		UM_inv = new Matrix[n];
		for(int i=0;i<n;i++){
			M[i]=crs.createIdentityMatrix(d);
			UM_inv[i]=crs.createIdentityMatrix(d);
		}
		
		System.out.println("init step 4");
		
		bu = crs.createMatrix(d, n);
		b = crs.createMatrix(d,1);
		
		w = crs.createMatrix(1,d);
		wU=crs.createMatrix(n,d);
		
		p_=crs.createMatrix(k,1);
		
		endsplit = 0;
		previousNC = 0;
		System.out.println("classification start!");
	}

	private void getWFromClusters(List cc){
		//this method calculate w, Minv and b
		int numClusters=cc.size();
		w = crs.createMatrix(numClusters, d);
		b = crs.createMatrix(d,numClusters);
		Minv = new Matrix[numClusters];
		for(int i=0; i<numClusters; i++){
			Minv[i]=crs.createIdentityMatrix(d);
		}
		for(int nc=0; nc<numClusters; nc++){
			Set nodesInTheCluster = (Set) cc.get(nc);
			Matrix tmpA = crs.createIdentityMatrix(d);
			for(int j=0; j<nodesInTheCluster.size();j++){
				for(int index=0;index<d;index++){
					try{
						b.set(index, nc, (b.get(index, nc) + bu.get(index, (Integer) nodesInTheCluster.toArray()[j])));
					}catch(Exception e){System.out.println("eccezione");b.set(index, nc,0);}
				}
				tmpA = tmpA.add(M[(Integer) nodesInTheCluster.toArray()[j]]);
			}
			try{
				Minv[nc]=tmpA.inverse(Matrices.GAUSSIAN_INVERTOR);
			}catch(Exception e){System.out.println(e);}
			Matrix bNC = crs.createMatrix(b.getColumn(nc).length(),1);
			bNC.setColumn(0, b.getColumn(nc));
			w.setRow(nc, Minv[nc].multiply(bNC).getColumn(0));
		}
	}
	
	private int findClusterByUserId(int id){
		for(int i=0; i<cc.size(); i++){
			if(((Set)cc.get(i)).contains((Integer)id)){
				return i;
			}
		}
		return 0;
	}
	
	@Override
	public GenericAction getActionToPerform(GenericVisitor visitor, List<GenericAction> possibleActions) {
		//this check is made every 100 steps, it re-calculate the subgraphs
		if(t%100==0){	
			ConnectivityInspector connectivityInspector = new ConnectivityInspector((DirectedGraph) usersGraphT);
			List listOfConnectedSubgraphs = connectivityInspector.connectedSets();
			int numberOfConnectedComponents = listOfConnectedSubgraphs.size();
			if(numberOfConnectedComponents>previousNC){
				cc=listOfConnectedSubgraphs;
				previousNC=numberOfConnectedComponents;
				getWFromClusters(listOfConnectedSubgraphs);
			}
			
			if(t%500==0){
				System.out.println("Number of cc: "+numberOfConnectedComponents);
			}
		}
		
		//trick
		if(t==0){Minv[0]=crs.createIdentityMatrix(d);}
		
		//steps for the algo's choice
		int actionWithMaxScore=-1;
		double maxScore=-9999;
		clusterMaster = findClusterByUserId(Integer.parseInt(visitor.getId()+""));	//cluster master is the user's cluster
		for(int j=0;j<possibleActions.size();j++){
			GenericAction action = possibleActions.get(j);
			double[][] tmpMatrix= new double[1][action.getFeatures().length];
			tmpMatrix[0]=action.getFeatures();
			Matrix featuresVector=crs.createMatrix(tmpMatrix);
			Vector wClusterMasterTmp = w.getRow(clusterMaster);
			Matrix wClusterMaster  = crs.createMatrix(1, wClusterMasterTmp.length());
			wClusterMaster.setRow(0, wClusterMasterTmp);
			double tmp1= featuresVector.multiply(wClusterMaster.transpose()).get(0,0);
			double tmp2= alpha * Math.sqrt(featuresVector.multiply(Minv[clusterMaster]).multiply(featuresVector.transpose()).get(0,0) * Math.log(t+1));
			double actionScore = tmp1 + tmp2 ;
			if(j==0||actionScore>maxScore){
				maxScore=actionScore;
				actionWithMaxScore=j;	
			}
		}
				t++;
		return possibleActions.get(actionWithMaxScore);
	}

	@Override
	public void updatePolicy(GenericVisitor c, GenericAction a, Boolean reward) {
		double[][] tmpMatrix= new double[1][a.getFeatures().length];
		tmpMatrix[0]=a.getFeatures();
		Matrix featuresVector =  crs.createMatrix(tmpMatrix);
		
			//update b's for user and cluster
			b.setColumn(clusterMaster, b.getColumn(clusterMaster).add(crs.createVector(a.getFeatures()).multiply(a.getReward())));
			
			
			
			bu.setColumn(Integer.parseInt(c.getId()+"") , bu.getColumn(Integer.parseInt(c.getId()+"")).add(crs.createVector(a.getFeatures()).multiply(a.getReward())));

			//update M_inv with Sherman-Morrison -- cluster matrix
			
	        Matrix tmp = Minv[clusterMaster].multiply(featuresVector.transpose());
	        Minv[clusterMaster] = Minv[clusterMaster].subtract(tmp.multiply(tmp.transpose()).div((1+featuresVector.multiply(tmp).get(0,0))));
			
	        //update M of the user --- watch out, M is the inverse called M_inv 
			M[Integer.parseInt(c.getId()+"")] = M[Integer.parseInt(c.getId()+"")].add(featuresVector.transpose().multiply(featuresVector));
			
			//update the inverse for the user, we need this to find the correct w and update the graph later
			tmp = UM_inv[Integer.parseInt(c.getId()+"")].multiply(featuresVector.transpose());
			UM_inv[Integer.parseInt(c.getId()+"")] = UM_inv[Integer.parseInt(c.getId()+"")].subtract(tmp.multiply(tmp.transpose()).div((1+featuresVector.multiply(tmp).get(0,0))));
			                 
	        //update w
			w.setRow(clusterMaster,Minv[clusterMaster].multiply(b.getColumn(clusterMaster)));
	        wU.setRow(Integer.parseInt(c.getId()+""),UM_inv[Integer.parseInt(c.getId()+"")].multiply(bu.getColumn(Integer.parseInt(c.getId()+""))));
	        used.set(0,Integer.parseInt(c.getId()+""),  used.get(0,Integer.parseInt(c.getId()+""))+1);
	        
	        Object[] currentCluster = ((Set)cc.get(findClusterByUserId(Integer.parseInt(c.getId()+"")))).toArray();
		       
	        for(int j=0; j<currentCluster.length; j++){
	        	//UPDATE: for saving time especially in the large data set
	        	
	        	//!changed this line
	        	if(wU.getRow(Integer.parseInt(c.getId()+"")).subtract(wU.getRow(j)).norm()>=alpha2*
	        			(Math.sqrt(Math.log(1+ used.get(0,Integer.parseInt(c.getId()+"")))/(1+Integer.parseInt(c.getId()+""))) 
	        			+ Math.sqrt((Math.log(1+used.get(0, j) ) )/(1+used.get(0, j))))){
	        		usersGraphT.removeEdge(Integer.parseInt(c.getId()+""), j);
	        		usersGraphT.removeEdge(j, Integer.parseInt(c.getId()+""));
	        	}
	        }
	        
	      
	}

}


