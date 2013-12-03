package exploChallenge.init;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

//this class is a thread that starts a single policy
public class PolicyThread implements Runnable{
	
	//init
	String policyToUse;
	String dataset;
	int numFeaturesUser;
	int numFeaturesArticle;
	ArrayList<String> params;
	
	public PolicyThread(String policyToUse, ArrayList<String> params, String dataset, int numFeaturesUser,int numFeaturesArticle){
		//costructor
		this.policyToUse=policyToUse;
		this.dataset=dataset;
		this.numFeaturesUser=numFeaturesUser;
		this.numFeaturesArticle=numFeaturesArticle;
		this.params=params;
	}

	public String DataCorrente() 
    { 
		//procedure to calculate the date of today, with format: dd/mm/yyyy
		 Calendar cal = new GregorianCalendar(); 
		 int giorno = cal.get(Calendar.DAY_OF_MONTH); 
		 int mese = 1+cal.get(Calendar.MONTH); 
		 int anno = cal.get(Calendar.YEAR); 
		 String mesead = mese+"";
		 if(mese<10) 
			 mesead ="0"+mese; 
		 String dataoggi=(giorno + "/" + mesead  + "/" + anno); 
		return dataoggi; 
    }
	
	@Override
	public void run() {
		try {
			new SimulatorLauncher(policyToUse, params, dataset, numFeaturesUser, numFeaturesArticle);	//start the policy
		} catch (Exception e) {
			try{
				//exception handling (on error-log.txt)
				  FileWriter fstream = new FileWriter("Logs/error-log.txt",true);
				  BufferedWriter out = new BufferedWriter(fstream);
				  out.write(DataCorrente()+":: "+policyToUse+"("+dataset+"): "+"Errore nel parsing: "+e.toString()+"\n");
				  out.close();
			}catch (Exception e2){
				//if file does not exists the output is on consolle
				  System.err.println("Error: " + e.getMessage());
			}
		}
	}
}
