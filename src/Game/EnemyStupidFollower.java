package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;

public class EnemyStupidFollower extends Enemy {

	private float jumpPower = -3f;
	
	public EnemyStupidFollower(Game game, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath,
			BodyType bodyType) throws SlickException {
		super(game, posX, posY, imgPath);
		
		this.getBody().setFixedRotation(true);
		this.setImage(new Image("images/smartpig.png"));
		initAnimations();
		
	}
	
	public void update(){
		super.update();
		
		if (!isDead() ){ 
			Player player = this.game.getPlayer();
			float speed = 1.25f;
			float x = speed;
			
			if(player.getBody().getPosition().x	< this.getBody().getPosition().x){
				x = -speed;
			}
			
			if(this.isOnGround()){
				this.getBody().setLinearVelocity(new Vec2(x, this.getBody().getLinearVelocity().y) );
//				this.getBody().applyLinearImpulse( new Vec2(x, 0*this.getBody().getLinearVelocity().y), this.getBody().getWorldCenter());
			}
			
			if(this.isOnWall() && this.isOnGround()) {
//				this.body.applyForce( new Vec2(this.body.getLinearVelocity().x, jumpPower), this.body.getWorldCenter() );
				this.getBody().setLinearVelocity( new Vec2(this.getBody().getLinearVelocity().x, jumpPower) );
			}
		}
	}

	@Override
	protected void initAnimations() throws SlickException {

		SpriteSheet sheetWalk = new SpriteSheet("images/smartpigrun.png", 	580, 610);
		SpriteSheet sheetIdle = new SpriteSheet("images/smartpigIdle.png", 	600, 575);
		SpriteSheet sheetDie  = new SpriteSheet("images/smartpigdeath.png", 	600, 575);
		
		Animation animationWalk = new Animation(sheetWalk, 80);
		
		Animation animationIdle= new Animation(sheetIdle, 80);
		animationIdle.setLooping(false);
		
		Animation animationDie= new Animation(sheetDie, 80);
		animationDie.setLooping(false);

		animations.put("walk", animationWalk);
		animations.put("idle", animationIdle);
		animations.put("die", animationDie);
		
		currentAnimation = animationWalk;
		
	}
	
	@Override
	public void drawImage() {
		currentAnimation.draw( 
				getBody().getPosition().x-width*0.5f,
				getBody().getPosition().y-height*0.5f, 
				width * 1.5f, 
				height * 1.5f);
	}
	
}
