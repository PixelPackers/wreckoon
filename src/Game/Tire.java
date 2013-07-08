package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Tire extends GameObjectCircle {
	
	private float	bounce	= 1f;
	
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
	
	public void bounce(float value) {
		bounce = value;
	}
	
	public void drawImage() {
		bounce = Functions.curveValue(1f, bounce, 10);
		Vec2 position = this.getBody().getPosition();
		float drawradius = getRadius() * bounce;
		this.getImage().draw(position.x - drawradius, position.y - drawradius, drawradius * 2, drawradius * 2);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
}
