package Game;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class Player {

	// TODO PLAYER.JAVA LIST
	// 2 sensors pro seite für wand collision
	// einen groundCollider
	// 		für jump()
	// die iwie einfärben zum debuggen?
	
	private final float MAX_VELOCITY_WALKING = 7f;
	private final float MAX_VELOCITY_RUNNING = 20f;
	private final float ACC_WALKING = 0.5f;
	private final float ACC_RUNNING = 0.75f;
	
	private int groundPoundCounter = 0;
	
	private float jumpPower = 20f;
	private float wallJumpPowerFactor = 0.3f;
	private float groundPoundPower = -50f;

	private boolean running = false;
	private boolean groundPounding = false;
	private boolean jumping = false;
	private float	accelerationX = ACC_WALKING;
	private float	maxVelocity = MAX_VELOCITY_WALKING;
	
	private float	maxPlayerRotation = 10f;
	private World world;
	
	private float width = 1f;
	private float height = 2f;
	private PolygonShape firstPolygonShape 		= new PolygonShape();
	private PolygonShape secondPolygonShape 	= new PolygonShape();
	
	private Body 		body;
	private BodyDef 	bodyDef				= new BodyDef();
	private Fixture 	firstFixture;
	private Fixture 	secondFixture;
	private FixtureDef 	firstFixtureDef 	= new FixtureDef();
	private FixtureDef	secondFixtureDef	= new FixtureDef();
	
	private MySensor 	sensorTopLeft;
	private MySensor 	sensorTopRight;
	private MySensor	sensorBottomLeft;
	private MySensor 	sensorBottomRight;
	private MySensor	sensorGroundCollision;

	private ArrayList<MySensor> sensorList			= new ArrayList<MySensor>();
	
	private Image 		img;
	
	public Player(World world, float posX, float posY) throws SlickException {
		
		this.world = world;
		this.bodyDef.type = BodyType.DYNAMIC;
		this.bodyDef.position.set(posX, posY);

		Vec2[] polygonShapeVerts = new Vec2[]{
			new Vec2(-width * 0.5f,  height * 0.5f),
			new Vec2(-width * 0.5f, -height * 0.5f),
			new Vec2( width * 0.5f, -height * 0.5f),
			new Vec2( width * 0.5f,  height * 0.5f)
		};
		
		this.firstPolygonShape.set(polygonShapeVerts, polygonShapeVerts.length);
		
		this.firstFixtureDef.density 	= 11f;
		this.firstFixtureDef.friction 	= 0.3f;
		this.firstFixtureDef.restitution = 0f;
		this.firstFixtureDef.shape = firstPolygonShape;
		
		
		this.img = new Image("images/player.png");

		this.body = world.createBody(bodyDef);
		this.firstFixture = this.body.createFixture(firstFixtureDef);
		this.body.setFixedRotation(true);
		
		// second hitbox
		Vec2[] vertsSensor = new Vec2[]{
			new Vec2(-height * 0.5f,  width * 0.5f),
			new Vec2(-height * 0.5f, -width * 0.5f),
			new Vec2( height * 0.5f, -width * 0.5f),
			new Vec2( height * 0.5f,  width * 0.5f)
		};

		PolygonShape secondPolygonShape = new PolygonShape();
		secondPolygonShape.set(vertsSensor, vertsSensor.length);
		
		this.secondFixtureDef = new FixtureDef();
		this.secondFixtureDef.shape = secondPolygonShape;
		this.secondPolygonShape = secondPolygonShape;

		this.secondFixture = this.body.createFixture(secondFixtureDef);
		
		
		this.createSensors();
		
	
	}
	
	private void createSensors() {
		System.out.println(this.body.getFixtureList().m_aabb.lowerBound.x);
		System.out.println(this.body.getFixtureList().m_aabb.lowerBound.y);
		System.out.println(this.body.getFixtureList().m_aabb.upperBound.x);
		System.out.println(this.body.getFixtureList().m_aabb.upperBound.y);

		// XXX WORK IN PROGRESS
		// eine methode, die die aktuelle fixture nimmt und aufgrund derer die sensoren anordnet
		
		// wall collision sensors
		float sensorSizeWidth	= width  * 0.125f;
		float sensorSizeHeight	= height * 0.1f;
		float default_xSpace = 0.5f;
		float default_ySpace = 0.75f;
		float xSpace = default_xSpace;
		float ySpace = default_ySpace;
		
		for (int i=0;i<2; ++i){

			if(i % 2 == 0){
				xSpace = -default_xSpace;
			} else {
				xSpace = default_xSpace;
			}
			
			for (int j=0;j<2; ++j){
				 
				if(j % 2 == 0){
					ySpace = -default_ySpace;
				} else {
					ySpace = default_ySpace;
				}
				
				Vec2[] vertsSensor = new Vec2[]{
					new Vec2(-sensorSizeWidth + xSpace,  sensorSizeHeight + ySpace),
					new Vec2(-sensorSizeWidth + xSpace, -sensorSizeHeight + ySpace),
					new Vec2( sensorSizeWidth + xSpace, -sensorSizeHeight + ySpace),
					new Vec2( sensorSizeWidth + xSpace,  sensorSizeHeight + ySpace)
				};

				PolygonShape sensorPolygonShape = new PolygonShape();
				sensorPolygonShape.set(vertsSensor, vertsSensor.length);
				
				FixtureDef 	fixtureDefSensor = new FixtureDef();
				fixtureDefSensor.shape = sensorPolygonShape;
				fixtureDefSensor.isSensor=true;
		
				sensorList.add(new MySensor(this.body.createFixture(fixtureDefSensor), sensorPolygonShape ) );
				
			}
		}
		
		// ground collision
		float groundCollisionSensorHeight=0.2f;
		
		Vec2[] vertsGroundSensor = new Vec2[]{
			new Vec2(-this.width * 0.25f, -this.height * 0.5f - groundCollisionSensorHeight),
			new Vec2(-this.width * 0.25f, -this.height * 0.5f + groundCollisionSensorHeight),
			new Vec2( this.width * 0.25f, -this.height * 0.5f + groundCollisionSensorHeight),
			new Vec2( this.width * 0.25f, -this.height * 0.5f - groundCollisionSensorHeight)
		};

		PolygonShape sensorPolygonShape = new PolygonShape();
		sensorPolygonShape.set(vertsGroundSensor, vertsGroundSensor.length);
		
		FixtureDef 	fixtureDefSensor = new FixtureDef();
		fixtureDefSensor.shape = sensorPolygonShape;
		fixtureDefSensor.isSensor=true;
		
		this.sensorGroundCollision = new MySensor( this.body.createFixture(fixtureDefSensor), sensorPolygonShape );
		sensorList.add(sensorGroundCollision);
		

		sensorBottomLeft 	= sensorList.get(0);
		sensorTopLeft		= sensorList.get(1);
		sensorBottomRight 	= sensorList.get(2);
		sensorTopRight 		= sensorList.get(3);
	}

	public void draw(Graphics g, boolean debugView){
		
		if(debugView || this.img == null)
			this.drawOutline(g);
		else 
			this.drawImage();
		
	}
	
	public void die(){
		
		System.out.println("You just did dies!");
		this.world.setGravity( new Vec2(0,0) );
		
	}
	
	public void drawImage(){
		
		Vec2 position	= this.body.getPosition();
		float angle		= this.body.getAngle();
		
		img.setRotation(-(float) Math.toDegrees(angle));
		img.draw(position.x - this.width / 2, -position.y - this.height / 2, this.width, this.height);
		
	}
	
	public void drawOutline(Graphics g) {

		
		if( !isRunning() ){
			Polygon polygonToDraw = new Polygon();
			Vec2[] verts = this.firstPolygonShape.getVertices();
			
			for (int i=0; i< this.firstPolygonShape.m_vertexCount; ++i) {
				Vec2 vert = verts[i];
				Vec2 worldPoint = this.body.getWorldPoint(vert);
				polygonToDraw.addPoint(worldPoint.x, -worldPoint.y);
			}
			
			g.draw(polygonToDraw);
		} else {
			
			Polygon polygonToDraw = new Polygon();
			Vec2[] verts = this.secondPolygonShape.getVertices();
			
			for (int i=0; i< this.secondPolygonShape.m_vertexCount; ++i) {
				Vec2 vert = verts[i];
				Vec2 worldPoint = this.body.getWorldPoint(vert);
				polygonToDraw.addPoint(worldPoint.x, -worldPoint.y);
			}
			
			g.draw(polygonToDraw);
		}
	
		
		// draw sensors
		for (MySensor mySensor : sensorList){
			mySensor.draw(g, this.body);
		}
		
	}
	
	public void update() {
		
//		 FIXME radian / degree problem 
//		if ( Math.abs( Math.toDegrees( this.body.getAngle() ) ) > this.maxPlayerRotation){
//			this.body.setTransform(this.body.getPosition(), -10);
//			this.body.m_angularVelocity = -(this.body.m_angularVelocity * 0.75f);
//		}
		
		// if player wouldnt use fixedRotation, this would only allow an specific angle
//		float currentRotation = (float) (Math.toDegrees( this.body.getAngle()) );
//		if( currentRotation < -this.maxPlayerRotation){
//			this.body.setTransform(this.body.getPosition(), (float)Math.toRadians(-this.maxPlayerRotation));
//		}
//		if( currentRotation > this.maxPlayerRotation){
//			this.body.setTransform(this.body.getPosition(), (float)Math.toRadians(this.maxPlayerRotation));
//		}
		
		
		if( this.leftWallColliding() || this.rightWallColliding() ){
			if(this.body.getLinearVelocity().y < 0){
				this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, 1f));	
			}
		}
				
		++groundPoundCounter;
		if(this.groundPoundCounter > 10 && groundPounding){
			this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, groundPoundPower));

		}
