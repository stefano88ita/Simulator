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
		this.outputFile="Results/"+filename;
		try {
			if (!new File(outputFile).exists())
			{
				csvOutput = new CsvWriter(new FileWriter(outputFile, true), ';');
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
		double reward=((GenericAction)chosenAction).getReward();
		double regret=((GenericLogLine)logLine).getRegret((GenericAction)chosenAction);
		try {
			// write out a few records
			csvOutput.write((number++)+"");
			csvOutput.write(reward+"");
			cumulativeReward+=reward;
			csvOutput.write((cumulativeReward)+"");
			csvOutput.write(regret+"");
			cumulativeRegret+=regret;
			csvOutput.write((cumulativeRegret)+"");
			csvOutput.endRecord();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeCsv(){
		csvOutput.close();
	}

}
