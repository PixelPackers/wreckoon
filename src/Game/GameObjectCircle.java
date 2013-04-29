package Game;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class GameObjectCircle extends GameObject {

	public GameObjectCircle(World world, float posX, float posY, float radius, float density, float friction, String imgPath, BodyType bodyType)
			throws SlickException {
		super(world, posX, posY, density, friction, imgPath, bodyType);

		CircleShape circleShape = new CircleShape();
		circleShape.m_radius = radius;
		fixtureDef.shape = circleShape;
		
		width = height = radius * 2;
		

		super.getReadyToRumble(world);
		
	}

	public void drawImage(){
		Vec2 position = this.body.getPosition();
		float angle = this.body.getAngle();
		img.setRotation(-(float) Math.toDegrees(angle));
		img.draw(position.x - this.width / 2, -position.y - this.height / 2, this.width, this.height);
	}
	public void drawOutline(Graphics g) {
		Vec2 pos = this.body.getPosition();
		g.drawArc(pos.x - width * 0.5f, -pos.y - height * 0.5f, width, height, 0f, 360f);
	}

}
