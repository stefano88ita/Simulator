package exploChallenge.init;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PolicyThread implements Runnable{
	String policyToUse;
	String dataset;
	int numFeaturesUser;
	int numFeaturesArticle;
	
	public PolicyThread(String policyToUse, String dataset, int numFeaturesUser,int numFeaturesArticle){
		this.policyToUse=policyToUse;
		this.dataset=dataset;
		this.numFeaturesUser=numFeaturesUser;
		this.numFeaturesArticle=numFeaturesArticle;
	}

	public String DataCorrente() 
    { 
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
			new SimulatorLauncher(policyToUse, dataset, numFeaturesUser, numFeaturesArticle);
		} catch (Exception e) {
			try{
				  FileWriter fstream = new FileWriter("error-log.txt",true);
				  BufferedWriter out = new BufferedWriter(fstream);
				  out.write(DataCorrente()+":: "+policyToUse+"("+dataset+"): "+"Errore nel parsing\n");
				  out.close();
			}catch (Exception e2){
				  System.err.println("Error: " + e.getMessage());
			}
		}
	}
}
