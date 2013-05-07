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
	
	private static final float TAILWHIP_DISTANCE = 5;
	private final float MAX_VELOCITY_WALKING = 7f;
	private final float MAX_VELOCITY_RUNNING = 20f;
	private final float ACC_WALKING = 0.5f;
	private final float ACC_RUNNING = 0.75f;

	private int groundPoundCounter = 0;
	private int tailwhipCounter = 0;
	
	private float jumpPower = 20f;
	private float wallJumpPowerFactor = 0.3f;
	private float groundPoundPower = -50f;

	private boolean left = false;
	private boolean running = false;
	private boolean bouncing = false;
	private boolean doTailwhip = false;
	private boolean groundPounding = false;
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

	private Fixture		fixtureTail 		= new Fixture();
	private FixtureDef	fixtureDefTail 		= new FixtureDef();
	private PolygonShape polygonShapeTail	= new PolygonShape();
	
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
		this.firstFixtureDef.friction 	= 0.8f;
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

		// delete old sensors
		for (MySensor mySensor : sensorList){
			this.body.destroyFixture( mySensor.getFixture() );
		}
		this.sensorList = new ArrayList<MySensor>();

		float width = this.width;
		float height = this.height;
		if(this.isRunning()){
			width = this.height;
			height = this.width;
		}
		// wall collision sensors
		float sensorSizeWidth	= width  * 0.125f;
		float sensorSizeHeight	= height * 0.1f;
		float default_xSpace = width*0.5f;
		float default_ySpace = height*0.45f;
		float xSpace = default_xSpace;
		float ySpace = default_ySpace;
		
		for (int i=0;i<2; ++i){
			if(i % 2 == 0){ xSpace = -default_xSpace; } else { xSpace = default_xSpace; }
			for (int j=0;j<2; ++j){
				if(j % 2 == 0){ ySpace = -default_ySpace; } else { ySpace = default_ySpace;	}
				
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
			new Vec2(-width * 0.45f, -height * 0.5f - groundCollisionSensorHeight),
			new Vec2(-width * 0.45f, -height * 0.5f + groundCollisionSensorHeight),
			new Vec2( width * 0.45f, -height * 0.5f + groundCollisionSensorHeight),
			new Vec2( width * 0.45f, -height * 0.5f - groundCollisionSensorHeight)
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
		
		// draw tail while attacking
		if( this.doTailwhip ){	
			Polygon polygonToDraw = new Polygon();
			Vec2[] verts = this.polygonShapeTail.getVertices();
			
			for (int i=0; i< this.polygonShapeTail.m_vertexCount; ++i) {
				Vec2 vert = verts[i];
				Vec2 worldPoint = this.body.getWorldPoint(vert);
				polygonToDraw.addPoint(worldPoint.x, -worldPoint.y);
			}
			
			g.draw(polygonToDraw);
		}
	}
	
	public void update() {
		// XXX evtl da checken, welche hitbox ausrichtung angebracht is
		
//		 F I X M E radian / degree problem 
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
		
		
		if( this.isOnWall() ){
			if(this.body.getLinearVelocity().y < 0){
//				if( (this.leftWallColliding() && this.body.getLinearVelocity().x < 0 ) || (this.rightWallColliding() && this.body.getLinearVelocity().x > 0 )){
					this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, 1f));	
//				}	else {
//					this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, -2f));
//				}
			}
//			if( !this.isOnGround()){
//				this.setRunning(false);
//			}
		}
				
		if(this.groundPoundCounter > 10 && groundPounding){
			this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, groundPoundPower));
		}
		
		// TODO konstante fuer speed
		if( this.tailwhipCounter > 20 && this.doTailwhip ){
			this.tailwhipFinalize();
		}
		
