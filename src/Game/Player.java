package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Player extends GameObjectCircle {

	private final float MAX_VELOCITY_WALKING = 3f;
	private final float MAX_VELOCITY_RUNNING = 5f;
	private final float ACC_WALKING = 0.5f;
	private final float ACC_RUNNING = 1f;

	private boolean isRunning = false;

	public Player(World world, float posX, float posY, float radius, float density, float friction, String imgPath) throws SlickException {
		super(world, posX, posY, radius, density, friction, imgPath, BodyType.DYNAMIC);
	}

	public void accelerate(boolean left) {
		float vX = this.getBody().getLinearVelocity().x;
		float aX = (isRunning) ? ACC_RUNNING : ACC_WALKING;
		if (left)
			aX = -aX;
		vX += aX;
		this.getBody().setLinearVelocity(new Vec2(vX, this.getBody().getLinearVelocity().y));
	}

}
