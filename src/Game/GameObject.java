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
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GameObject {

	private Body body;
	private BodyDef bodyDef;
	private PolygonShape polygonShape;
	private FixtureDef fixtureDef;

	private Image	img;
	private float	width, height;
	
	/*
	public GameObject(BodyDef bd, FixtureDef fd, World world, String imgPath) throws SlickException {
		this.body = world.createBody(bd);
		this.body.createFixture(fd);
		this.img = new Image(imgPath);
		AABB aabb = this.body.getFixtureList().m_aabb;
		width = aabb.lowerBound.x - aabb.upperBound.x;
		height = aabb.lowerBound.y - aabb.upperBound.y;
	}
	//*/
	
	public void draw() {
		Vec2 position = this.body.getPosition();
		float angle = this.body.getAngle();
		img.setRotation(-(float) Math.toDegrees(angle));
		img.draw(position.x - this.width / 2, -position.y - this.height / 2, this.width, this.height);
	}
	
	public Body getBody() {
		return body;
	}
	


//*

	public GameObject(World world, float posX, float posY, Shape shape, String imgPath, boolean isDynamic) throws SlickException {
		
		bodyDef 		= new BodyDef();
		if(isDynamic){
			bodyDef.type = BodyType.DYNAMIC;
		} else {
			bodyDef.type = BodyType.STATIC;
		}
		bodyDef.position.set(posX, posY);
	
		body			= new Body(bodyDef, world);
		
		fixtureDef 		= new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.11f;
		fixtureDef.friction = 0.1f;
		
		body = world.createBody( bodyDef );
		body.createFixture( fixtureDef );
		
		
		this.img = new Image(imgPath);
		AABB aabb = this.body.getFixtureList().m_aabb;
		width = aabb.lowerBound.x - aabb.upperBound.x;
		height = aabb.lowerBound.y - aabb.upperBound.y;
	}

//*/
}