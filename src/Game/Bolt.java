package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import org.newdawn.slick.SlickException;

public class Bolt extends DropItem {
			
	public Bolt(Game game, World world, Vec2 pos, String imgPath)
			throws SlickException {
		
		super(game, world, pos, imgPath);

		MIN_TIME = 50;
		DRAW_FACTOR = 0.1f;
	}
	
	public void collect() {
		game.getPlayer().increaseBoltCounter();
	}
	
}
