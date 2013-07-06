package Game;

import java.util.ArrayList;
import java.util.Vector;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


public class Laser extends GameObjectPolygon {

	private ArrayList<GameObject> laserContacts = new ArrayList<GameObject>();
	
	private SpriteSheet laserSheet = Images.getInstance().getSpriteSheet("images/laser.png", 1500, 100);
	private Animation laserAnimation;
	
	private static float WIDTH = 15f;
	private static float HEIGHT = 1f;
	
	
	private static Vec2[] verts = new Vec2[] {
			new Vec2(0f, 0f),
			new Vec2(WIDTH, -HEIGHT * 0.5f),
			new Vec2(WIDTH, HEIGHT * 0.5f)
		};
	
	public Laser(World world, float posX, float posY) throws SlickException {
		super(world, posX, posY, verts, 0f, 0f, 0f, null, BodyType.KINEMATIC, true);
		
		laserAnimation = new Animation(laserSheet, 80);
		laserAnimation.start();
	}
	
	public void position(float x, float y, float angle) {
		this.getBody().setTransform(new Vec2(x, y), angle);
	}
	
	public void draw(Graphics g, boolean debugView){
		if(debugView)
			this.drawOutline(g);
		else 
			this.drawImage(g);
	}
	
	public void drawImage(Graphics g) {
		g.pushTransform();
		g.rotate(getBody().getPosition().x, getBody().getPosition().y, (float) Math.toDegrees(getBody().getAngle()));
		g.translate(0f, -HEIGHT * 0.5f);
		laserAnimation.draw(
				getBody().getPosition().x,
				getBody().getPosition().y, 
				WIDTH, 
				HEIGHT);
		g.popTransform();
	}
	
	public ArrayList<GameObject> getLaserContacts() {
		return laserContacts;
	}

	private static class Vector2d {
		public double x;
		public double y;
		public Vector2d(double x, double y) {
			this.x = x;
			this.y = y;
		}
		public Vector2d add(Vector2d vector) {
			return new Vector2d(x + vector.x, y + vector.y);
		}
		public Vector2d mult(double s) {
			return new Vector2d(x * s, y * s);
		}
		public double dot(Vector2d vector) {
			return x * vector.x + y * vector.y;
		}
	}
	
	public static double curveAngle(double from, double to, double step)
	{
	    Vector2d fromVector = new Vector2d(Math.cos(from), Math.sin(from));
	    Vector2d toVector = new Vector2d(Math.cos(to), Math.sin(to));

	    Vector2d currentVector = slerp(fromVector, toVector, step);
	    
	    double returnValue = Math.atan2(currentVector.y, currentVector.x);
	    
	    return (float) returnValue;
	}

	public static Vector2d slerp(Vector2d from, Vector2d to, double step)
	{
	    if (step == 0) return from;
	    if (from == to || step == 1) return to;

	    double theta = Math.acos(from.dot(to));
	    if (theta == 0) return to;

	    double sinTheta = Math.sin(theta);
	    return from.mult((Math.sin((1 - step) * theta) / sinTheta)).add(to.mult((Math.sin(step * theta) / sinTheta)));
	}

	public Animation getAnimation() {
		return laserAnimation;
	}

}
