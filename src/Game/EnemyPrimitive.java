package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class EnemyPrimitive extends Enemy{

	// units are update cycles
	private final static int DIRECTION_SWITCH_MIN_TIME 	= 25;
	private final static int IDLE_WAITING_TIME 			= 100;

	private float	maxSpeed		= 5;
	private float	speed			= -maxSpeed;
	private int		updateCounter	= 0;
	private boolean	idle			= false;
	
	
	public EnemyPrimitive(Game game, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath,
			BodyType bodyType) throws SlickException {
		super(game, posX, posY, width, height, density, friction, restitution, imgPath, bodyType);


		this.img = new Image("images/dumbpig.png");
		this.body.setFixedRotation(true);
	}

	@Override
	public void update() {
		if ( !isDead() ){ 
		
			if(this.isOnGround() && !idle){
				this.body.setLinearVelocity(new Vec2(speed, this.body.getLinearVelocity().y) );
//				this.body.applyLinearImpulse( new Vec2(speed, this.body.getLinearVelocity().y), this.body.getWorldCenter());
			}
			
			
			if(this.isOnWall()  && updateCounter > DIRECTION_SWITCH_MIN_TIME && !idle){
				idle = true;
				updateCounter=0;
			}
			
			if ( idle && updateCounter > IDLE_WAITING_TIME){
				this.left = !this.left;  
				speed = (left) ? -maxSpeed : maxSpeed;
				this.idle = false;
				updateCounter=0;
			}
		}
		
		++updateCounter;
	}

	@Override
	protected void initAnimations() throws SlickException {
		// TODO Auto-generated method stub
		
	}

}
