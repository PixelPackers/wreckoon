package Game;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Images {
	
	private static final Images	instance	= new Images();
	
	public static Images getInstance() {
		return instance;
	}
	
	private HashMap<String, Image>			images			= new HashMap<String, Image>();
	
	private HashMap<String, SpriteSheet>	spriteSheets	= new HashMap<String, SpriteSheet>();
	
	private Images() {
		
	}
	
	public Image getImage(String path) throws SlickException {
		if (!images.containsKey(path)) {
			images.put(path, new Image(path));
		}
		return images.get(path);
	}
	
	public SpriteSheet getSpriteSheet(String path, int width, int height) throws SlickException {
		if (!spriteSheets.containsKey(path)) {
			spriteSheets.put(path, new SpriteSheet(path, width, height));
		}
		return spriteSheets.get(path);
	}
	
}
