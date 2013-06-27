package Game;

import java.util.ArrayList;
import java.util.HashMap;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;

public class Player {

	private static final float	TAILWHIP_DISTANCE	= 20f;
	private static final int	TAILWHIP_TIME		= 110;
	private static final int 	GROUNDPOUND_AIRTIME = 30;
//	private final float MAX_VELOCITY_WALKING = 7f;
//	private final float MAX_VELOCITY_RUNNING = 20f;
//	private final float ACC_WALKING = 0.5f;
//	private final float ACC_RUNNING = 0.75f;
//	
	// XXX more friction test
	private final float MAX_VELOCITY_WALKING = 7f;
	private final float MAX_VELOCITY_RUNNING = 20f;
	private final float ACC_WALKING = 1.5f;
	private final float ACC_RUNNING = 1.75f;
	private final float playerFriction = 1f;
	
	private int groundPoundCounter	= 0;
	private int tailwhipCounter		= 0;
	private int idleCounter			= 0;
	private int dizzyCounter		= 0;
	private int dizzyIncrease		= -1;
	private int jumpCounter 		= 0;
	
	
	private float jumpPower = 20f;
	private float wallJumpPowerFactor = 0.3f;
	private float groundPoundPower = -50f;

	private boolean left = false;
	private boolean running = false;
	private boolean doTailwhip = false;
	private boolean groundPounding = false;
	private boolean jumpingFromWall = false;
	private boolean dizzy = false;
	private boolean dead = false;
	
	//XXX ??
	private float	accelerationX = ACC_WALKING;
	private float	maxVelocity = MAX_VELOCITY_WALKING;
	

	private GameObject 	lockedObject = null;
	private boolean		charging = false;
	private float		shootingPower 		= 0f;
	private float 		maxShootingPower 	= 50f;
	private Vec2 		lockedPlayerPosition;
	private	Vec2 		shootingDirection = new Vec2(1,1);
	
	private float		maxPlayerRotation = 10f;
	private World		world;
	
	private float width		= 1f;
	private float height	= 2f;
	private PolygonShape firstPolygonShape 		= new PolygonShape();
	private PolygonShape secondPolygonShape 	= new PolygonShape();
	
	private Body 		body;
	private BodyDef 	bodyDef				= new BodyDef();
	private Fixture 	firstFixture;
	private Fixture 	secondFixture;
	private FixtureDef 	firstFixtureDef 	= new FixtureDef();
	private FixtureDef	secondFixtureDef	= new FixtureDef();

	private FixtureDef	firstfixtureDefWheel = new FixtureDef();
	private Fixture 	firstFixtureWheel;

	private Fixture		fixtureTail 		= new Fixture();
	private FixtureDef	fixtureDefTail 		= new FixtureDef();
	private PolygonShape polygonShapeTail	= new PolygonShape();
	

	private Fixture		fixtureLaser 		= new Fixture();
	private FixtureDef	fixtureDefLaser 	= new FixtureDef();
	private PolygonShape polygonShapeLaser	= new PolygonShape();
	
	private MySensor 	sensorTopLeft;
	private MySensor 	sensorTopRight;
	private MySensor	sensorBottomLeft;
	private MySensor 	sensorBottomRight;
	private MySensor	sensorGroundCollision;

	private ArrayList<MySensor> sensorList			= new ArrayList<MySensor>();
	private Image 		img;
	
	private HashMap<String, Animation> animations = new HashMap<String, Animation>();
	private Animation currentAnimation;

