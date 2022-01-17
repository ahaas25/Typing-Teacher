import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class gameWindow implements KeyListener, WindowListener, MouseListener, MouseMotionListener {
	private JFrame mainFrame;
	private variables v = new variables();
	private keyboard k = new keyboard(v);
	private language l = new language();
	mySurface s;

	/**
	 * Initializes window and starts the graphics runner.
	 * Graphics runner runs @v.fps times a second. 
	 */
	public gameWindow (int x, int y, String name) {
		mainFrame = new JFrame(name);
		mainFrame.setSize(x, y);
		mainFrame.addKeyListener(this);
		mainFrame.addWindowListener(this);
		mainFrame.getContentPane().addMouseListener(this);
		mainFrame.getContentPane().addMouseMotionListener(this);
		mainFrame.setFocusTraversalKeysEnabled(false);
		s = new mySurface();
		mainFrame.add(s);
		mainFrame.setVisible(true);
		graphicsRunner();
	}
	
	/**
	 * Redraws the window @v.fps times a second
	 */
	public void graphicsRunner() {
		while(true) {
			try {
				Thread.sleep(1000/v.fps);
				s.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}
	
	protected class mySurface extends JPanel {
		double topBarHeight = 0.075;
		double textHeight = 0.075;
		int UIWidthArea = (int) (getWidth() * 0.8);
		int UIHeight = (int) (getHeight() * 0.025);
		int UITopBarHeightArea = (int) (getHeight() * topBarHeight);
		int UIHeightArea = (int) (getHeight() * 0.9 * 0.5);
		
		/**
		 * Main drawing method
		 * Calls UI drawing methods
		 */
		private void draw(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(
	            RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			drawUI(g);
			drawKeyboard(g);
		}
		
		/**
		 * Draws the UI for typing teacher.
		 */
		private void drawUI(Graphics g) {
			Font wordsFont = new Font("Arial", Font.PLAIN, 24);
			double topBarHeight = 0.075;
			double textHeight = 0.075;
			int UIWidthArea = (int) (getWidth() * 0.8);
			int UIHeight = (int) (getHeight() * 0.025);
			int UITopBarHeightArea = (int) (getHeight() * topBarHeight);
			int UIHeightArea = (int) (getHeight() * 0.9 * 0.5);
			
			v.rOptions = new Rectangle(UIWidthArea, UIHeight, (int) 
					(getWidth() * 0.15), UITopBarHeightArea);
			v.rScore = new Rectangle((int) (getWidth() * 0.05), 
					UIHeight, (int) (getWidth() * 0.15), UITopBarHeightArea);
			v.rWPM = new Rectangle((int) 
					((UIWidthArea - getWidth() * 0.15)/2 + getWidth() * 0.1), 
					UIHeight, (int) (getWidth() * 0.15), UITopBarHeightArea);
			v.rTopBar = new Rectangle((int) (getWidth() * 0.05), 
					UIHeight, (int) (getWidth() * 0.9), UITopBarHeightArea);
			
			generateTestStrings(g, wordsFont, (int) (getWidth() * 0.8));
			Rectangle lines[] = new Rectangle[v.drawLines];
			
			
			v.WPMText = l.wpm + ": " + v.getWPM();
			v.ScoreText = l.score + ": " + 0;
			
	        g.setColor(v.backgroundColor);
	        g.fillRect(0,0, getWidth(), getHeight());
	        
	        g.setColor(v.UIColor);
			
			// Draw debug rectangles
			if(v.debug) {
				g.drawRect((int) v.rWPM.getX(), (int) v.rWPM.getY(), 
						(int) v.rWPM.getWidth(), (int) v.rWPM.getHeight());
				g.drawRect((int) v.rScore.getX(), (int) v.rScore.getY(), 
						(int) v.rScore.getWidth(), (int) v.rScore.getHeight());
				g.drawRect((int) v.rOptions.getX(), (int) v.rOptions.getY(), 
						(int) v.rOptions.getWidth(), (int) v.rOptions.getHeight());
			}
			
			g.drawRoundRect(v.rTopBar.x, v.rTopBar.y, v.rTopBar.width, 
					v.rTopBar.height, 20, 20);
			g.drawRoundRect((int) (getWidth() * 0.05), 
					(int) (getHeight() * 0.025), (int) (getWidth() * 0.90), 
					UIHeightArea, 20, 20);
			
			g.setColor(v.textColor);
			drawCenteredString(g, v.WPMText, v.rWPM, wordsFont);
			drawCenteredString(g, v.ScoreText, v.rScore, wordsFont);
			drawCenteredString(g, v.testWords + l.words, v.rOptions, wordsFont);
			
			int textSizeY = (int) (textHeight * getHeight() * lines.length);
			
			double topOfBarPadding = getHeight() * 0.025;
			double topBarPadding = getHeight() * 0.025 + getHeight() * topBarHeight;
			
			int yPadding = (int) ((getHeight() * 0.35) - textSizeY)/2;
			
			generateTestStrings(g, wordsFont, UIWidthArea);
			generateUserInputStrings();
			
			int k = 0;
			int middle = v.drawLines/2;
			while(v.typedLines - middle - k > 0 && v.drawStrings.size() > k 
					+ v.drawLines)
				k++;
			
			for(int i = 0; i < lines.length; i++) {
				lines[i] = new Rectangle((int) (getWidth() * 0.1), 
						(int) (topOfBarPadding/2 + topBarPadding + yPadding 
								+ textHeight * getHeight() * i), 
						(int) (getWidth() * 0.8), (int) (getHeight() * textHeight));
				drawTypingPrompt(g, v.drawStrings.get(i+k), 
						v.userInputStrings.get(i+k), lines[i], wordsFont);
			}
			
			if(v.testOver) {
				v.calculateEndGameStats();
				String[] endStats = {l.testOver, l.wpm + ": " + v.getRealWPM(), 
						l.wpm + l.raw + ": " + v.getWPM(), l.accuracy + ": " 
						+ v.getAccuracy()
				+ "%", l.time + ": " + v.generateElapsedTimeString()};
				drawStatsWindow(g, endStats, wordsFont);				
			}
		}
		
		private void drawStatsWindow(Graphics g, String[] s, Font f) {
			double topBarHeight = 0.075;
			int UIHeight = (int) (getHeight() * 0.025);
			int UITopBarHeightArea = (int) (getHeight() * topBarHeight);
			int UIHeightArea = (int) (getHeight() * 0.9 * 0.5);
			Rectangle rStatsWindow = new Rectangle((int) (getWidth() * 0.25), 
					UIHeight + UITopBarHeightArea * 2, 
					(int) (getWidth() * 0.5), (int) (UIHeightArea * 0.5));
			
			Rectangle[] textLines = new Rectangle[s.length];
			
			g.setColor(v.backgroundColor);
			g.fillRoundRect(rStatsWindow.x, rStatsWindow.y, rStatsWindow.width, 
					rStatsWindow.height, 25, 25);
			g.setColor(v.textColor);
			int counter = (int) (rStatsWindow.getY() / (textLines.length - 1));
			for(int i = 0; i < textLines.length; i++) {
				textLines[i] = new Rectangle((int) rStatsWindow.getX(), 
						(int) rStatsWindow.getY() + counter * i, 
						(int) rStatsWindow.getWidth(), counter);
				drawCenteredString(g, s[i], textLines[i], f);
			}
			g.setColor(v.UIColor);
			g.drawRoundRect(rStatsWindow.x, rStatsWindow.y, rStatsWindow.width, 
					rStatsWindow.height, 25, 25);
			g.drawRoundRect(textLines[0].x, textLines[0].y, textLines[0].width, textLines[0].height, 20, 20);
		}
		
		/**
		 * Takes the user's input and converts it into strings the length of
		 * the test lines.
		 * Example:
		 * v.drawStrings: [window enter noon star, believe expect wash, chair city tube]
		 * User input: "window enter noon staa bnelieve expect was charity t tube"
		 * Output: [window enter noon staa, bnelieve expect was,  charity t tube]
		 */
		private void generateUserInputStrings() {
			v.userInputStrings.clear();
			int position = 0;
			v.typedLines = 0;
			for(int i = 0; i < v.drawStrings.size(); i++) {
				if(v.userString.length() - position <= v.drawStrings.get(i).length()) {// If userString is not as long as line						
					v.userInputStrings.add(v.userString.substring(position, 
							v.userString.length()));
					i = v.drawStrings.size();
					}
				else { // If user completed string
					v.userInputStrings.add(v.userString.substring(position, 
							position + v.drawStrings.get(i).length()));
					position += v.drawStrings.get(i).length() + 1; // + 1 For space
					v.typedLines++;
				}
			}
			while(v.userInputStrings.size() < v.drawStrings.size()) {
				v.userInputStrings.add("");
			}
		}
		
		/**
		 * Calculates how many words can be displayed on a line and adds them
		 */
		private void generateTestStrings(Graphics g, Font font, int lineLength) {
			String temp[] = v.getTestWords();
			String toAdd = "";
			FontMetrics metrics = g.getFontMetrics(font);
			v.drawStrings.clear();
			int length = 0;
			for(String x : temp) {
				int textLength = metrics.stringWidth(" " + x);
				if(length + textLength < lineLength) {
					length += textLength;
					toAdd += x + " ";
				}
				else {
					toAdd = v.backspace(toAdd);
					v.drawStrings.add(toAdd);
					length = textLength;
					toAdd = x + " ";
				}
			}
			toAdd = v.backspace(toAdd);
			v.drawStrings.add(toAdd);
			
			if (v.drawStrings.size() < v.lines)
				v.drawLines = v.drawStrings.size();
			else
				v.drawLines = v.lines;
		}
		
		/**
		 * Draws the on-screen keyboard and displays which keys the user is
		 * pressing.
		 */
		private void drawKeyboard(Graphics g) {
			int keySizeX = getWidth()/16;
			int keySizeY = getHeight()/12;
			Font keyboardFont = new Font("Dialog", Font.PLAIN, 24);
			Graphics gr = (Graphics2D) g;
			gr.setColor(v.keyColor);		
			Key keyboard[][] = k.kb;
			
			for(int y = 0; y < keyboard.length; y++) {
				for(int x = 0; x < keyboard[y].length; x++) {				
					int xLength = keySizeX * keyboard[y].length; // Calculates how many pixels each row will take
					int leftPadding = (getWidth() - xLength)/2;
					int yLength = keySizeY * keyboard.length;
					int yPadding = (getHeight() - yLength);
					Key key = keyboard[y][x];
					Rectangle keyRect = new Rectangle(leftPadding + x*keySizeX, yPadding + (y-1)
							*keySizeY, keySizeX, keySizeY);
					
					boolean specialChar = keyboard[y][x].specialChar;
					// Font size will be calculated by window size later.
					if(!specialChar)
						keyboardFont = new Font("Dialog", Font.PLAIN, 24);
					else
						keyboardFont = new Font("Dialog", Font.PLAIN, 9);
				
					gr.setColor(v.translucentKeyColor);
					gr.drawRoundRect(keyRect.x, keyRect.y, keyRect.width, keyRect.height, 25, 25);
					if (v.pressedKeys.contains(key.key)){
						gr.fillRoundRect(leftPadding + x*keySizeX, yPadding + (y-1)
								*keySizeY, keySizeX, keySizeY, 25, 25);
					}
					
					// Checks if game is running
					if (!v.timerRunning) {
						if(v.isInRect(keyRect, v.mouseLocation) && !specialChar) {
							String[] keyStats = key.getStats(l);
							drawStatsWindow(g, keyStats, keyboardFont);
							gr.setColor(v.translucentKeyColor);
							gr.fillRoundRect(leftPadding + x*keySizeX, yPadding + (y-1)
									*keySizeY, keySizeX, keySizeY, 25, 25);
						}
					}
					gr.setColor(v.keyColor);
					drawCenteredString(gr, key.key, keyRect, keyboardFont);
				}
			}
		}
		
		/**
		 * Draws a centered string.
		 * Credit: https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
		 */
		public void drawCenteredString(Graphics gr, String text, int x, int y, 
				int xHeight, int yHeight, Font font) {
		    // Get the FontMetrics
		    FontMetrics metrics = gr.getFontMetrics(font);
		    // Determine the X coordinate for the text
		    int drawx = x + (xHeight - metrics.stringWidth(text)) / 2;
		    // Determine the Y coordinate for the text (note we add the ascent, 
		    // as in java 2d 0 is top of the screen)
		    int drawy = y + ((yHeight - metrics.getHeight()) / 2) + metrics.getAscent();
		    // Set the font
		    gr.setFont(font);
		    // Draw the String
		    gr.drawString(text, drawx, drawy);
		}
		
		/** 
		 * Draws a centered string.
		 */
		public void drawCenteredString(Graphics g, String text, 
				Rectangle rect, Font font) {
		    // Get the FontMetrics
		    FontMetrics metrics = g.getFontMetrics(font);
		    // Determine the X coordinate for the text
		    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
		    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		    // Set the font
		    g.setFont(font);
		    // Draw the String
		    g.drawString(text, x, y);
		}
		/**
		 * Draws the typing prompt text
		 * Aligns text left and in the middle (y)
		 * Sets the color of each letter according to its status
		 */
		public void drawTypingPrompt(Graphics g, String testText, 
			String userText, Rectangle rect, Font font) {
		    FontMetrics metrics = g.getFontMetrics(font);
		    int x = rect.x;
		    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) 
		    		+ metrics.getAscent();
		    g.setFont(font);
		    g.drawString(testText, x, y);
		    
		    char c = ' ';
	        for (int i = 0; i < testText.length(); i++) {
	        	if(userText.length() <= i) // User hasn't typed
	        		g.setColor(v.typingPromptColors[0]);
	        	else if (userText.charAt(i) == testText.charAt(i))
	        		g.setColor(v.typingPromptColors[1]);
	        	else
	        		g.setColor(v.typingPromptColors[2]);
	            c = testText.charAt(i);
	            int width = g.getFontMetrics(font).charWidth(c);
	            g.drawString("" + c, x, y);
	            x += width;
	        }
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			draw(g);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	/**
	 * Handles user key presses
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {  
	    	v.userString = v.backspace(v.userString);
	    }
	    else if (e.getKeyCode() == KeyEvent.VK_TAB) {
	    	v.resetVars();
	    }
	    else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	    	mainFrame.dispose();
	        System.exit(0);
	    }
	    else if (e.getKeyChar() != '\uFFFF' && v.gameRunning){
	    	v.userString += e.getKeyChar();
	    	v.timingHandler();
	    	checkKey(e.getKeyChar());
	    }
	    String pressedKey = "" + e.getKeyChar();
	    if(!v.pressedKeys.contains(pressedKey)) {
	    	v.pressedKeys.add(pressedKey.toLowerCase());
	    }
	    
	    v.checkTest();
	    
	    // Refreshes keyboard graphics to reduce visual input delay
	    refreshGraphics();
	}
	
	/**
	 * Checks if the pressed key is the correct key for test stats.
	 */
	public void checkKey(char input) {
		int x = 0, y = 0;
		char correctKey = v.getString().charAt(0);
		if(v.userString.length() > 0 && v.userString.length()
				<= v.getString().length())
			correctKey = v.getString().charAt(v.userString.length() - 1);

		// Finds the location of the correct key in the KB array
		for(int i = 0; i < k.kb.length; i++) {
			for(int k = 0; k < this.k.kb[i].length; k++) {
				if(!this.k.kb[i][k].specialChar && this.k.kb[i][k].key.charAt(0) == correctKey) {
					x = k; y = i;
				}
			}
		}
		
		// Checks if user pressed the correct key
		if(v.userString.length() <= v.getString().length()) {
			boolean isCorrect = correctKey == input;
			if(isCorrect) {
				if(v.keyLastPressed == -1) { // If its the first key of the test
					k.kb[y][x].update(isCorrect);
				}
				else {
					Long elapsedTime = (System.nanoTime() - v.keyLastPressed)/10000;
					k.kb[y][x].update(isCorrect, elapsedTime);
				}
				v.keyLastPressed = System.nanoTime();				
			}
			else { // updates key as being incorrectly pressed
				k.kb[y][x].update(isCorrect);
			}
		}
	}
	/**
	 * Handles key released for keyboard graphics
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		String depressedKey = "" + e.getKeyChar();
		v.pressedKeys.remove(depressedKey.toLowerCase());
		refreshGraphics();
	}
	
	private void refreshGraphics() {
		s.repaint();
	}

	public void windowClosing(WindowEvent e) {
        mainFrame.dispose();
        System.exit(0);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(v.isInRect(v.rOptions, e.getPoint())) {
			if(v.wordMode + 1 >= v.wordModes.length) {
				v.wordMode = 0;
				v.testWords = v.wordModes[0];
			}
			else {
				v.wordMode++;
				v.testWords = v.wordModes[v.wordMode];
			}
			v.resetVars();
		}
		else if (v.isInRect(v.rWPM, e.getPoint())) {
			v.darkMode = !v.darkMode;
			v.generateThemes();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		v.mouseLocation = e.getPoint();
	}
	
}
