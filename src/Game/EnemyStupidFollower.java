package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class EnemyStupidFollower extends Enemy {

	private float jumpPower = 15f;
	
	public EnemyStupidFollower(Game game, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath,
			BodyType bodyType) throws SlickException {
		super(game, posX, posY, width, height, density, friction, restitution, imgPath, bodyType);
		this.body.setFixedRotation(true);
	}
	
	public void update(){
		
		if(!dead){ 
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
			
			
			if(this.isOnWall() && this.isOnGround())
				this.body.applyForce( new Vec2(this.body.getLinearVelocity().x, jumpPower), this.body.getWorldCenter() );
		}
	}
	
	@Override
	public void drawOutline(Graphics g){
				
		Polygon polygonToDraw = new Polygon();
		Vec2[] verts = this.polygonShape.getVertices();
		for (int i=0; i< this.polygonShape.m_vertexCount; ++i) {
			Vec2 vert = verts[i];
			Vec2 worldPoint = this.body.getWorldPoint(vert);
			polygonToDraw.addPoint(worldPoint.x, -worldPoint.y);
		}
		
		if(this.dead)
			g.setColor(Color.red);
		g.draw(polygonToDraw);
		g.setColor(Color.white);
		
		
		// draw sensors
		for (MySensor mySensor : sensorList){
			mySensor.draw(g, this.body);
		}
		
	}
}