	private void initAnimations() throws SlickException {
		
		// XXX evtl auch da eine hashmap verwenden?
		SpriteSheet sheetWalk 		= new SpriteSheet("images/walkcycle.png", 	600, 575);
		SpriteSheet sheetRun 		= new SpriteSheet("images/runcycle.png", 	735, 385);
		SpriteSheet sheetWallJump	= new SpriteSheet("images/walljump.png", 	620, 685);
		SpriteSheet sheetTailwhip	= new SpriteSheet("images/tailwhip.png",	770, 360);
		SpriteSheet sheetIdle		= new SpriteSheet("images/idle.png", 		454, 575);
		SpriteSheet sheetGroundpound= new SpriteSheet("images/groundpound.png", 600, 540);
		SpriteSheet sheetDeath		= new SpriteSheet("images/death.png", 730, 320);
		SpriteSheet sheetWalkJump	= new SpriteSheet("images/jump.png", 675, 575);
		SpriteSheet sheetRunJump	= new SpriteSheet("images/flycycle.png", 735, 385);
		
		Animation animationWallJump = new Animation(sheetWallJump, 	100);
		animationWallJump.setLooping(false);
		
		Animation animationTailwhip = new Animation(sheetTailwhip, 	TAILWHIP_TIME);
		animationTailwhip.setLooping(false);

		Animation animationIdle = new Animation(sheetIdle, 	200);
		animationIdle.setPingPong(true);
		
		Animation animationGroundpound = new Animation(sheetGroundpound, 100);
		animationGroundpound.setLooping(false);
		
		Animation animationDeath= new Animation(sheetDeath, 200);
		animationDeath.setLooping(false);
		
		Animation animationWalkJump = new Animation(sheetWalkJump, 80);
		animationWalkJump.setLooping(false);
		
		
		animations.put("run", 			new Animation(sheetRun,			100));
		animations.put("walk", 			new Animation(sheetWalk,		100));
		animations.put("wallJump", 		animationWallJump);
		animations.put("tailwhip", 		animationTailwhip);
		animations.put("idle", 			animationIdle );
		animations.put("groundpound",	animationGroundpound);
		animations.put("death", 		animationDeath);
		animations.put("walkJump",		animationWalkJump);
		animations.put("runJump",		new Animation(sheetRunJump, 	100));
		
		currentAnimation = animations.get("idle");
	}
	
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
		
		initAnimations();
		
		this.firstPolygonShape.set(polygonShapeVerts, polygonShapeVerts.length);
		
		this.firstFixtureDef.density 	= 11f;
		this.firstFixtureDef.friction 	= playerFriction;
		// XXX braucht ma das? evtl wegen vom reifen wegbouncen?
		this.firstFixtureDef.restitution = 0f;
		this.firstFixtureDef.shape = firstPolygonShape;
		
		this.img = new Image("images/player.png");

		this.body = world.createBody(bodyDef);
		this.firstFixture = this.body.createFixture(firstFixtureDef);
		// FIXME das oder body.sweep verwenden?
		this.body.setFixedRotation(true);
//		this.body.setLinearDamping(0.7f);
		
		// wheel
		/// XXX GAY GAY GAY
		CircleShape circleShape = new CircleShape();
		circleShape.m_radius = 0.5f;
		
		this.firstfixtureDefWheel.shape = circleShape;
		this.firstFixtureWheel = this.body.createFixture(this.firstfixtureDefWheel);
		
		
		
		
		
		// second hitbox
		Vec2[] vertsSensor = new Vec2[]{
			new Vec2(-height * 0.5f,  width * 0.5f),
			new Vec2(-height * 0.5f, -width * 0.5f),
			new Vec2( height * 0.5f, -width * 0.5f),
			new Vec2( height * 0.5f,  width * 0.5f)
		};

		PolygonShape secondPolygonShape = new PolygonShape();
		secondPolygonShape.set(vertsSensor, vertsSensor.length);
		
		this.secondFixtureDef.density 	= 11f;
		this.secondFixtureDef.friction 	= playerFriction;
		// XXX braucht ma das? evtl wegen vom reifen wegbouncen? selbes wie oben
//		this.secondFixtureDef.restitution = 0f;
		this.secondFixtureDef.shape = secondPolygonShape;
		this.secondPolygonShape = secondPolygonShape;

		this.secondFixture = this.body.createFixture(secondFixtureDef);
		
		// wheels
//		this
		// 2 kreise die sich rotieren lassen am player fixen, an jeweiliger hitbox orientieren
		
		
		// eye laser
		Vec2[] vertsLaser = new Vec2[]{
			new Vec2( this.getBody().getPosition() ),
			new Vec2( this.getBody().getPosition().x,		this.getBody().getPosition().y + 1),
			new Vec2( this.getBody().getPosition().x + 50,	this.getBody().getPosition().y + 1),
			new Vec2( this.getBody().getPosition().x + 50,	this.getBody().getPosition().y)
		};

