package Game;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Tire extends GameObjectCircle {
	
	private float	x, y;
	
	public Tire(World world, float posX, float posY, float radius) throws SlickException {
		
		super(world, posX, posY, radius, 0f, 0.3f, 1f, "images/tire.png", BodyType.STATIC);
		
		Filter filter = new Filter();
		filter.categoryBits = 33;
		filter.maskBits = 1 + 2 + 4 + 8 + 16 + 32;
		this.getFixture().setFilterData(filter);
		
		x = posX;
		y = posY;
		
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
}
