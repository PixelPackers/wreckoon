package Game;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


public class Laser extends GameObjectPolygon {

	private ArrayList<Enemy> laserContacts = new ArrayList<Enemy>();
	private SpriteSheet laserSheet = Images.getInstance().getSpriteSheet("images/laser.png", 1500, 100);
	private Animation laserAnimation;
	
	private static float DRAW_WIDTH = 8f;
	private static float DRAW_HEIGHT = 1f;
	
	
	private static Vec2[] verts = new Vec2[] {
			new Vec2( 0.3f, -0f),
			new Vec2( 8f, -0.5f),
			new Vec2( 8f, 0.5f)
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
		g.translate(0.15f, -DRAW_HEIGHT / 2);
		laserAnimation.draw(
				getBody().getPosition().x,
				getBody().getPosition().y, 
				DRAW_WIDTH, 
				DRAW_HEIGHT);
		g.popTransform();
	}
	
	public ArrayList<Enemy> getLaserContacts() {
		return laserContacts;
	}

	public static float curveAngle(float from, float to, float step)
	{
	    Vec2 fromVector = new Vec2((float)Math.cos(from), (float)Math.sin(from));
	    Vec2 toVector = new Vec2((float)Math.cos(to), (float)Math.sin(to));

	    Vec2 currentVector = slerp(fromVector, toVector, step);
	    
	    double returnValue = Math.atan2(currentVector.y, currentVector.x);
	    
	    System.out.println((float)returnValue + "f");
	    System.out.println(returnValue+"d");
	    
	    return (float) returnValue;
	}

	public static Vec2 slerp(Vec2 from, Vec2 to, float step)
	{
	    if (step == 0) return from;
	    if (from == to || step == 1) return to;

	    double theta = Math.acos(Vec2.dot(from, to));
	    if (theta == 0) return to;

	    double sinTheta = Math.sin(theta);
	    return from.mul((float)(Math.sin((1 - step) * theta) / sinTheta)).add(to.mul((float)(Math.sin(step * theta) / sinTheta)));
	}

}
