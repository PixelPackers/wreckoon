package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Shred extends DropItem {
	
	private float pigSizeFactor;
		
	public Shred(Game game, World world, Vec2 pos, String imgPath, float pigSizeFactor)
			throws SlickException {
		super(game, world, pos, imgPath);
		
		this.pigSizeFactor = pigSizeFactor;
		MIN_TIME = 115;
		MAX_TIME = 115;
		
		DRAW_FACTOR = 0.17f * pigSizeFactor;
	}
	
	public void collect(){
	}
	
	
	@Override
	public void update() {
		super.update();
	}
}
