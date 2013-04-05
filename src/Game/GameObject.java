package Game;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GameObject {
	
	private Body	body;

	private Image	img;
	private float	width, height;
	
	public GameObject(BodyDef bd, FixtureDef fd, World world, String imgPath) throws SlickException {
		this.body = world.createBody(bd);
		this.body.createFixture(fd);
		this.img = new Image(imgPath);
		AABB aabb = this.body.getFixtureList().m_aabb;
		width = aabb.lowerBound.x - aabb.upperBound.x;
		height = aabb.lowerBound.y - aabb.upperBound.y;
	}
	
	public void draw() {
		Vec2 position = this.body.getPosition();
		float angle = this.body.getAngle();
		img.setRotation(-(float) Math.toDegrees(angle));
		img.draw(position.x - this.width / 2, -position.y - this.height / 2, this.width, this.height);
	}
	
	public Body getBody() {
		return body;
	}
	
}
