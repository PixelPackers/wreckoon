package Game;

import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Part {

	private World world;
	private float x;
	private float y;
	private Image image = new Image("images/part.png");

	private static Music music;
	
	public Part(World world, float x, float y) throws SlickException{
		
		this.world = world;
		this.x = x;
		this.y = y;		
		
		this.music = new Music("audio/Part 1_ Loop _Lang.wav");
	}
	
	public void collected(){

		music.play();
		
		// dissappear
		
		// show counter
		// make counter dissapear?
	}
	
	public void draw(){
		image.draw(this.x, -this.y);
//		image.draw(100, 100);
		
	}
	
}