		this.polygonShapeLaser = new PolygonShape();
		this.polygonShapeLaser.set(vertsLaser, vertsLaser.length);
		
		this.fixtureDefLaser = new FixtureDef();
		this.fixtureDefLaser.shape = this.polygonShapeLaser;
		fixtureDefLaser.isSensor = true;
		
		this.fixtureLaser = this.body.createFixture(this.fixtureDefLaser);
//		this.body.destroyFixture(this.fixtureLaser);
	
		this.createSensors();
		this.adjustHitboxes();		
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
	
	public void die() {
		
		this.currentAnimation = animations.get("death");
		this.currentAnimation.restart();
		
		this.dead = true;
		
	}
	
	public void draw(Graphics g, boolean debugView){
		if (debugView || this.img == null) {
			this.drawOutline(g);
		} else { 
			this.drawImage();
		}
		
		// sprite testing
//		this.drawImage();
//		this.drawOutline(g);
		
	}
	
	public void drawImage(){
		Vec2 position	= this.body.getPosition();
		
		// XXX MAGIC NUMBERS
		float drawWidth =  currentAnimation.getWidth() / 150; 
		float drawHeight = currentAnimation.getHeight() / 150;
		drawWidth= (left) ? drawWidth: -drawWidth;
		
		currentAnimation.draw( position.x + drawWidth*0.5f,
				-position.y - drawHeight*0.5f - 0.5f, // -0.5f --> sonst wuerde sprite in den boden hinein stehen 
				-drawWidth, 
				drawHeight);	
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
		
		// augenlaser

		Polygon polygonToDraw = new Polygon();
		Vec2[] verts = this.polygonShapeLaser.getVertices();
		
		for (int i=0; i< this.polygonShapeLaser.m_vertexCount; ++i) {
			Vec2 vert = verts[i];
			Vec2 worldPoint = this.body.getWorldPoint(vert);
			polygonToDraw.addPoint(worldPoint.x, -worldPoint.y);
		}
		
		g.draw(polygonToDraw);
		
	}
	
	public void update() {
		// XXX evtl da checken, welche hitbox ausrichtung angebracht is
				
		
		// abwaerts bewegung an wand
		if( this.isOnWall() ){
			if(this.body.getLinearVelocity().y < 0){
//				if( (this.leftWallColliding() && this.body.getLinearVelocity().x < 0 ) || (this.rightWallColliding() && this.body.getLinearVelocity().x > 0 )){
//					this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, 1f));	
//				}	else {
//					this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, -2f));
//				}
			}
//			if( !this.isOnGround()){
//				this.setRunning(false);
//			}
		}
		if (this.groundPounding) {
			this.groundpound();	
		}
		
		// TODO konstante fuer speed
		if( this.tailwhipCounter > 20 && this.doTailwhip ){
			this.tailwhipFinalize();
		}
		
//		if(getSensorGroundCollision().isColliding() && this.body.getLinearVelocity().y < 0f){
		if(getSensorGroundCollision().isColliding()){
			if(groundPounding){
				this.groundPounding = false;
				dizzyIncrease = -1;
			}
			this.jumpingFromWall = false;
		}
		

		
		if(dizzyCounter > 0){
			this.dizzy = true;
		} else {
			this.dizzy = false;
		}
//		this.adjustHitboxes();
		
		increaseCounters();
		
		// FIXME das oder fixed rotation verwenden?
//		this.body.m_sweep.a = 0;
		
		this.telekinesis();

		if( this.isOnGround() && !this.isRunning() && !this.doTailwhip && idleCounter > 2 && !this.dead){
			this.currentAnimation = animations.get("idle");
		}
		
	}

