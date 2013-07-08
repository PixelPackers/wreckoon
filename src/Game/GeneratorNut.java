package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class GeneratorNut extends DropItem {

	public GeneratorNut(Game game, World world, Vec2 pos, String imgPath) throws SlickException {
		super(game, world, pos, imgPath);

		MIN_TIME = 25;
		MAX_TIME = 125;
		DRAW_FACTOR = 0.1f;
		
		Filter filter = new Filter();
		filter.maskBits = 1;
		filter.categoryBits = 1;
		this.getFixture().setFilterData(filter);
	}

	@Override
	public boolean isCollectable() {
		return false;
	}

	@Override
	public void collect() {
		
	}
}