//		if(getSensorGroundCollision().isColliding() && this.body.getLinearVelocity().y < 0f){
		if(getSensorGroundCollision().isColliding()){
			groundPounding = false;
			jumping = false;
		}

	}

	public void accelerate(boolean left) {
		
			float velocityX 	= this.body.getLinearVelocity().x;
			float velocityY 	= this.body.getLinearVelocity().y;
			
			if(sensorGroundCollision.isColliding()){
				if( this.isRunning() ){
					accelerationX 	= ACC_RUNNING;
					maxVelocity 	= MAX_VELOCITY_RUNNING;
					if(this.sensorGroundCollision.isColliding()){
						// FIXME problem: man kann jetzt nur mehr im laufen springen fuer den kurzen moment wo spieler boden berührt
						velocityY = 4f; // TODO mit += evtl iwie besser loesbar?
					}
					
				} else {
					accelerationX 	= ACC_WALKING;
					maxVelocity		= MAX_VELOCITY_WALKING;
				}
			}
			
			if (left){
				// TODO Math.abs, verbraucht das mehr rechenleistung? als ob man die checkt obs pos sind?
				accelerationX	= -Math.abs(accelerationX);
				maxVelocity 	= -Math.abs(maxVelocity);
			} else {
				accelerationX	= Math.abs(accelerationX);
				maxVelocity 	= Math.abs(maxVelocity);
			}
			
			if(Math.abs(velocityX + accelerationX) < Math.abs(maxVelocity)){
				velocityX += accelerationX;
			}
			
			this.body.setLinearVelocity(new Vec2(velocityX, velocityY));
			
	}
	
	public void switchHitboxes(){

		this.body.destroyFixture(this.firstFixture);
		this.body.destroyFixture(this.secondFixture);
		
		if( isRunning() && this.body.getLinearVelocity().x != 0){ // TODO 2. if-part passt nicht ganz. funkt nciht, wenn man vom stand wegspringen will...
			this.secondFixture = this.body.createFixture(this.secondFixtureDef);
		} else {
			this.firstFixture = this.body.createFixture(this.firstFixtureDef);

		}
		
	}

	public void jump(){
		this.jumping = true;
		
		if(!groundPounding && (sensorGroundCollision.isColliding() || leftWallColliding() || rightWallColliding() ) ){
			
			float jumpSpeedX = 0; 
					
			if(sensorGroundCollision.isColliding()){
				jumpSpeedX = this.body.getLinearVelocity().x;
			} else if(leftWallColliding()){
				jumpSpeedX = this.jumpPower * this.wallJumpPowerFactor;
			} else if(rightWallColliding()){
				jumpSpeedX = -this.jumpPower * this.wallJumpPowerFactor;
			}
			
			this.body.setLinearVelocity(new Vec2(jumpSpeedX, this.jumpPower));
		}
	}
	public void groundPound(){
//		if(!isGroundPounding){
		if(groundPoundCounter > 50){
			groundPounding = true;
			
			this.groundPoundCounter = 0;
			this.getBody().setLinearVelocity(new Vec2(0f,0f));
		}
	}
	
	public boolean leftWallColliding(){
		return sensorBottomLeft.isColliding() && sensorTopLeft.isColliding();
	}

	public boolean rightWallColliding(){
		return sensorBottomRight.isColliding() && sensorTopRight.isColliding();
	}
	
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		if(this.running != running && this.sensorGroundCollision.isColliding() ){
			this.running = running;
			this.switchHitboxes();
		}
	}

	public Vec2 getCurrentVelocity(){
		return this.body.getLinearVelocity();
	}

	public Body getBody() {
		return body;
	}
	
	public ArrayList<MySensor> getSensorList(){
		return this.sensorList;
	}

	public MySensor getSensorTopLeft() {
		return sensorTopLeft;
	}

	public MySensor getSensorTopRight() {
		return sensorTopRight;
	}

	public MySensor getSensorBottomLeft() {
		return sensorBottomLeft;
	}

	public MySensor getSensorBottomRight() {
		return sensorBottomRight;
	}
	public MySensor getSensorGroundCollision() {
		return sensorGroundCollision;
	}

	
	
}