	public void accelerate() {
		
		if( shouldntMove() )
			return;
		
		float velocityX 	= this.body.getLinearVelocity().x;
		float velocityY 	= this.body.getLinearVelocity().y;
		
		if( this.isOnGround() || this.isOnWall() ){
			adjustVelocity();
		}
		/*
		
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
		
		/*/
		// another movement approach by using linearImpulse
		/// XXX MAGIC NUMBERS
//		float speed = (left) ? -15 : 15;
//		if ( Math.abs(velocityX + accelerationX) < Math.abs(maxVelocity)*1.5f  ) {
//			this.body.applyLinearImpulse(new Vec2(speed, 0), this.body.getPosition());
//		}

		float speed = (left) ? -800 : 800;
		if ( Math.abs(velocityX + accelerationX) < Math.abs(maxVelocity)  ) {
			this.body.applyForce( new Vec2(speed, 0), this.body.getPosition() );
		}
		//*/
		if(!groundPounding && !doTailwhip && this.isOnGround() && jumpCounter > 5){
			if(this.running){
				this.currentAnimation = animations.get("run");
			} else {
				this.currentAnimation = animations.get("walk");
			}
		}
		idleCounter = 0;
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

		if( this.shouldntMove() )
			return;
		
		if( !groundPounding && (sensorGroundCollision.isColliding() || leftWallColliding() || rightWallColliding() ) ) {

			jumpCounter = 0;
			
			// normal jump / on ground
			float jumpSpeedX = this.body.getLinearVelocity().x;
			float jumpSpeedY = this.jumpPower; 
			
			if (this.isOnGround()){

				if ( this.isRunning() ) {
					this.currentAnimation = animations.get("runJump");
				} else {
					this.currentAnimation = animations.get("walkJump");
					this.currentAnimation.restart();
				}
				
			} else {

				this.currentAnimation = animations.get("wallJump");
				this.currentAnimation.restart();
				
				if(leftWallColliding()){
					
					this.left = false;
					jumpSpeedX = this.jumpPower * 0.5f;
					this.jumpingFromWall = true;
					
				} else if(rightWallColliding()){
					
					this.left = true;
					jumpSpeedX = -this.jumpPower * 0.5f;
					this.jumpingFromWall = true;
				
				}
			}
			
			this.body.setLinearVelocity(new Vec2(jumpSpeedX, jumpSpeedY));
		}
		
	}
	public void groundpoundInit(){
		
		if(groundPoundCounter > 50){
			
			this.currentAnimation = animations.get("groundpound");
			this.currentAnimation.restart();
			
			this.groundPounding = true;
			this.groundPoundCounter = 0;
			this.dizzyCounter = 0;
			this.dizzyIncrease = 1; // positive
			
			this.groundpound();
			
		}
		
		if(this.isRunning()) {
			setRunning(false);
		}
		
	}
	
