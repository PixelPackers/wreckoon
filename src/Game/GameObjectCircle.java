package Game;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class GameObjectCircle extends GameObject {
	
	private float radius;

	public GameObjectCircle(World world, float posX, float posY, float radius, float density, float friction, float restitution, String imgPath, BodyType bodyType, boolean fixedRotation)
			throws SlickException {
		super(world, posX, posY, density, friction, restitution, imgPath, bodyType, fixedRotation);

		CircleShape circleShape = new CircleShape();
		circleShape.m_radius = radius;
		fixtureDef.shape = circleShape;
		this.radius = radius;
		

		super.getReadyToRumble(world);
		
	}

	public void drawImage(){
		Vec2 position = this.body.getPosition();
		float angle = this.body.getAngle();
		img.setRotation(-(float) Math.toDegrees(angle));
		img.draw(position.x - this.radius, -position.y - this.radius, this.radius*2, this.radius*2);
	}
	public void drawOutline(Graphics g) {
		Vec2 pos = this.body.getPosition();
		g.drawArc(pos.x - this.radius, -pos.y - this.radius, this.radius*2, this.radius*2, 0f, 360f);
	}

}
