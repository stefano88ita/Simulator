package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.stromberglabs.cluster.KMeansClusterer;

import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericLogLineReader;
import exploChallenge.logs.GenericVisitor;
import exploChallenge.logs.LogLine;

public class YahooClustering {
	public static void main(String[] args) throws FileNotFoundException {
		int numOfClusters = 10;
		ArrayList<String> policies=null;
		ArrayList<ArrayList<String>> parameters=null;
		String dataset=null;
		int datasetUserDimensions=0;
		int datasetActionDimensions=0;
		//ArrayList<double[]> userList = new ArrayList<double[]>();
		//KMeansClusterer kmeans = new KMeansClusterer();
		//kmeans.cluster(userList, numOfClusters);
		//parsing the input configuration file: policy, pamams, etc...
		Scanner sc = new Scanner(new File("start_configuration2.txt"));
		sc.useDelimiter("\n");
		int count = 0;
		while(sc.hasNext()){
			System.out.println("Riga "+(count++));
			Scanner sc1=new Scanner(sc.next());
			sc1.useDelimiter("=");
			String key=sc1.next();
			String value=sc1.next();
			if(key.equals("Algoritms")){
				Scanner sc2=new Scanner(value);
				sc2.useDelimiter(";");
				policies=new ArrayList<String>();
				parameters=new ArrayList<ArrayList<String>>();
				while(sc2.hasNext()){
					String methodAndParams=sc2.next();
					Scanner sc3=new Scanner(methodAndParams);
					sc3.useDelimiter(":");
					policies.add(sc3.next());
					String params=null;
					if(sc3.hasNext()){
						params = sc3.next();
						ArrayList<String> paramsList = new ArrayList<String>();
						Scanner sc4=new Scanner(params);
						sc4.useDelimiter(",");
						while(sc4.hasNext()){
							paramsList.add(sc4.next());
						}
						parameters.add(paramsList);
					}else{
						ArrayList<String> paramsList = new ArrayList<String>();
						parameters.add(paramsList);
					}
				}
			}
			if(key.equals("Dataset")){
				dataset = value;			
			}
			if(key.equals("Userdim")){
				datasetUserDimensions=Integer.parseInt(value);
			}
			if(key.equals("Actiondim")){
				datasetActionDimensions=Integer.parseInt(value);
			}
		}
		
		
		try {
			GenericLogLineReader logLineReader = new GenericLogLineReader(dataset,datasetUserDimensions, datasetActionDimensions);
			HashMap<String, double[]> hash = new HashMap<String, double[]>();
			while(logLineReader.hasNext()){
				LogLine<GenericVisitor, GenericAction, Boolean> line = logLineReader.read();
				double[] userFeatures = line.getContext().getFeatures();
				String key = "";
				for(int i=0; i<userFeatures.length; i++){
					key = key+userFeatures[i];
				}
				if(!hash.containsKey(key)){ hash.put("key", userFeatures);}
			}
			System.out.println(hash.size());
			
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		} catch (IOException e) {
		}

	}

}
