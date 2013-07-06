package Game;

import java.util.HashMap;

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
		
		this.getBody().setFixedRotation(true);
	}

	@Override
	public void update() {
		super.update();

		if ( !isDead() && !dizzy && !getsGrilled){ 
		
			if(this.isOnGround() && !idle){
				this.getBody().setLinearVelocity(new Vec2(speed, this.getBody().getLinearVelocity().y) );
//				this.body.applyLinearImpulse( new Vec2(speed, this.body.getLinearVelocity().y), this.body.getWorldCenter());
			}
			
			
			if( updateCounter > DIRECTION_SWITCH_MIN_TIME && !idle){
				if ( (this.leftWallColliding() && !left) || (this.rightWallColliding() && left)){
					idle = true;
					updateCounter=0;
					if(Math.random() < 0.75){
						this.currentAnimation = animations.get("idle");
					} else {
						this.currentAnimation = animations.get("idle2");
					}
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

		SpriteSheet sheetWalk = Images.getInstance().getSpriteSheet("images/dumbpigwalk.png", 	275, 276);
		SpriteSheet sheetIdle = Images.getInstance().getSpriteSheet("images/dumbpigidle.png", 	275, 275);
		SpriteSheet sheetIdle2 = Images.getInstance().getSpriteSheet("images/dumbpigidlescratch.png", 	275, 275);
		SpriteSheet sheetDie  = Images.getInstance().getSpriteSheet("images/dumbpigdeath.png", 	275, 275);
		SpriteSheet sheetDisabled  = Images.getInstance().getSpriteSheet("images/dumbpigdisable.png", 	275, 250);
		
		Animation animationWalk = new Animation(sheetWalk, 80);
		
		Animation animationDisabled = new Animation(sheetDisabled, 150);

		Animation animationIdle = new Animation(sheetIdle, idleTime/2);
		
		Animation animationIdle2 = new Animation(sheetIdle2, idleTime/2);
		animationIdle.setLooping(false);
		
		Animation animationDie = new Animation(sheetDie, DIE_TIME);
		animationDie.setLooping(false);

		animations.put("walk", animationWalk);
		animations.put("idle", animationIdle);
		animations.put("idle2", animationIdle2);
		animations.put("die", animationDie);
		animations.put("disabled", animationDisabled);
		
		animationOffsets.put("images/dumbpigwalk.png", new Vec2(-0.5f, -0.7f));
		animationOffsets.put("images/dumbpigidle.png", new Vec2(-0.5f, -0.7f));
		animationOffsets.put("images/dumbpigidlescratch.png", new Vec2(-0.5f, -0.7f));
		animationOffsets.put("images/dumbpigdeath.png", new Vec2(-0.5f, -0.6f));
		animationOffsets.put("images/dumbpigdisable.png", new Vec2(-0.5f, -0.6f));
		
		currentAnimation = animationWalk;
	
	}

	@Override
	public void die() {
		super.die();
		currentAnimation = animations.get("die");
	}
	
	@Override
	public void throwBack() {
		super.throwBack();
		this.currentAnimation = animations.get("disabled");
	}
	
}
