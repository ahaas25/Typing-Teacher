import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.awt.*;

public class variables {
	public int fps = 5, lines = 3;
	public boolean debug = false;
	// Updating variables
	private double WPM, score;
	private String testString;
	private String[] testWord;
	private long startTime, endTime, elapsedTime;
	public int typedLines, testWords, drawLines, wordMode;
	public int wordModes[] = {5, 10, 25, 50, 100};
	public String userString;
	public String WPMText, ScoreText;
	public boolean gameRunning, testOver;
	public ArrayList<String> pressedKeys = new ArrayList();
	public ArrayList<String> words = new ArrayList();
	public ArrayList<String> drawStrings = new ArrayList();
	public ArrayList<String> userInputStrings = new ArrayList();
	
	// to do:
	// Finish test when last word is spelled right
	// calculate real WPM and actual WPM
	boolean repeatWords, timerRunning;
	Color typingPromptColors[], backgroundColor, textColor;
	Rectangle rOptions, rScore, rWPM, rTopBar;
	Random r = new Random();
	
	public variables() {
		loadSettings();
		loadWords();
		resetVars();
		generateNewTest();
		generateThemes();
	}
	
	public void resetVars() {
		userString = "";
		testString = "";
		typedLines = 0;
		generateNewTest();
		timerRunning = false;
		elapsedTime = -1;
		WPM = 0;
		score = 0;
		drawLines = lines;
		gameRunning = true;
		testOver = false;
	}
	
	public void loadSettings() {
		// This will load from a config file later.
		wordMode = 1;
		testWords = wordModes[wordMode];
		repeatWords = false;
	}
	
	/** 
	 * typingPromptColors
	 * 		0 = not typed
	 * 		1 = correctly typed
	 * 		2 = incorrectly typed
	 */
	public void generateThemes() {
		typingPromptColors = new Color[3];
		typingPromptColors[0] = new Color(128,128,128);
		typingPromptColors[1] = new Color(0,0,0);
		typingPromptColors[2] = new Color(255,128,128);
		
		backgroundColor = new Color(255,255,255);
		textColor = new Color(0,0,0);
	}
	
	public void loadWords() {
		try {
		      File myObj = new File("words.txt");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        words.add(data);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("Could not find words file");
		      e.printStackTrace();
		    }
	}
	
	/**
	 * Generates new test
	 * Called when program is first started and when user requests a new one.
	 */
	public void generateNewTest() {
		testWord = generateRandomWords();
		testString = "";
		for (String x : testWord)
			testString += x + " ";
		testString = backspace(testString);
	}
	
	/**
	 * Generates 'size' amount of random words 
	 * Can only draw the same word once
	 */
	public String[] generateRandomWords() {
		String[] toReturn = new String[testWords];
		ArrayList<Integer> usedWords = new ArrayList();
		int wordChoice = r.nextInt(words.size());
		
		if(!repeatWords) {
			for(int i = 0; i < testWords; i++) {
				while(usedWords.contains(wordChoice))
					wordChoice = r.nextInt(words.size());
				usedWords.add(wordChoice);
				toReturn[i] = words.get(wordChoice);
			}
		}
		else {
			for(int i = 0; i < testWords; i++) {
				wordChoice = r.nextInt(words.size());
				toReturn[i] = words.get(wordChoice);
			}
		}
		return toReturn;
	}
	
	/**
	 * Checks if user has completed test
	 * Called when user presses key
	 */
	public void checkTest() {
		String lastWordOfTest = testString.substring(testString.lastIndexOf(" ") + 1);
		String lastWordOfUser = userString.substring(userString.lastIndexOf(" ") + 1);
		if(userString.length() == testString.length() && lastWordOfTest.equals(lastWordOfUser)) {
			stopTimer();
			gameRunning = false;
			testOver = true;
		}
	}
	
	public String backspace(String str) {
	    if (str != null && str.length() > 0) {
	        str = str.substring(0, str.length() - 1);
	    }
	    return str;
	}
	
	/**
	 * Called when user presses key
	 * Updates game stats to prevent delayed info
	 */
	public void timingHandler() {
		if(!timerRunning) { // Starts test
			startTimer();
			timerRunning = true;
		}
		calculateGameStats();			
	}
	
	public void startTimer() {
		startTime = System.nanoTime();
	}
	
	public void stopTimer() {
		if(timerRunning)
			endTime = System.nanoTime();
		timerRunning = false;
		calculateGameStats();
	}
	
	public void calculateGameStats() {
		if(timerRunning)
			elapsedTime = (System.nanoTime() - startTime)/10000;
		else
			elapsedTime = (endTime - startTime)/10000;
		WPM = (double) userString.length() / 5 / elapsedTime * (60 / 0.00001);
		WPM = round(WPM, 2);
	}
	
	public double calculateAccuracy() {
		int counter = 0;
		for(int i = 0; i < testString.length(); i++) {
			if(testString.charAt(i) == userString.charAt(i))
				counter++;
		}
		double toReturn = round((double)(counter)/testString.length(), 3);
		return toReturn * 100;
	}
	
	public double round(double number, int places) {
		long shift = (long) Math.pow(10, places);
		double shifted = number * shift;
		shifted = Math.round(shifted);
		return (double) (shifted)/shift;
	}
		
	public void setTestSize(int x) {
		testWords = x;
	}

	public String getString() {
		return testString;
	}
	
	public String[] getTestWords() {
		return testWord;
	}
	
	public double getWPM() {
		return WPM;
	}
	
	public String generateElapsedTimeString() {
		double time = ((double) (elapsedTime / 1000))/100;
		return time + "s";
	}
	public Long getElapsedTime() {
		return elapsedTime;
	}
	
	/**
	 * Checks if a specified point is inside a rectangle
	 * @param r the rectangle
	 * @param p the point
	 */
	public boolean isInRect(Rectangle r, Point p) {
		return r.x <= p.x && r.x + r.width >= p.x
				&& r.y <= p.y && r.y + r.height >= p.y;
	}
	
}
