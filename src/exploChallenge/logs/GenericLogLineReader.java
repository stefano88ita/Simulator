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


public class GenericLogLineReader implements
		LogLineReader<GenericVisitor, GenericArticle, Boolean> {

	private static final int indexSize = 2;
	private static final int noRewardValue=0;
	private Scanner scan;
	private int nbOfVisitorFeatures;
	private int nbOfArticleFeatures;
	private ArrayList<GenericArticle> possibleActions;
	private int currentFile;
	private int lastFile;
	private String filePrefix;
  
	private void buildNextScanner() throws FileNotFoundException {
		this.currentFile++;
		String file = new Integer(currentFile) + "";
		while (file.length() < indexSize)
			file = "0" + file;
		file = filePrefix + file;
		scan = new Scanner(new File(file));
		scan.useDelimiter("\n");
	}
	
	public GenericLogLineReader(String filePath,
			int nbOfVisitorFeatures, int nbOfArticleFeatures) throws FileNotFoundException {
		this.nbOfVisitorFeatures = nbOfVisitorFeatures;
		this.nbOfArticleFeatures = nbOfArticleFeatures;
		this.scan = new Scanner(new File(filePath));
		this.scan.useDelimiter("\n");
		this.currentFile = 0;
		this.lastFile = 0;
	}

	private byte[] FillArrayFromPositions(String positions, byte[] arrayToFill){
		Scanner features = new Scanner(positions);
		features.useDelimiter(" ");
		while(features.hasNext()){
			arrayToFill[Integer.parseInt(features.next())-1]=1;
		}
		return arrayToFill;
	}
	
	@Override
	public LogLine<GenericVisitor, GenericArticle, Boolean> read()
			throws IOException {
		if(! hasNext())
			throw new IOException("no next line to read");
		GenericVisitor user = null;
		possibleActions = new ArrayList<GenericArticle>();
		String timestamp = "";
		String line = scan.next();
		Scanner tokens = new Scanner(line);
		tokens.useDelimiter(",");
		while(tokens.hasNext()){
			String token = tokens.next();
			Scanner scanLine =new Scanner(token);
			scanLine.useDelimiter("#");
			String type=scanLine.next();
			String value=scanLine.next();
			if(type.equals("t")){ 
				timestamp=value;
			}
			if(type.equals("u")){ 
				Scanner scanId =new Scanner(value);
				scanId.useDelimiter(">");
				String id = scanId.next();
				value = scanId.next();
				byte[] userFeatures = ArrayHelper.newZeroByteArray(nbOfVisitorFeatures);
				userFeatures = FillArrayFromPositions(value, userFeatures);
				user = new GenericVisitor(Long.parseLong(id), userFeatures);
			}
			if(type.equals("a")){ 
				Scanner scanId =new Scanner(value);
				scanId.useDelimiter(">");
				String id = scanId.next();
				value = scanId.next();
				Scanner scanReward =new Scanner(value);
				scanReward.useDelimiter(":");
				value=scanReward.next();
				String reward=scanReward.next();
				byte[] actionFeatures = ArrayHelper.newZeroByteArray(nbOfArticleFeatures);
				actionFeatures = FillArrayFromPositions(value, actionFeatures);
				double r;
				try{
					r=Double.parseDouble(reward);
				}catch(Exception e){
					r=noRewardValue;
				}
				
				possibleActions.add(new GenericArticle(Integer.parseInt(id), actionFeatures, r));	
			}
		}
		GenericLogLine logLine = new GenericLogLine(user, possibleActions);
		return logLine;
	}
	
	@Override
	public boolean hasNext() throws IOException {
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
	public List<GenericArticle> getPossibleActions() {
		return possibleActions;
	}
}
