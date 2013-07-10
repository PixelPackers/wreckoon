package Game;

import java.util.ArrayList;

public class Textbox {
	
	private class Text {
		
		private String	text;
		
		public String getText() {
			return text;
		}
		
		public void setText(String text) {
			this.text = text;
		}
		
		public int getCamFocus() {
			return camFocus;
		}
		
		public void setCamFocus(int camFocus) {
			this.camFocus = camFocus;
		}
		
		private int	camFocus;
		
		// 0 - Ray
		
		public Text(String text, int camFocus) {
			this.text = text;
			this.camFocus = camFocus;
		}
		
	}
	
	private static ArrayList<ArrayList<Text>>	texts			= new ArrayList<ArrayList<Text>>();
	
	private int									currentTextBox	= -1;
	private int									currentText		= -1;
	
	public Textbox() {
		
		ArrayList<Text> tmp;
		
		// dialog 0
		tmp = new ArrayList<Text>();
		tmp.add(new Text("Wtf?! What's that noise?!", 0));
		tmp.add(new Text("Have I really got to get up now?", 0));
		tmp.add(new Text("Well, better check where it came from...", 0));
		texts.add(tmp);
		
		// dialog 1
		tmp = new ArrayList<Text>();
		tmp.add(new Text("Boaris?!!", 1));
		tmp.add(new Text("What the hell are you doing here?", 0));
		tmp.add(new Text("Where are my parents?", 0));
		
		tmp.add(new Text("Well if it isn't little Ray...", 1));
		tmp.add(new Text("Your parents?", 1));
		tmp.add(new Text("They refused to cooperate with the money laundering,", 1));
		tmp.add(new Text("AGAIN.", 1));
		tmp.add(new Text("They had it coming.", 1));
		
		tmp.add(new Text("Don't you dare lay a single finger on them...", 0));
		tmp.add(new Text("Now tell me where they are!!!", 0));
		
		tmp.add(new Text("Too late, bud.", 1));
		tmp.add(new Text("But I guess you'll find that out soon enough.", 1));
		tmp.add(new Text("Farewell! Hahaha...", 1));
		
		tmp.add(new Text("MOM?! DAD?!", 2));
		tmp.add(new Text("NOOOOOOOOO!", 0));
		
		tmp.add(new Text("I promise, I will seek bloody revenge on him and his hideous minions!", 0));
		tmp.add(new Text("Dad, I will finish building the DIVINE STEAMER, your life's work,...", 0));
		tmp.add(new Text("...and crush Boaris with all my wrath!!!", 0));
		
		texts.add(tmp);
		
		// dialog 2
		tmp = new ArrayList<Text>();
		tmp.add(new Text("Dad's plan for the DIVINE STEAMER!", 0));
		tmp.add(new Text("Wait!", 0));
		tmp.add(new Text("I've seen this somewhere.", 0));
		tmp.add(new Text("...in the attic!", 3));
		texts.add(tmp);
		
		// dialog 3
		tmp = new ArrayList<Text>();
		tmp.add(new Text("I was right! There it is.", 0));
		tmp.add(new Text("Let's go find the other parts!", 0));
		texts.add(tmp);
		
	}
	
	public String getText() {
		if (currentTextBox < texts.size() && currentText < texts.get(currentTextBox).size())
			return texts.get(currentTextBox).get(currentText).getText();
		return null;
	}
	
	public void nextText() {
		++currentText;
	}
	
	public boolean hasNext() {
		return currentText < texts.get(currentTextBox).size() - 1;
	}
	
	public void setDialog(int i) {
		currentTextBox = i;
		currentText = 0;
	}

	public int getCamFocus() {
		return texts.get(currentTextBox).get(currentText).getCamFocus();
	}
	
}
