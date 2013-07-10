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
		
		ArrayList<Text> tmp = new ArrayList<Text>();
		
		// dialog 0
		tmp.add(new Text("Hallo Boaris!", 0));
		tmp.add(new Text("Hi, Ray!", 1));
		tmp.add(new Text("Git sucks!", 0));
		tmp.add(new Text("I know!", 1));
		texts.add(tmp);
		
	}
	
	public String getText() {
		if (currentTextBox < texts.size() && currentText < texts.get(0).size())
			return texts.get(currentTextBox).get(currentText).getText();
		return null;
	}
	
	public void nextText() {
		++currentText;
	}
	
	public boolean hasNext() {
		return currentText < texts.get(0).size() - 1;
	}
	
	public void setDialog(int i) {
		currentTextBox = i;
		currentText = 0;
	}
	
}
