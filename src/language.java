
public class language {
	public String score, words, testOver, wpm, raw, accuracy, time, keyStats,
		averageTime, sampleSize;
	
	// This will later load from a text file so uses can change languages
	public language() {
		loadLanguage();
	}
	
	private void loadLanguage() {
		loadEnglish();
	}
	
	// Temporary method
	private void loadEnglish() {
		score = "Score";
		words = " Words";
		testOver = "Test Complete!";
		wpm = "WPM";
		raw = " (raw)";
		accuracy = "Accuracy";
		time = "Time";
		keyStats = "Key Stats";
		averageTime = "Average Time";
		sampleSize = "Sample Size";
	}
}
