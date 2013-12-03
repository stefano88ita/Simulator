package exploChallenge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import exploChallenge.init.PolicyThread;

public class Main { 
	public static void main(String[] args) throws FileNotFoundException {
		//init
		System.out.println("simulator is started now");	
		ArrayList<String> policies=null;
		ArrayList<ArrayList<String>> parameters=null;
		String dataset=null;
		int datasetUserDimensions=0;
		int datasetActionDimensions=0;
		
		//parsing the input configuration file: policy, pamams, etc...
		Scanner sc = new Scanner(new File("start_configuration.txt"));
		sc.useDelimiter("\n");
		while(sc.hasNext()){
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
		
		if(!(policies!=null && dataset!=null && datasetUserDimensions!=-1 && datasetActionDimensions!=-1)){
			System.out.println("Error!Please check your start_configuration.txt file");
		}else{
			//starting a thread for each policy
			for(int i=0; i<policies.size(); i++){
				try {
					Thread t = new Thread(new PolicyThread(policies.get(i),parameters.get(i),dataset,datasetUserDimensions,datasetActionDimensions));
					t.start();
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
			System.out.println("end");
		}
	}
}
