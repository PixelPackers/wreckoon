package Game;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Laser extends GameObjectPolygon {
	
	private ArrayList<GameObject>	laserContacts	= new ArrayList<GameObject>();
	private SpriteSheet				laserSheet		= Images.getInstance().getSpriteSheet("images/laser.png", 1500, 100);
	
	private Animation				laserAnimation;
	private static float			WIDTH			= 11.25f;
	private static float			HEIGHT			= 0.75f;
	
	private static Vec2[]			verts			= new Vec2[] { new Vec2(0f, 0f), new Vec2(WIDTH, -HEIGHT * 0.5f),
			new Vec2(WIDTH, HEIGHT * 0.5f)			};
	
	public Laser(World world, float posX, float posY) throws SlickException {
		super(world, posX, posY, verts, 0f, 0f, 0f, null, BodyType.KINEMATIC, true);
		
		laserAnimation = new Animation(laserSheet, 80);
		laserAnimation.start();
	}
	
	public void draw(Graphics g, boolean debugView) {
		if (debugView)
			this.drawOutline(g);
		else
			this.drawImage(g);
	}
	
	public void drawImage(Graphics g) {
		g.pushTransform();
		g.rotate(getBody().getPosition().x, getBody().getPosition().y, (float) Math.toDegrees(getBody().getAngle()));
		g.translate(0f, -HEIGHT * 0.5f);
		laserAnimation.draw(getBody().getPosition().x, getBody().getPosition().y, WIDTH, HEIGHT);
		g.popTransform();
	}
	
	public Animation getAnimation() {
		return laserAnimation;
	}
	
	public ArrayList<GameObject> getLaserContacts() {
		return laserContacts;
	}
	
	public boolean hitsPigs() {
		return laserContacts.size() > 0;
	}
	
	public void position(float x, float y, float angle) {
		this.getBody().setTransform(new Vec2(x, y), angle);
	}
	
}
