package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class EnemyStupidFollower extends Enemy {

	private float jumpPower = 12f;
	
	public EnemyStupidFollower(Game game, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath,
			BodyType bodyType) throws SlickException {
		super(game, posX, posY, width, height, density, friction, restitution, imgPath, bodyType);
		this.body.setFixedRotation(true);
	}
	
	public void update(){
		
		if (!isDead() ){ 
			Player player = this.game.getPlayer();
			float speed = 5;
			float x = speed;
			
			if(player.getBody().getPosition().x	< this.getBody().getPosition().x){
				x = -speed;
			}
			if(this.isOnGround()){
	//			this.body.setLinearVelocity(new Vec2(x, this.body.getLinearVelocity().y) );
				this.body.applyLinearImpulse( new Vec2(x, this.body.getLinearVelocity().y), this.body.getWorldCenter());
			}
			
			
			if(this.isOnWall() && this.isOnGround()) {
//				this.body.applyForce( new Vec2(this.body.getLinearVelocity().x, jumpPower), this.body.getWorldCenter() );
				this.body.setLinearVelocity( new Vec2(this.body.getLinearVelocity().x, jumpPower) );
			}
		}
	}
	
	
}