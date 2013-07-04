package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class SmartPig extends Enemy {

	private static final float MIN_JUMP_POWER = -4.5f;
	private static final float MAX_JUMP_POWER = -6f;
	private static final float AGGRO_DISTANCE = 5f; 
	private float jumpPower;
	private boolean aggro = false;

	private static final int MIN_SWITCH_TIME = 20;
	private int switchTimeCounter = 0;
	
	public SmartPig(Game game, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath,
			BodyType bodyType) throws SlickException {
		super(game, posX, posY, imgPath);

		this.jumpPower =  (float)(Math.random() *  (MAX_JUMP_POWER-MIN_JUMP_POWER) + MIN_JUMP_POWER);
		
		PIG_CLASS_SIZE_FACTOR = 1.5f;
		this.getBody().setFixedRotation(true);
		this.setImage(Images.getInstance().getImage("images/smartpig.png"));
		initAnimations();
		
	}
	
	public void update(){
		super.update();
		Player player = this.game.getPlayer();
		
		Vec2 p = player.getBody().getPosition();
		Vec2 e = this.getBody().getPosition();
		
		float distance = (float) (Math.sqrt( (p.x - e.x)*(p.x - e.x) + (p.y - e.y)*(p.y - e.y) ) );
		
		if( !player.isDead() &&  ((distance < AGGRO_DISTANCE && aggro) ||
				(distance < AGGRO_DISTANCE && playerIsLeft() == left) ) ){
			aggro = true;
		} else {
			if(aggro) {
				aggro = false;
				if(Math.random() < 0.5){
					left = !left;
				}
			}
		}
		
		if (!isDead() && !dizzy ){
			if(aggro){
			
				float x; 
				
				if(playerIsLeft() ){
					x = -speed;
					setLeft(true);
				} else {
					x = speed;
					setLeft(false); 
				}
				if(this.isOnGround()){
					this.getBody().setLinearVelocity(new Vec2(x, this.getBody().getLinearVelocity().y) );
	//				this.getBody().applyLinearImpulse( new Vec2(x, 0*this.getBody().getLinearVelocity().y), this.getBody().getWorldCenter());
				}
				
				if(this.isOnWall() && this.isOnGround()) {
	//				this.body.applyForce( new Vec2(this.body.getLinearVelocity().x, jumpPower), this.body.getWorldCenter() );
					this.getBody().setLinearVelocity( new Vec2(this.getBody().getLinearVelocity().x, jumpPower) );
				}
				
				this.currentAnimation = animations.get("walk");
				
			} else { // patroullieren
				
				if ( (this.leftWallColliding() && !left) || (this.rightWallColliding() && left)){
				
					setLeft(!left);
	
				}
				float x = (left) ? -speed*0.5f : speed*0.5f;
				
				this.getBody().setLinearVelocity(new Vec2(x, this.getBody().getLinearVelocity().y) );
			
				this.currentAnimation = animations.get("idle");
				
			}
		}

		++switchTimeCounter;
	}

	@Override
	protected void initAnimations() throws SlickException {

		SpriteSheet sheetWalk = new SpriteSheet("images/smartpigrun.png", 	580, 610);
		SpriteSheet sheetIdle = new SpriteSheet("images/smartpigIdle.png", 	550, 600);
		SpriteSheet sheetDie  = new SpriteSheet("images/smartpigdeath.png", 	550, 600);
		
		Animation animationWalk = new Animation(sheetWalk, 80);
		
		Animation animationIdle= new Animation(sheetIdle, 150);
		
		Animation animationDie= new Animation(sheetDie, DIE_TIME);
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
	
	private void setLeft(boolean left) {
		if(switchTimeCounter > MIN_SWITCH_TIME) {
			switchTimeCounter = 0;
			this.left = left;
		}
	}
	
	private boolean playerIsLeft(){
		return this.game.getPlayer().getBody().getPosition().x	< this.getBody().getPosition().x;
		
	}
	@Override
	public void throwBack() {
		super.throwBack();
		aggro = false;
	}
}
