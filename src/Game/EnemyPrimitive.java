package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class EnemyPrimitive extends Enemy{

	// units are update cycles
	private final static int DIRECTION_SWITCH_MIN_TIME 	= 25;
	private final static int IDLE_WAITING_TIME 			= 100;

	private float	maxSpeed		= 1.25f;
	private float	speed			= -maxSpeed;
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
		super.update();

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

		SpriteSheet sheetWalk = new SpriteSheet("images/walkcycle.png", 	600, 575);
		SpriteSheet sheetIdle = new SpriteSheet("images/walkcycle.png", 	600, 575);
		SpriteSheet sheetDie  = new SpriteSheet("images/walkcycle.png", 	600, 575);
		
		Animation animationWalk = new Animation(sheetWalk, 80);
		
		Animation animationIdle= new Animation(sheetIdle, 80);
		animationIdle.setLooping(false);
		
		Animation animationDie= new Animation(sheetDie, 80);
		animationDie.setLooping(false);

		animations.put("walk", animationWalk);
		animations.put("idle", animationIdle);
		animations.put("die", animationDie);
	
	}

}
