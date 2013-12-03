package exploChallenge.eval;

import java.io.FileWriter;
import java.io.PrintStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.csvreader.CsvWriter;

import exploChallenge.logs.LogLine;
import exploChallenge.logs.GenericAction;
import exploChallenge.logs.GenericLogLine;


public class MyEvaluationPolicy<Context, Action> implements
		EvaluationPolicy<Context, Action, Boolean> {
	//init
	double cumulativeReward=0;
	double cumulativeRegret=0;
	private int clicks;
	private int evaluations;
	private int lines;
	private PrintStream logger;
	String outputFile = "Results/users.csv";
	CsvWriter csvOutput;
	int number=1;
	
	public MyEvaluationPolicy(String filename) {
		//constructor
		this.outputFile="Results/"+filename;
		try {
			if (!new File(outputFile).exists())
			{
				//write the headers of csv output file
				csvOutput = new CsvWriter(new FileWriter(outputFile, true), ';');	// ";" is good for excel, for matlab "," and no headers
				csvOutput.write("NUMBER");
				csvOutput.write("REWARD");
				csvOutput.write("CUMULATIVE REWARD");
				csvOutput.write("REGRET");
				csvOutput.write("CUMULATIVE REGRET");
				csvOutput.endRecord();
			}else{
				csvOutput = new CsvWriter(new FileWriter(outputFile, true), ';');
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void log() {
		//yahoo-back-compatibility-procedure
		logger.println(lines + " " + evaluations + " " + clicks + " "
				+ getResult());
		logger.flush();
	}

	@Override
	public double getResult() {
		return ((double) clicks) / ((double) evaluations);
	}

	@Override
	public void evaluate(LogLine<Context, Action, Boolean> logLine,
			Action chosenAction) { 
		double reward=((GenericAction)chosenAction).getReward();	//reward of chosen action
		double regret=((GenericLogLine)logLine).getRegret((GenericAction)chosenAction);	//regret of chosen action
		try {
			// write results on csv output file
			csvOutput.write((number++)+"");
			csvOutput.write(reward+"");
			cumulativeReward+=reward;	//cumulative reward
			csvOutput.write((cumulativeReward)+"");
			csvOutput.write(regret+"");
			cumulativeRegret+=regret;	//cumulative regret: good to plot in matlab
			csvOutput.write((cumulativeRegret)+"");
			csvOutput.endRecord();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeCsv(){
		csvOutput.close();	//close the csv output file
	}

}
