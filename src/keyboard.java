import java.util.ArrayList;

public class keyboard {
	
	public String engl[][] = {{"`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "backspace"},
					   {"tab", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\"},
					   {"caps lock", "a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'", "enter"},
					   {"shift", "z", "x", "c", "v", "b", "n", "m", ",", ".", "/", "shift"}};
	public Key[][] kb;
	private variables v;
	
	public keyboard(variables v) {
		this.v = v;
		generateEngl();
	}
	
	/**
	 * Creates a Jagged Array of keys
	 * This later will read from a text file which the user can edit
	 */
	public void generateEngl() {
		kb = new Key[engl.length][];
		for (int i = 0; i < engl.length; i++) {
			kb[i] = new Key[engl[i].length];
		}

		for (int y = 0; y < kb.length; y++) {
			for (int x = 0; x < kb[y].length; x++) {
				kb[y][x] = new Key(engl[y][x], v);
			}
		}
	}
}

class Key {
	public String key;
	public int keyID, dataPoints;
	public boolean specialChar;
	private variables v;
	// Backspace needs to count as a miss or some way of preventing
	// exploit
	// Need to make arraylist of arraylists for the word modes
	private ArrayList<ArrayList<Boolean>> correctStrikes = new ArrayList<>();
	private ArrayList<ArrayList<Double>> timesToStrike = new ArrayList<>();
	private ArrayList<Boolean> correctStrike = new ArrayList<>();
	private ArrayList<Double> timeToStrike = new ArrayList<>();	
	int correctStrikeCounter, timeToStrikeCounter;
	
	public Key(String text, variables v) {
		key = text;
		this.v = v;
		specialChar = text.length() != 1;
		dataPoints = 100;
		// Correct strike will be changed based on gamemode
		for(int i = 0; i < v.wordModes.length; i++) {
			correctStrikes.add(new ArrayList<Boolean>());
			timesToStrike.add(new ArrayList<Double>());
		}
		// Initalize arraylists here
	}
	
	/**
	 * @param correctStrike
	 * @param timeToStrike
	 * 
	 * Adds data to ArrayList. Checks if ArrayList is at max
	 * capacity. If so, it rolls over back to zero.
	 */
	public void update(boolean correctStrike, double timeToStrike) {
		this.correctStrike = correctStrikes.get(v.wordMode);
		this.timeToStrike = timesToStrike.get(v.wordMode);
		timeToStrike = timeToStrike / 100000; // 10^5 Converts to seconds
		if(correctStrikeCounter == dataPoints)
			correctStrikeCounter = 0;
		if(timeToStrikeCounter == dataPoints)
			timeToStrikeCounter = 0;
		
		if(this.correctStrike.size() >= dataPoints)
			this.correctStrike.set(correctStrikeCounter, correctStrike);
		else
			this.correctStrike.add(correctStrike);
			
		if(this.timeToStrike.size() >= dataPoints)
			this.timeToStrike.set(timeToStrikeCounter, timeToStrike);
		else
			this.timeToStrike.add(timeToStrike);
		correctStrikeCounter++;
		timeToStrikeCounter++;
		correctStrikes.set(v.wordMode, this.correctStrike);
		timesToStrike.set(v.wordMode, this.timeToStrike);
	}
	
	public void update(boolean correctStrike) {
		this.correctStrike = correctStrikes.get(v.wordMode);
		if(correctStrikeCounter == dataPoints)
			correctStrikeCounter = 0;
		
		if(this.correctStrike.size() >= dataPoints)
			this.correctStrike.set(correctStrikeCounter, correctStrike);
		else
			this.correctStrike.add(correctStrike);

		correctStrikeCounter++;	
		correctStrikes.set(v.wordMode, this.correctStrike);
	}
	
	public String[] getStats(language l) {
		correctStrike = correctStrikes.get(v.wordMode);
		timeToStrike = timesToStrike.get(v.wordMode);
		double counter = 0, accuracy = 100, averageTime = 0;
		for(boolean temp : correctStrike) {
			counter += temp ? 1 : 0;
		}
		if(correctStrike.size() > 0) {
			accuracy = counter / correctStrike.size() * 100;
			accuracy = v.round(accuracy, 2);
		}
		
		counter = 0;
		for(double temp: timeToStrike) {
			counter += temp;
		}
		if(timeToStrike.size() > 0) {
			averageTime = counter / timeToStrike.size() * 100;
			averageTime = v.round(averageTime, 2);
		}
		
		String[] toReturn = {l.keyStats + " - " + v.wordModes[v.wordMode] + " Words - " + key, l.accuracy + ": " + accuracy + "%", l.averageTime + ": " + averageTime + "ms", l.sampleSize + ": " + correctStrike.size()};
		return toReturn;
	}

	public ArrayList<Boolean> getCorrectStrike() {
		return correctStrike;
	}

	public void setCorrectStrike(ArrayList<Boolean> correctStrike) {
		if(correctStrike.size() == 100) {
			correctStrikeCounter = 0;

		}
		else {
			correctStrikeCounter = correctStrike.size();
		}
		this.correctStrike = correctStrike;
	}

	public ArrayList<Double> getTimeToStrike() {
		return timeToStrike;
	}

	public void setTimeToStrike(ArrayList<Double> timeToStrike) {
		if(timeToStrike.size() == 100) {
			timeToStrikeCounter = 0;

		}
		else {
			timeToStrikeCounter = timeToStrike.size();
		}
		this.timeToStrike = timeToStrike;
	}
	
	
	
	// Need getters and setters for the two arraylists
}
