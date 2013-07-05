package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.World;

import org.newdawn.slick.SlickException;

public class Bolt extends DropItem {
			
	public Bolt(Game game, World world, Vec2 pos, String imgPath)
			throws SlickException {
		
		super(game, world, pos, imgPath);

		MIN_TIME = 50;
		DRAW_FACTOR = 0.1f;
		
		Filter filter = new Filter();
		filter.maskBits = 1 + 2 + 8 + 16;
		filter.categoryBits = 8;
		this.getFixture().setFilterData(filter);
	}
	
	public void collect() {
		game.getPlayer().boltsCollected(1);
	}
	
}
