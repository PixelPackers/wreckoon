package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Player extends GameObject {
	
	private final float	MAX_VELOCITY_WALKING	= 3f;
	private final float	MAX_VELOCITY_RUNNING	= 5f;
	private final float ACC_WALKING = 0.5f;
	private final float ACC_RUNNING = 1f;
	
	private boolean		isRunning				= false;
	
	public Player(BodyDef bodyDef, FixtureDef fixtureDef, World world, String imgPath) throws SlickException {
		super(bodyDef, fixtureDef, world, imgPath);
	}
	
	public void accelerate(boolean left) {
		float vX = this.getBody().getLinearVelocity().x;
		float aX = (isRunning) ? ACC_RUNNING : ACC_WALKING;
		if (left) aX = -aX;
		vX += aX;
		this.getBody().setLinearVelocity(new Vec2(vX, this.getBody().getLinearVelocity().y));
	}
	
}
