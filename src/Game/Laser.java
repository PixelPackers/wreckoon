package Game;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class Laser extends GameObjectPolygon {

	private ArrayList<Enemy> laserContacts = new ArrayList<Enemy>();
	
	private static Vec2[] verts = new Vec2[] {
			new Vec2( 0.5f, 0.3f),
			new Vec2( 0.5f, -0.3f),
			new Vec2( 3.5f, -0.5f),
			new Vec2( 3.5f, 0.5f)
		};
	
	public Laser(World world, float posX, float posY) throws SlickException {
		super(world, posX, posY, verts, 0f, 0f, 0f, null, BodyType.KINEMATIC, true);
	}
	
	public void position(float x, float y, float angle) {
		this.getBody().setTransform(new Vec2(x, y), angle);
	}
	
	public void drawOutline(Graphics g) {
		super.drawOutline(g);
	}
	
	public ArrayList<Enemy> getLaserContacts() {
		return laserContacts;
	}
	
	public float curveAngle(float dest, float current, float smoothness) {
		if (dest - current < 180f) {
			return current + (dest - current) / smoothness;
		} else {
			return current + (360f - dest - current) / smoothness;
		}
		
	}
}
