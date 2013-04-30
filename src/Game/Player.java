package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Player extends GameObjectBox {

	private final float MAX_VELOCITY_WALKING = 7f;
	private final float MAX_VELOCITY_RUNNING = 20f;
	private final float ACC_WALKING = 0.5f;
	private final float ACC_RUNNING = 0.75f;
	
	private float jumpPower = 20f;
	private float groundPoundPower = -50f;

	private boolean isRunning = false;
	private boolean isJumping = false;
	private boolean isGroundPounding = false;

	public Player(World world, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath) throws SlickException {
		super(world, posX, posY, width, height, density, friction, restitution, imgPath, BodyType.DYNAMIC, true);
	}

	public void accelerate(boolean left) {
		if(!isGroundPounding){
			float velocityX = this.getBody().getLinearVelocity().x;
			float accelerationX = (isRunning) ? ACC_RUNNING : ACC_WALKING;
			float maxVelocity = (isRunning) ? MAX_VELOCITY_RUNNING : MAX_VELOCITY_WALKING;
			
			if (left){
				accelerationX = -accelerationX;
				maxVelocity = -maxVelocity;
			}
			
			velocityX += accelerationX;
			velocityX = ( Math.abs(velocityX) > Math.abs(maxVelocity) ) ? maxVelocity : velocityX;
			
			this.getBody().setLinearVelocity(new Vec2(velocityX, this.getBody().getLinearVelocity().y));
		}
	}

	public void jump(){
		if(!isGroundPounding){
			this.getBody().setLinearVelocity(new Vec2(this.getBody().getLinearVelocity().x, this.jumpPower));
		}
	}
	public void groundPound(){
		isGroundPounding = true;
		
		// TODO wie mach ich das, dass der kurz stehen bleibt?
		// 		thread sleep funkt nicht, weil dann alles stehen bleibt...
		
		//		this.getBody().setLinearVelocity(new Vec2(0,0));
		this.getBody().setLinearVelocity(new Vec2(this.getBody().getLinearVelocity().x, groundPoundPower));

		isGroundPounding = false;
	}
	
	
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public Vec2 getCurrentVelocity(){
		return this.getBody().getLinearVelocity();
	}
}
