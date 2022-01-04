
public class keyboard {
	
	public String engl[][] = {{"`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "backspace"},
					   {"tab", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\"},
					   {"caps lock", "a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'", "enter"},
					   {"shift", "z", "x", "c", "v", "b", "n", "m", ",", ".", "/", "shift"}};
	public key[][] kb;
	
	public keyboard() {
		generateEngl();
	}
	
	/**
	 * Creates a Jagged Array of keys
	 */
	public void generateEngl() {
		kb = new key[engl.length][];
		for (int i = 0; i < engl.length; i++) {
			kb[i] = new key[engl[i].length];
		}
		
		for (int y = 0; y < kb.length; y++) {
			for (int x = 0; x < kb[y].length; x++) {
				kb[y][x] = new key(engl[y][x]);
			}
		}
	}
}

class key {
	public String key;
	public int keyID;
	public boolean specialChar;
	
	public key(String text) {
		key = text;
		specialChar = text.length() == 1;
	}
}
