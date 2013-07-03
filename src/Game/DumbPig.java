package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class DumbPig extends Enemy{

	// units are update cycles
	private final static int DIRECTION_SWITCH_MIN_TIME 	= 25;
	private final static int MIN_IDLE_WAITING_TIME 			= 50;
	private final static int MAX_IDLE_WAITING_TIME 			= 150;
	private int idleTime;

	private int		updateCounter	= 0;
	private boolean	idle			= false;
	
	
	
	public DumbPig(Game game, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath,
			BodyType bodyType) throws SlickException {
		super(game, posX, posY, imgPath);
		PIG_CLASS_SIZE_FACTOR = 1.2f;
		
		this.idleTime=  (int)(Math.random() *  (MAX_IDLE_WAITING_TIME - MIN_IDLE_WAITING_TIME) + MIN_IDLE_WAITING_TIME);
		
		initAnimations();
		
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
			
			
			if( updateCounter > DIRECTION_SWITCH_MIN_TIME && !idle){
				if ( (this.leftWallColliding() && !left) || (this.rightWallColliding() && left)){
					idle = true;
					updateCounter=0;
					this.currentAnimation = animations.get("idle");
					this.currentAnimation.restart();
				}
			}
			
			if ( idle && updateCounter > MIN_IDLE_WAITING_TIME){
				this.left = !this.left;  
				speed = -speed;
				this.idle = false;
				updateCounter=0;
				this.currentAnimation = animations.get("walk");
			}
		}
		
		++updateCounter;
	}

	@Override
	protected void initAnimations() throws SlickException {

		SpriteSheet sheetWalk = new SpriteSheet("images/dumbpigwalk.png", 	550, 550);
		SpriteSheet sheetIdle = new SpriteSheet("images/dumbpigidlescratch.png", 	550, 550);
		SpriteSheet sheetDie  = new SpriteSheet("images/smartpigdeath.png", 	550, 550);
		
		Animation animationWalk = new Animation(sheetWalk, 80);
		
		Animation animationIdle = new Animation(sheetIdle, idleTime/2);
		animationIdle.setLooping(false);
		
		Animation animationDie = new Animation(sheetDie, DIE_TIME);
		animationDie.setLooping(false);

		animations.put("walk", animationWalk);
		animations.put("idle", animationIdle);
		animations.put("die", animationDie);
		
		currentAnimation = animationWalk;
	
	}

	@Override
	public void die() {
		super.die();
		currentAnimation = animations.get("die");
	}
	
}
