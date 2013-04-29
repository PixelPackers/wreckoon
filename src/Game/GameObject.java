package Game;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class GameObject {

	protected Body body;
	protected BodyDef bodyDef;
	protected FixtureDef fixtureDef;

	protected Image img;
	protected float width, height;

	public GameObject(World world, float posX, float posY, float density, float friction, String imgPath, BodyType bodyType)
			throws SlickException {
		bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(posX, posY);
		body = new Body(bodyDef, world);

		fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;

		this.img = new Image(imgPath);
	}

	protected void getReadyToRumble(World world) {
		
		// XXX was is der unterschied zwischen den naechsten 2 zeilen?
		// body = new Body(bodyDef, world);
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
	}

	public void draw(Graphics g, boolean debugView){
		if(debugView || this.img == null)
			drawOutline(g);
		else 
			drawImage();
		
	}

	public abstract void drawImage();
	public abstract void drawOutline(Graphics g);

	public Body getBody() {
		return body;
	}

}