package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class GameObjectBox extends GameObjectPolygon {

	private float width, height;

	public GameObjectBox(World world, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath, BodyType bodyType, boolean fixedRotation)
			throws SlickException {
		super(world, posX, posY, new Vec2[]{
				new Vec2(-width * 0.5f,  height * 0.5f),
				new Vec2(-width * 0.5f, -height * 0.5f),
				new Vec2( width * 0.5f, -height * 0.5f),
				new Vec2( width * 0.5f,  height * 0.5f)},
				density, friction, restitution, imgPath, bodyType, fixedRotation);
		this.width = width;
		this.height = height;
	}

	public void drawImage(){
		Vec2 position = this.body.getPosition();
		float angle = this.body.getAngle();
		img.setRotation(-(float) Math.toDegrees(angle));
		img.draw(position.x - this.width / 2, -position.y - this.height / 2, this.width, this.height);
	}
}
