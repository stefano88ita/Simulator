package exploChallenge.logs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import exploChallenge.logs.LogLine;
import exploChallenge.logs.LogLineReader;
import exploChallenge.utils.ArrayHelper;


public class GenericLogLineReader implements LogLineReader<GenericVisitor, GenericAction, Boolean> {
	
	//init
	private static final int indexSize = 2;
	private static final int noRewardValue=0;
	private Scanner scan;
	private int nbOfVisitorFeatures;
	private int nbOfArticleFeatures;
	private ArrayList<GenericAction> possibleActions;
	private int currentFile;
	private int lastFile;
	private String filePrefix;
  
	private void buildNextScanner() throws FileNotFoundException {
		//build a scanner for a line
		this.currentFile++;
		String file = new Integer(currentFile) + "";
		while (file.length() < indexSize)
			file = "0" + file;
		file = filePrefix + file;
		scan = new Scanner(new File(file));
		scan.useDelimiter("\n");
	}
	
	public GenericLogLineReader(String filePath, int nbOfVisitorFeatures, int nbOfArticleFeatures) throws FileNotFoundException {
		//constructor
		this.nbOfVisitorFeatures = nbOfVisitorFeatures;
		this.nbOfArticleFeatures = nbOfArticleFeatures;
		this.scan = new Scanner(new File(filePath));
		this.scan.useDelimiter("\n");
		this.currentFile = 0;
		this.lastFile = 0;
	}

	
	@Override
	public LogLine<GenericVisitor, GenericAction, Boolean> read() throws IOException {
		//parse a line
		if(! hasNext())
			throw new IOException("no next line to read");	//exception if no more line to read
		GenericVisitor user = null;
		possibleActions = new ArrayList<GenericAction>();
		String timestamp = "";
		String line = scan.next();
		Scanner tokens = new Scanner(line);
		tokens.useDelimiter(",");
		int actionParsed=0;
		int actionWithNoReward=0;
		while(tokens.hasNext()){	//using a string tokenizer
			String token = tokens.next();
			Scanner scanLine =new Scanner(token);
			scanLine.useDelimiter("#");
			String type=scanLine.next();
			String value=scanLine.next();
			if(type.equals("t")){ 
				//parsing the timestamp of dataset log line
				timestamp=value;
			}
			if(type.equals("u")){ 
				//parsing user id, and features vector of the user in the current log line
				try{
				Scanner scanId =new Scanner(value);
				scanId.useDelimiter(">");
				String id = scanId.next();
				value = scanId.next();
				double[] userFeatures = new double[nbOfVisitorFeatures];
				Scanner scanFeatures=new Scanner(value);
				scanFeatures.useDelimiter(" ");
				while(scanFeatures.hasNext()){
					String featurePositionAndValue=scanFeatures.next();
					Scanner positionAndValueScanner = new Scanner(featurePositionAndValue);
					positionAndValueScanner.useDelimiter(":");
					int featureIndex = Integer.parseInt(positionAndValueScanner.next());
					double featureValue= Double.parseDouble(positionAndValueScanner.next());
					userFeatures[featureIndex]=featureValue;
				}
				user = new GenericVisitor(Long.parseLong(id), userFeatures);
				}catch(Exception e){
					user = new GenericVisitor(Long.parseLong(value), null);
				}
			}
			if(type.equals("a")){ 
				actionParsed++;
				//parsing an action's id, features and reward
				Scanner scanId =new Scanner(value);
				scanId.useDelimiter(">");
				String id = scanId.next();
				value = scanId.next();
				String reward=scanId.next();
				double[] actionFeatures = new double[nbOfArticleFeatures];
				Scanner scanFeatures=new Scanner(value);
				scanFeatures.useDelimiter(" ");
				while(scanFeatures.hasNext()){
					String featurePositionAndValue=scanFeatures.next();
					Scanner positionAndValueScanner = new Scanner(featurePositionAndValue);
					positionAndValueScanner.useDelimiter(":");
					int featureIndex = Integer.parseInt(positionAndValueScanner.next());
					double featureValue= Double.parseDouble(positionAndValueScanner.next());
					actionFeatures[featureIndex]=featureValue;
				}
				double r;
				try{
					r=Double.parseDouble(reward);
				}catch(Exception e){
					r=noRewardValue;
					actionWithNoReward++;
				}
				possibleActions.add(new GenericAction(Integer.parseInt(id), actionFeatures, r));	
			}
		}
		if(actionParsed==actionWithNoReward){
			return null; //if there are no action with reward skip the line
		}
		GenericLogLine logLine = new GenericLogLine(user, possibleActions);
		return logLine;
	}
	
	@Override
	public boolean hasNext() throws IOException {
		//return true if there's line to read
		if (scan.hasNext())
			return true;
		if (currentFile == lastFile)
			return false;
		buildNextScanner();
		return hasNext();
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public List<GenericAction> getPossibleActions() {
		//get possible actions for a timestamp
		return possibleActions;
	}
}
