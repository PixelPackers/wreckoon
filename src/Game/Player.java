package Game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class Player {

	private final float MAX_VELOCITY_WALKING = 7f;
	private final float MAX_VELOCITY_RUNNING = 20f;
	private final float ACC_WALKING = 0.5f;
	private final float ACC_RUNNING = 0.75f;
	
	private float jumpPower = 20f;
	private float groundPoundPower = -50f;

	private boolean isRunning = false;
	private boolean isJumping = false;
	private boolean isGroundPounding = false;
	
	private float	maxPlayerRotation = 15f;
	
	
	// GameObjectBox
	private float width, height;
	
	// GameObjectPolygon
	protected PolygonShape polygonShape;
	
	// GameObject
	protected Body 			body;
	protected BodyDef 		bodyDef;
	protected FixtureDef 	fixtureDef;
	protected Image 		img;

//	TODO
//	für player einfach code verdoppeln, und alles im konstruktor erstellen, 
//		evt sogar ohne parameter, weil eh nur einmal... außer posX/Y world usw was ma halt braucht... 
//	dann kann man rotation sperre parameter wieder raus aus anderen klassen
//	dann collision vom player iwie anzeigen lassen...
//	evtl sprites laden?
	
// gDoc überarbeiten falls alles fertig is...
	
	
	public Player(World world, float posX, float posY) throws SlickException {
	
		this.width = 1f;
		this.height = 2f;
		
	
		// GameObject
		this.bodyDef = new BodyDef();
		this.bodyDef.type = BodyType.DYNAMIC;
		this.bodyDef.position.set(posX, posY);
//		this.bodyDef.fixedRotation = true;

		
		// GameObjectBox
		Vec2[] verts= new Vec2[]{
			new Vec2(-width * 0.5f,  height * 0.5f),
			new Vec2(-width * 0.5f, -height * 0.5f),
			new Vec2( width * 0.5f, -height * 0.5f),
			new Vec2( width * 0.5f,  height * 0.5f)
		};
		
		this.polygonShape = new PolygonShape();
		this.polygonShape.set(verts, verts.length);
		
		this.fixtureDef = new FixtureDef();
		this.fixtureDef.density 	= 11f;
		this.fixtureDef.friction 	= 0.3f;
		this.fixtureDef.restitution = 0f;
		this.fixtureDef.shape = polygonShape;
		
		this.img = new Image("images/player.png");

	
	
	

//		this.body = new Body(bodyDef, world);
		this.body = world.createBody(bodyDef);
		this.body.createFixture(fixtureDef);	
		
	}
	
	public void draw(Graphics g, boolean debugView){
		if(debugView || this.img == null)
			this.drawOutline(g);
		else 
			this.drawImage();
	}
	
	public void drawImage(){
		Vec2 position = this.body.getPosition();
		float angle = this.body.getAngle();
		img.setRotation(-(float) Math.toDegrees(angle));
		img.draw(position.x - this.width / 2, -position.y - this.height / 2, this.width, this.height);
	}
	public void drawOutline(Graphics g) {

		Polygon polygonToDraw = new Polygon();
		Vec2[] verts = this.polygonShape.getVertices();
		for (int i=0; i< this.polygonShape.m_vertexCount; ++i) {
			Vec2 vert = verts[i];
			Vec2 worldPoint = this.body.getWorldPoint(vert);
			polygonToDraw.addPoint(worldPoint.x, -worldPoint.y);
		}
		g.draw(polygonToDraw);
		
	}
	
	public void update() {
		
		//*
//		 FIXME kA alles fixen am besten...
		if ( Math.abs( Math.toDegrees( this.body.getAngle() ) ) > this.maxPlayerRotation){
//			this.body.setTransform(this.body.getPosition(), -10);
			this.body.m_angularVelocity = -(this.body.m_angularVelocity * 0.75f);
		}
		/*/
		if ( Math.toDegrees( this.body.getAngle() ) > this.maxPlayerRotation){
			this.body.setTransform(this.body.getPosition(), this.maxPlayerRotation);
			this.body.m_angularVelocity = -(this.body.m_angularVelocity * 0.75f);
		}
		if ( Math.toDegrees( this.body.getAngle() ) < -this.maxPlayerRotation){
			this.body.setTransform(this.body.getPosition(), -this.maxPlayerRotation);
			this.body.m_angularVelocity = -(this.body.m_angularVelocity * 0.75f);
		}
		//*/
	}
	

	public void accelerate(boolean left) {
		
		// FIXME wenn man auf max sprint beschleunigt und springt, kann man ohne sprint taste nicht gegenlenken, weil max speed für 'gehen' zu hoch wäre und somit eingabe ignoriert wird
		// 		man kann mit 'gehen' nie gegenlenken, wenn speed höher als maxGehen ist...
		
		
//		if(!isGroundPounding){
			float velocityX = this.body.getLinearVelocity().x;
			float accelerationX = (isRunning) ? ACC_RUNNING : ACC_WALKING;
			float maxVelocity = (isRunning) ? MAX_VELOCITY_RUNNING : MAX_VELOCITY_WALKING;
			
			if (left){
				accelerationX = -accelerationX;
				maxVelocity = -maxVelocity;
			}
			
			if(Math.abs(velocityX + accelerationX)  < Math.abs(maxVelocity)){
				velocityX += accelerationX;
			}
			
			this.body.setLinearVelocity(new Vec2(velocityX, this.body.getLinearVelocity().y));
//		} // isGroundPounding Check end
	}

	public void jump(){
		if(!isGroundPounding){
			this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, this.jumpPower));
		}
	}
	public void groundPound(){
		isGroundPounding = true;
		
		// TODO wie mach ich das, dass der kurz stehen bleibt?
		// 		thread sleep funkt nicht, weil dann alles stehen bleibt...
		
		//		this.getBody().setLinearVelocity(new Vec2(0,0));
		this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, groundPoundPower));

		isGroundPounding = false;
	}
	
	
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public Vec2 getCurrentVelocity(){
		return this.body.getLinearVelocity();
	}

	public Body getBody() {
		return body;
	}

}