	private void groundpound(){
		
		if(this.groundPoundCounter > GROUNDPOUND_AIRTIME ) {
			this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, groundPoundPower));
		} else {
			this.getBody().setLinearVelocity(new Vec2(0f,0f));
		}
	}
	
	public void tailwhipInit(){
 
		if (this.shouldntMove()) {
			return;
		}
		
		if ( !this.doTailwhip && !this.groundPounding /* && this.isOnGround()*/ ) {
			
			
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
			this.fixtureDefTail.isSensor = true;
			
			this.currentAnimation = animations.get("tailwhip");
			this.currentAnimation.restart();
			
			this.running = true;
			adjustHitboxes();
			this.tailwhip();
			
			
			
		}
	}
	private void tailwhip(){
		this.fixtureTail = this.body.createFixture(this.fixtureDefTail);
	}
	
	private void tailwhipFinalize(){
		
		float distance = TAILWHIP_DISTANCE;
		
		if(this.movesLeft()){
			 distance 	= -distance;
		}

		this.doTailwhip = false;
		if(isOnGround() && false){
			this.getBody().setLinearVelocity( new Vec2(-distance, body.getLinearVelocity().y) );
		}

		this.body.destroyFixture(this.fixtureTail);
		
	}
	
	public boolean leftWallColliding(){
		return sensorBottomLeft.isColliding() && sensorTopLeft.isColliding();
	}

	public boolean rightWallColliding(){
		return sensorBottomRight.isColliding() && sensorTopRight.isColliding();
	}	
	
	public void telekinesis(){

		if (this.shouldntMove()) {
			return;
		}
		
		float 	floatingDistanceX	= 2f;
		float 	floatingDistanceY	= 2f;
		
		if( !this.isCharging() ) {
			
			if(lockedObject != null){

				int 	directionMultiplier = (this.movesLeft()) ? -1 : 1;

				float 	playerX				= this.getBody().getPosition().x;
				float 	playerY				= this.getBody().getPosition().y;
				
				float 	targetX				= playerX + floatingDistanceX * directionMultiplier;
				float 	targetY				= playerY + floatingDistanceY;
				
				// object tracks player movement
				moveFloatingObjectToTarget( targetX, targetY);
				
			}
		} else { // charging
			
			Vec2 placement = this.shootingDirection.clone();
			placement.normalize();
			placement = placement.mul(3f);
			
			//*
			lockedObject.getBody().setTransform(new Vec2(
					this.getBody().getPosition().x + placement.x,
					this.getBody().getPosition().y + placement.y 
					), 0);
			
			Vec2 antiGravity = this.world.getGravity().negate();
			antiGravity = antiGravity.mul(lockedObject.getBody().getMass());
			lockedObject.getBody().applyForce(antiGravity , lockedObject.getBody().getPosition());
			
			/*/
			moveFloatingObjectToTarget(this.getBody().getPosition().x + placement.x, this.getBody().getPosition().y + placement.y);
			//*/
			
		}
		
	}
	
	private void moveFloatingObjectToTarget( float targetX, float targetY ){

		float	lockObjX			= lockedObject.getBody().getPosition().x;
		float	lockObjY			= lockedObject.getBody().getPosition().y;
	
		/* braucht ma alles nicht bzw nur f�r speed, der grad auskommentiert is
		float	distanceX	=	Math.abs( lockObjX ) - Math.abs( this.getBody().getPosition().x ) ;
		float	distanceY	=	Math.abs( lockObjY ) - Math.abs( this.getBody().getPosition().y ) ;

		// minimal distance
		if(distanceX < 0.5f){
			distanceX=0.5f;
		}//*/
		
		// XXX
		// float speed = 0.5f * 1f/Math.abs(distanceX);
		float speed = MAX_VELOCITY_RUNNING;
		
		float toleranceX = 1f;
		
		if(lockObjX + toleranceX < targetX)
			lockedObject.getBody().setLinearVelocity( new Vec2( speed, lockedObject.getBody().m_linearVelocity.y ));
		else if(lockObjX - toleranceX > targetX) 
			lockedObject.getBody().setLinearVelocity( new Vec2(-speed,lockedObject.getBody().m_linearVelocity.y ));
		else // within tolerance
			lockedObject.getBody().setLinearVelocity( new Vec2(lockedObject.getBody().m_linearVelocity.x*0.5f,lockedObject.getBody().m_linearVelocity.y ));
			
		
		// XXX telekinesis work in progress
		float toleranceY= 0f;
		if(lockObjY + toleranceY < targetY){ // should move up
//			lockedObject.getBody().setLinearVelocity( new Vec2( lockedObject.getBody().m_linearVelocity.x, speed
//					-( this.world.getGravity().y) 
//					));			

			float max = 2f;
			float slowDownFactor = 1.4f;
			float distanceFactor = Math.abs(targetX - lockedObject.getBody().getLinearVelocity().y);
			distanceFactor = (float) Math.sqrt(distanceFactor) * slowDownFactor;

//			System.out.print(distanceFactor + "    ");
			distanceFactor = (distanceFactor > max) ? max : distanceFactor;
//			System.out.println(distanceFactor);
			
			Vec2 antiGravity = this.world.getGravity().clone();
			antiGravity = antiGravity.mul(distanceFactor * lockedObject.getBody().getMass()).negate();
//			lockedObject.getBody().applyLinearImpulse(antiGravity, lockedObject.getBody().getWorldCenter());
			lockedObject.getBody().applyForce(antiGravity, lockedObject.getBody().getWorldCenter());
			
		} else if(lockObjY - toleranceY > targetY) { // should move down 
			// let gravity do the work...
//			lockedObject.getBody().setLinearVelocity( new Vec2( lockedObject.getBody().m_linearVelocity.x, -speed ));
		}	else { // within tolerance
			
		}
		// balloon example...
//		lockedObject.getBody().applyForce(this.world.getGravity().negate().mul(lockedObject.getBody().getMass()), lockedObject.getBody().getWorldCenter()	);
		
		
		// XXX is setting object to target position (no transition animation)
//		lockedObject.getBody().setTransform(new Vec2(targetX, targetY), 0);
//		lockedObject.getBody().setTransform(new Vec2(lockObjX, targetY), 0);
		
		
		// XXX ANTIGRAVITY remove every force in any direction --> float / stay in place
//		lockedObject.getBody().setLinearVelocity(new Vec2(0,0));
		
	}
	
	public void startCharging(){

		this.setShootingPower(0);
		this.setCharging(true);
		this.setLockedPlayerPosition(this.getBody().getPosition());
		this.shootingDirection = new Vec2(
				lockedObject.getBody().getPosition().x - this.getBody().getPosition().x,
				lockedObject.getBody().getPosition().y - this.getBody().getPosition().y
				);
		}
	
	public void shoot(){

		this.shootingDirection.normalize();
	
		lockedObject.getBody().setLinearVelocity(shootingDirection.mul(this.getShootingPower() ) );

		
		this.releaseObject();
		this.setCharging(false);
	}
	
	private void increaseCounters(){
		++groundPoundCounter;
		++tailwhipCounter;
		if(shootingPower < maxShootingPower){
			++shootingPower;
		}
		++idleCounter;
		dizzyCounter += dizzyIncrease;
		++jumpCounter;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		// performance wenn man if weglaesst?
		if(this.running != running){
			this.running = running;

			adjustHitboxes();
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
		if( this.shouldntMove() ) {
			return;
		}
		
		this.left = left;
	}
	
	public boolean movesLeft(){
		return this.left;
	}
	
	public float getCurrentMaxSpeed(){
		return (this.isRunning()) ? this.MAX_VELOCITY_RUNNING : this.MAX_VELOCITY_WALKING;
	}
	
	public void lockObject(GameObject lockObj){
		this.lockedObject = lockObj;
	}
	public void releaseObject(){
		if(this.hasLockedObject()){
			lockedObject = null;
		}
	}
	
	public boolean hasLockedObject(){
		return this.lockedObject != null;
	}
	
	
	public boolean isCharging(){
		return this.charging;
	}
	
	public void setCharging(boolean charging){
		this.charging = charging;
	}

	public Vec2 getLockedPlayerPosition() {
		return lockedPlayerPosition;
	}

	public void setLockedPlayerPosition(Vec2 lockedPlayerPosition) {
		this.lockedPlayerPosition = lockedPlayerPosition;
	}

	public float getShootingPower() {
		return shootingPower;
	}

	public void setShootingPower(float shootingPower) {
		this.shootingPower = shootingPower;
	}
	
	public Vec2 getShootingDirection() {
		return shootingDirection;
	}
	
	public GameObject getLockedObject(){
		return this.lockedObject;
	}
	public void increaseShootingDirection(float x, float y){
		
		int maxRadius = 25;
		this.shootingDirection.x += x;
		this.shootingDirection.y += y;

		this.shootingDirection.normalize();
		this.shootingDirection = this.shootingDirection.mul(maxRadius);
			
	}
	
//	public void setShootingPower(int shootingPower) {
//		this.shootingPower = shootingPower;
//	}

	public boolean isGroundPounding() {
		return groundPounding;
	}
	
	public boolean isJumpingFromWall(){
		return this.jumpingFromWall;
	}
	
	public Fixture getFixture(){
		
		if(running){
			return secondFixture;
		} else {
			return firstFixture;
		}
		
	}
	
	public boolean shouldntMove() {
		return this.dizzy;
//		mit charging funkt telekinese ziel steuerung nicht mehr...
//		return this.isCharging() || this.dizzy;
	}
}
