package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class EnemyPrimitive extends Enemy{

	// units are update cycles
	private final static int DIRECTION_SWITCH_MIN_TIME 	= 25;
	private final static int IDLE_WAITING_TIME 			= 100;
	
	private float	speed			= -5;
	private int		updateCounter	= 0;
	private boolean	idle			= false;
	
	
	public EnemyPrimitive(Game game, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath,
			BodyType bodyType) throws SlickException {
		super(game, posX, posY, width, height, density, friction, restitution, imgPath, bodyType);


		this.setImage(new Image("images/dumbpig.png"));
		this.getBody().setFixedRotation(true);
	}

	@Override
	public void update() {
		if ( !isDead() ){ 
		
			if(this.isOnGround() && !idle){
				this.getBody().setLinearVelocity(new Vec2(speed, this.getBody().getLinearVelocity().y) );
//				this.body.applyLinearImpulse( new Vec2(speed, this.body.getLinearVelocity().y), this.body.getWorldCenter());
			}
			
			
			if(this.isOnWall()  && updateCounter > DIRECTION_SWITCH_MIN_TIME && !idle){
				idle = true;
				updateCounter=0;
			}
			
			if ( idle && updateCounter > IDLE_WAITING_TIME){

				speed = -speed;
				this.idle = false;
				updateCounter=0;
			}
		}
		
		++updateCounter;
	}

}
