package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Player extends GameObjectBox {
	// TODO limits einbauen und verwenden
	private final float MAX_VELOCITY_WALKING = 2f;
	private final float MAX_VELOCITY_RUNNING = 5f;
	private final float ACC_WALKING = 0.5f;
	private final float ACC_RUNNING = 5f;

	private boolean isRunning = false;

	public Player(World world, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath) throws SlickException {
		super(world, posX, posY, width, height, density, friction, restitution, imgPath, BodyType.DYNAMIC, true);
	}

	public void accelerate(boolean left) {
		float vX = this.getBody().getLinearVelocity().x;
		float aX = (isRunning) ? ACC_RUNNING : ACC_WALKING;
		if (left)
			aX = -aX;
		vX += aX;
		this.getBody().setLinearVelocity(new Vec2(vX, this.getBody().getLinearVelocity().y));
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

}