//		if(getSensorGroundCollision().isColliding() && this.body.getLinearVelocity().y < 0f){
		if(getSensorGroundCollision().isColliding()){
			groundPounding = false;
			bouncing = false;
		}
		

		this.adjustHitboxes();
		
		increaseCounters();
		
	}

	public void accelerate() {
		
			float velocityX 	= this.body.getLinearVelocity().x;
			float velocityY 	= this.body.getLinearVelocity().y;
			
			if( this.isOnGround() || this.isOnWall() ){
				adjustVelocity();
			}
			
			if( this.isRunning()){
				if(!bouncing){
//				if(!bouncing && this.isOnGround() && (this.sensorBottomLeft.isColliding() || this.sensorBottomRight.isColliding()) ){
//					velocityY = 1f; // TODO mit += evtl iwie besser loesbar?
					bouncing = true;
				}
			}
			if (left){
				// TODO Math.abs, verbraucht das mehr rechenleistung? als ob man die checkt obs pos sind?
				this.accelerationX	= -Math.abs(this.accelerationX);
				this.maxVelocity 	= -Math.abs(this.maxVelocity);
			} else {
				this.accelerationX	= Math.abs(this.accelerationX);
				this.maxVelocity 	= Math.abs(this.maxVelocity);
			}
			
			if(Math.abs(velocityX + accelerationX) < Math.abs(maxVelocity)){
				velocityX += accelerationX;
			}
			
			this.body.setLinearVelocity(new Vec2(velocityX, velocityY));
			
	}
	
	public void adjustVelocity(){

		if( this.isRunning() ){
			accelerationX 	= ACC_RUNNING;
			maxVelocity 	= MAX_VELOCITY_RUNNING;
			
		} else {
			accelerationX 	= ACC_WALKING;
			maxVelocity		= MAX_VELOCITY_WALKING;
		}
	}
	
	public void adjustHitboxes(){

		// TODO overhead evtl durch lastLeft != left loesbar?
		this.body.destroyFixture(this.firstFixture);
		this.body.destroyFixture(this.secondFixture);
		
		if( isRunning() ){ 
			this.secondFixture = this.body.createFixture(this.secondFixtureDef);
			createSensors();
		} else {
			this.firstFixture = this.body.createFixture(this.firstFixtureDef);
			createSensors();

		}
		
	}

	public void jump(){
		
		if( !groundPounding && (sensorGroundCollision.isColliding() || leftWallColliding() || rightWallColliding() ) ){
			float jumpSpeedX = 0; 
			if( this.isOnGround()){
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
		
		if(groundPoundCounter > 50){
			groundPounding = true;
			
			this.groundPoundCounter = 0;
			this.getBody().setLinearVelocity(new Vec2(0f,0f));
		}
		
		if(this.isRunning()){
			setRunning(false);
		}
		
	}
	
	public void tailwhipInit(){
 
		if( !this.doTailwhip ){
			
			
			this.doTailwhip = true;
			this.tailwhipCounter = 0;
			

			float tailWidth 	= 1.5f;
			float tailHeight	= 0.5f;
			float direction = this.width;
			float distance = TAILWHIP_DISTANCE;
			
			if(this.movesLeft()){
				 direction 	= -direction;
				 distance 	= -distance;
			}
			
			this.getBody().setLinearVelocity( new Vec2(distance, body.getLinearVelocity().y) );
			
			// TODO tail sollte beim normalen gehen nicht zentriert sein, sondern weiter unten
			
			Vec2[] vertsTail = new Vec2[]{
				new Vec2( -tailWidth * 0.5f + direction, tailHeight * 0.5f ),
				new Vec2( -tailWidth * 0.5f + direction, -tailHeight * 0.5f ),
				new Vec2(  tailWidth * 0.5f + direction, -tailHeight * 0.5f ),
				new Vec2(  tailWidth * 0.5f + direction, tailHeight * 0.5f )
			};

			this.polygonShapeTail = new PolygonShape();
			this.polygonShapeTail.set(vertsTail, vertsTail.length);
			
			this.fixtureDefTail = new FixtureDef();
			this.fixtureDefTail.shape = this.polygonShapeTail;
			fixtureDefTail.isSensor=true;
			

			this.tailwhip();
			
			
			
		}
	}
	public void tailwhip(){
		this.fixtureTail = this.body.createFixture(this.fixtureDefTail);
	}
	
	public void tailwhipFinalize(){
		
		float distance = TAILWHIP_DISTANCE;
		
		if(this.movesLeft()){
			 distance 	= -distance;
		}

		this.doTailwhip = false;
		this.getBody().setLinearVelocity( new Vec2(-distance, body.getLinearVelocity().y) );
		this.body.destroyFixture(this.fixtureTail);
		
	}
	
	public boolean leftWallColliding(){
		return sensorBottomLeft.isColliding() && sensorTopLeft.isColliding();
	}

	public boolean rightWallColliding(){
		return sensorBottomRight.isColliding() && sensorTopRight.isColliding();
	}	
	
	
	public void increaseCounters(){
		++groundPoundCounter;
		++tailwhipCounter;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		// performance wenn man if weglaesst?
		if(this.running != running){
			this.running = running;
		}
	}
	
	public boolean isOnGround(){
		return this.sensorGroundCollision.isColliding();
	}
	
	public boolean isOnWall(){
		return this.leftWallColliding() || this.rightWallColliding();
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
	
	public Fixture getTailFixture() {
		return this.fixtureTail;
	}

	public void setLeft ( boolean left){
		this.left = left;
	}
	public boolean movesLeft(){
		return this.left;
	}
	
	
}
