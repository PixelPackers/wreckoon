package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;

public class Player {

	private static final float	TAILWHIP_DISTANCE	= 5f;
	private static final int	TAILWHIP_TIME		= 110;
	private static final int 	GROUNDPOUND_AIRTIME = 30;
	private static final int 	LASER_DURATION 		= 50;
	private static final int 	SHOCK_DURATION		= 200;
//	private final float MAX_VELOCITY_WALKING = 7f;
//	private final float MAX_VELOCITY_RUNNING = 20f;
//	private final float ACC_WALKING = 0.5f;
//	private final float ACC_RUNNING = 0.75f;
//	
	// XXX more friction test
	private final float MAX_VELOCITY_WALKING = 1.75f;
	private final float MAX_VELOCITY_RUNNING = 5f;
	private final float ACC_WALKING = 0.375f;
	private final float ACC_RUNNING = 0.4375f;
	private final float playerFriction = 1f;
	
	private int groundPoundCounter	= 0;
	private int tailwhipCounter		= 0;
	private int idleCounter			= 0;
	private int dizzyCounter		= 0;
	private int dizzyIncrease		=-1;
	private int jumpCounter 		= 0;
	private int laserCounter 		= 0;
	private int biteCounter 		= 0;
	private int floatingCounter 	= 0;
	private int boltCounter			= 0;
	private int pigCounter			= 0;
	
	
	private float jumpPower				= -10f;
	private float wallJumpPowerFactor	= 0.3f;
	private float groundPoundPower		= 12.5f;

	private boolean left			= false;
	private boolean running			= false;
	private boolean doTailwhip		= false;
	private boolean groundPounding	= false;
	private boolean jumpingFromWall	= false;
	private boolean dizzy			= false;
	private boolean dead			= false;
	private boolean deadAndOnGround	= false;	
	private boolean biting			= false;
	private boolean laserActive		= false;
	private boolean ableToGetLaser	= false;
	private boolean laserAble		= false;
	private boolean laserStarted	= false;
	private boolean wasLasering		= false;
	
	private boolean locked = false;
	private boolean godmode = false;
		
	private boolean movementButtonIsDown = false;
	
	//XXX ??
	private float	accelerationX = ACC_WALKING;
	private float	maxVelocity = MAX_VELOCITY_WALKING;
	

	private GameObject 	lockedObject			= null;
	private boolean		charging				= false;
	private float		shootingPower 			= 0f;
	private float 		maxShootingPower 		= 50f * 0.25f;
	private Vec2 		lockedPlayerPosition;
	private	Vec2 		shootingDirection		= new Vec2(1,1);
	private float 		floatingDistanceX		= 3f * 0.25f;
	private float 		floatingDistanceY		= 3f * 0.25f;
	
	private float 		conveyorSpeed			= 0f;
	
	private float		maxPlayerRotation = 10f;
	private World		world;
	
	private float width		= 0.48f;
	private float height	= 0.48f;
	private PolygonShape secondPolygonShape 	= new PolygonShape();
	
	private Body 		body;
	private BodyDef 	bodyDef				= new BodyDef();
	private Fixture 	secondFixture;
	private FixtureDef	secondFixtureDef	= new FixtureDef();

	private Fixture		fixtureTail 		= new Fixture();
	private FixtureDef	fixtureDefTail 		= new FixtureDef();
	private PolygonShape polygonShapeTail	= new PolygonShape();
	
	private MySensor 	sensorTopLeft;
	private MySensor 	sensorTopRight;
	private MySensor	sensorBottomLeft;
	private MySensor 	sensorBottomRight;
	private MySensor	sensorGroundCollision;
	
	private Laser laser;


	private ArrayList<MySensor> sensorList			= new ArrayList<MySensor>();
	
	private HashMap<String, Animation> animations = new HashMap<String, Animation>();
	private Animation currentAnimation;

	private void initAnimations() throws SlickException {
		int scale = 4;
		// XXX evtl auch da eine hashmap verwenden?
		SpriteSheet sheetWalk 		= new SpriteSheet("images/walkcycle.png", 	600/scale, 575/scale);
		SpriteSheet sheetRun 		= new SpriteSheet("images/runcycle.png", 	735/scale, 385/scale);
		SpriteSheet sheetWallJump	= new SpriteSheet("images/walljump.png", 	620/scale, 685/scale);
		SpriteSheet sheetWallIdle	= new SpriteSheet("images/wallidle.png", 	620/scale, 685/scale);
		SpriteSheet sheetTailwhip	= new SpriteSheet("images/tailwhip.png",	770/scale, 360/scale);
		SpriteSheet sheetIdle		= new SpriteSheet("images/idle.png", 		454/scale, 575/scale);
		SpriteSheet sheetGroundpoundRoll	= new SpriteSheet("images/groundpoundroll.png", 600/scale, 540/scale);
		SpriteSheet sheetGroundpoundAir		= new SpriteSheet("images/groundpoundair.png", 600/scale, 540/scale);
		SpriteSheet sheetGroundpoundImpact	= new SpriteSheet("images/groundpoundimpact.png", 600/scale, 540/scale);
		SpriteSheet sheetDeath		= new SpriteSheet("images/death.png", 		730/scale, 320/scale);					// 3
		SpriteSheet sheetDeathAir	= new SpriteSheet("images/deathair.png", 	730/scale, 320/scale);
		SpriteSheet sheetWalkJump	= new SpriteSheet("images/jump.png", 		675/scale, 575/scale);
		SpriteSheet sheetWalkJumpAir= new SpriteSheet("images/jumpair.png", 	675/scale, 575/scale);
		SpriteSheet sheetRunJump	= new SpriteSheet("images/flycycle.png", 	735/scale, 385/scale);
		SpriteSheet sheetBite		= new SpriteSheet("images/bite.png", 		454/scale, 575/scale);					// 4
		SpriteSheet sheetShock		= new SpriteSheet("images/shock.png", 		454/scale, 575/scale);
		SpriteSheet sheetLaser		= new SpriteSheet("images/lasercycle.png", 	600/scale, 540/scale);		 		// 5
		
		Animation animationWallJump = new Animation(sheetWallJump, 	70);
		animationWallJump.setLooping(false);
		
		Animation animationTailwhip = new Animation(sheetTailwhip, 	TAILWHIP_TIME);
		animationTailwhip.setLooping(false);

		Animation animationIdle = new Animation(sheetIdle, 	200);
		animationIdle.setPingPong(true);

		Animation animationGroundpoundRoll = new Animation(sheetGroundpoundRoll, 100);
		animationGroundpoundRoll.setLooping(false);
		Animation animationGroundpoundAir = new Animation(sheetGroundpoundAir, 50);
//		animationGroundpoundRoll.setLooping(false);
		Animation animationGroundpoundImpact = new Animation(sheetGroundpoundImpact, 80);
//		animationGroundpoundImpact.setLooping(false);
		
		Animation animationDeath= new Animation(sheetDeath, 200);
		animationDeath.setLooping(false);

		Animation animationWalkJump = new Animation(sheetWalkJump, 80);
		animationWalkJump.setLooping(false);

		Animation animationBite = new Animation(sheetBite, 100);
		animationBite.setLooping(false);
		
		
		animations.put("run", 			new Animation(sheetRun,			100));
		animations.put("walk", 			new Animation(sheetWalk,		100));
		animations.put("wallJump", 		animationWallJump);
		animations.put("wallIdle", 		new Animation (sheetWallIdle, 	200));
		animations.put("tailwhip", 		animationTailwhip);
		animations.put("idle", 			animationIdle );
		animations.put("groundpound",		animationGroundpoundRoll);
		animations.put("groundpoundAir",	animationGroundpoundAir);
		animations.put("groundpoundImpact",	animationGroundpoundImpact);
		animations.put("death", 		animationDeath);
		animations.put("deathAir", 		new Animation(sheetDeathAir,	 150));
		
		animations.put("walkJump",		animationWalkJump);
		animations.put("walkJumpAir",	new Animation(sheetWalkJumpAir, 100));
		animations.put("runJump",		new Animation(sheetRunJump, 	100));
		animations.put("bite",			animationBite);
		animations.put("shock",			new Animation(sheetShock, 	100));
		animations.put("laser",			new Animation(sheetLaser, 	100));
		
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
		

		this.body = world.createBody(bodyDef);
		
		// FIXME das oder body.sweep verwenden?
		this.body.setFixedRotation(true);
//		this.body.setLinearDamping(0.7f);
				
		
		
		// player body hitbox
		Vec2[] vertsSensor = new Vec2[]{
			new Vec2(-height * 0.5f,  width * 0.5f),
			new Vec2(-height * 0.5f, -width * 0.5f),
			new Vec2( height * 0.5f, -width * 0.5f),
			new Vec2( height * 0.5f,  width * 0.5f)
		};
		
//		Vec2[] vertsSensor = new Vec2[]{				
//				new Vec2( width * 0.25f, -height *  0.5f),
//				new Vec2( width *  0.5f, -height * 0.25f),
//				new Vec2( width *  0.5f,  height * 0.25f),
//				new Vec2( width * 0.25f,  height *  0.5f),
//				new Vec2(-width * 0.25f,  height *  0.5f),
//				new Vec2(-width *  0.5f,  height * 0.25f),
//				new Vec2(-width *  0.5f, -height * 0.25f),
//				new Vec2(-width * 0.25f, -height *  0.5f)
//			};

		PolygonShape secondPolygonShape = new PolygonShape();
		secondPolygonShape.set(vertsSensor, vertsSensor.length);
		
		this.secondFixtureDef.density 	= 11f;
		this.secondFixtureDef.friction 	= playerFriction;
		// XXX braucht ma das? evtl wegen vom reifen wegbouncen? selbes wie oben
//		this.secondFixtureDef.restitution = 0f;
		this.secondFixtureDef.shape = secondPolygonShape;
		this.secondPolygonShape = secondPolygonShape;

		this.secondFixture = this.body.createFixture(secondFixtureDef);
					
		this.createSensors();
	}
	
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}
	
	public void setLaser(Laser laser) {
		this.laser = laser;
	}
	
	public Laser getLaser() {
		return this.laser;
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
		
		for (int i=0; i < 2; ++i){
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
		float groundCollisionSensorHeight = 0.0625f;
		
		Vec2[] vertsGroundSensor = new Vec2[]{
			new Vec2(-width * 0.45f, height * 0.5f - groundCollisionSensorHeight),
			new Vec2(-width * 0.45f, height * 0.5f + groundCollisionSensorHeight),
			new Vec2( width * 0.45f, height * 0.5f + groundCollisionSensorHeight),
			new Vec2( width * 0.45f, height * 0.5f - groundCollisionSensorHeight)
		};

		PolygonShape sensorPolygonShape = new PolygonShape();
		sensorPolygonShape.set(vertsGroundSensor, vertsGroundSensor.length);
		
		FixtureDef 	fixtureDefSensor = new FixtureDef();
		fixtureDefSensor.shape = sensorPolygonShape;
		fixtureDefSensor.isSensor = true;
		
		this.sensorGroundCollision = new MySensor( this.body.createFixture(fixtureDefSensor), sensorPolygonShape );
		sensorList.add(sensorGroundCollision);
		
		sensorBottomLeft 	= sensorList.get(0);
		sensorTopLeft		= sensorList.get(1);
		sensorBottomRight 	= sensorList.get(2);
		sensorTopRight 		= sensorList.get(3);
	}
	
	public void die() {
		if(!godmode){
			this.currentAnimation = animations.get("deathAir");
			this.dead = true;
			this.deadAndOnGround = false;
		}
		lock();
		System.out.println("die.lock()");
		
	}
	
	public void draw(Graphics g, boolean debugView){
//		if (debugView) {
//			this.drawOutline(g);
//		} else { 
//			this.drawImage();
//		}
		
		// sprite testing
		this.drawImage();
		this.drawOutline(g);
		
	}
	
	public void drawImage(){
		Vec2 position	= this.body.getPosition();
		
		// XXX MAGIC NUMBERS
		float drawWidth =  currentAnimation.getWidth() / 150f; 
		float drawHeight = currentAnimation.getHeight() / 150f;
		drawWidth= (left) ? drawWidth: -drawWidth;
		
		currentAnimation.draw( position.x + drawWidth*0.5f,
				position.y - drawHeight*0.5f - 0.5f*0.5f, // -0.5f --> sonst wuerde sprite in den boden hinein stehen 
				-drawWidth, 
				drawHeight);	
	}
	
	public void drawOutline(Graphics g) {

		// body hitbox
		Polygon polygonToDraw = new Polygon();
		Vec2[] verts = this.secondPolygonShape.getVertices();
		
		for (int i=0; i< this.secondPolygonShape.m_vertexCount; ++i) {
			Vec2 vert = verts[i];
			Vec2 worldPoint = this.body.getWorldPoint(vert);
			polygonToDraw.addPoint(worldPoint.x, worldPoint.y);
		}
		g.draw(polygonToDraw);
		
		
		// draw sensors
		for (MySensor mySensor : sensorList){
			mySensor.draw(g, this.body);
		}
		
		// draw tail while attacking
		if( this.doTailwhip ){	
			Polygon polygonTailwhipToDraw = new Polygon();
			Vec2[] vertsTailwhip = this.polygonShapeTail.getVertices();
			
			for (int i=0; i< this.polygonShapeTail.m_vertexCount; ++i) {
				Vec2 vert = vertsTailwhip[i];
				Vec2 worldPoint = this.body.getWorldPoint(vert);
				polygonTailwhipToDraw.addPoint(worldPoint.x, worldPoint.y);
			}
			g.draw(polygonTailwhipToDraw);
		}
		
		// laser
		if (laserActive) {
			laser.drawOutline(g);
		}
			
	}
	
	public float curveValue(float dest, float current, float smoothness) {
		return current + (dest - current) / smoothness;
	}
	
	public void update() { if(!dead) {
		
//		// slow down player if no directionmovment button is pressed
//		if( this.conveyorSpeed == 0 && !this.movementButtonIsDown){
//			float slowDownForce = 0.5f;
//			float slowDownThreshold = 0.15f;
//			// left
//			if(this.getBody().getLinearVelocity().x < -slowDownThreshold && this.isOnGround() ) {
//				this.getBody().applyLinearImpulse(new Vec2(slowDownForce,0), this.getBody().getPosition());
//			} else
//			
//			// right
//			if(this.getBody().getLinearVelocity().x > slowDownThreshold && this.isOnGround() ) {
//				this.getBody().applyLinearImpulse(new Vec2(-slowDownForce,0), this.getBody().getPosition());
//			}
//		}
		
		// float in air while shooting laser
		if ( this.laserStarted ){
//			this.body.setTransform(this.laserStartingPosition, this.body.getAngle());
//			FIXME magic number
			this.body.setLinearVelocity(new Vec2(0f,-0.3f) );
		}
		
		// XXX evtl da checken, welche hitbox ausrichtung angebracht is
				
		
//		// abwaerts bewegung an wand
//		if( this.isOnWall() ){
//			if(this.body.getLinearVelocity().y < 0){
//				if( (this.leftWallColliding() && this.body.getLinearVelocity().x < 0f ) || (this.rightWallColliding() && this.body.getLinearVelocity().x > 0f )){
//					this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, 1f));	
//				}	else {
//					this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, -2f));
//				}
//			}
////			if( !this.isOnGround()){
////				this.setRunning(false);
////			}
//		}
		
		if (this.groundPounding) {
			this.groundpound();	
		}
		
		// TODO konstante fuer speed
		if( this.tailwhipCounter > 20f && this.doTailwhip ){
			this.tailwhipFinalize();
		}
		
//		if(getSensorGroundCollision().isColliding() && this.body.getLinearVelocity().y < 0f){
		if(getSensorGroundCollision().isColliding()){
			
			if(groundPounding || wasLasering){
				this.groundPounding = false;
				dizzyIncrease = -1;
				unlock();
				System.out.println("new unlock");
			}
			this.jumpingFromWall = false;
			wasLasering = false;
			
		}
		

		
		if(dizzyCounter > 0){
			this.dizzy = true;
		} else {
			this.dizzy = false;
		}
//		this.adjustHitboxes();
		
		increaseCounters();
		
		// FIXME das oder fixed rotation verwenden?
		//this.body.m_sweep.a = 0;
		
		this.telekinesis();

		if( this.isOnGround() /*&& !this.isRunning() */&& !this.doTailwhip && idleCounter > 2 && !this.dead && !this.biting && !locked) {
			this.currentAnimation = animations.get("idle");
		}
		
		if( !this.running && !this.isOnGround() && this.currentAnimation.isStopped() ) {
			this.currentAnimation = animations.get("walkJumpAir");
		}
		
		if (this.isJumpingFromWall() && this.currentAnimation.isStopped()) {
			this.currentAnimation = animations.get("runJump");
			this.jumpingFromWall = false;
		}
		
		if (this.currentAnimation == animations.get("wallIdle") && !this.isOnWall()) {
			this.jumpingFromWall = false;
			this.currentAnimation = animations.get("runJump");
		}
		
		if(!this.isOnGround() && !this.isJumpingFromWall() && !this.groundPounding && !this.dead && !this.laserStarted) {
			this.currentAnimation = animations.get("runJump");
		}
		
		if (this.isGroundPounding() && !this.isOnGround() && this.currentAnimation.isStopped()) {
			this.currentAnimation = animations.get("groundpoundAir");
		}
		
		if (this.isOnGround() && this.dizzy){
			this.currentAnimation = animations.get("groundpoundImpact");
		}

		if (this.isOnWall() && !this.isJumpingFromWall() && !this.isOnGround() ) {
			this.currentAnimation = animations.get("wallIdle");
		}
		
		if (this.dead && this.isOnGround() ) {
			
			if ( !this.deadAndOnGround ) {
				this.deadAndOnGround = true;
				
				this.currentAnimation = animations.get("death");
				this.currentAnimation.restart();
			}
		}	
		
		if( laserCounter == LASER_DURATION ){
			destroyLaser();
		}
		
		if (this.biting && this.currentAnimation.isStopped() ) {
			this.currentAnimation = animations.get("shock");
		}

		if	(this.biting && biteCounter == SHOCK_DURATION ) {
			biteFinalize();		
		}


		if (this.laserStarted && this.currentAnimation.isStopped() ) {
			this.currentAnimation = animations.get("laser");
			createLaser();
		}
		
		// TODO �berpr�fen ob das jetzt mit lauf band funkt
		if(this.conveyorSpeed != 0){
			this.getBody().setLinearVelocity( new Vec2(getBody().getLinearVelocity().x + conveyorSpeed, getBody().getLinearVelocity().y) );
		}
	}// !dead end	
	}

	public void accelerate(float magnitude) {
		
		if( locked )
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

		// third approach
		float speed = (left) ? -50 * magnitude : 50 * magnitude;
//		if ( Math.abs(velocityX + accelerationX) < Math.abs(maxVelocity)  ) {
//			this.body.applyForce( new Vec2(speed, 0), this.body.getPosition() );
//		}
		
		if (velocityX + accelerationX <= maxVelocity &&
				velocityX - accelerationX >= -maxVelocity) {
			//this.body.applyForce(new Vec2(speed, 0), this.body.getPosition());
		}
		this.body.applyForce(new Vec2(speed, 0), this.body.getPosition());
		if (velocityX > maxVelocity) this.body.setLinearVelocity(new Vec2(maxVelocity, velocityY));
		if (velocityX < -maxVelocity) this.body.setLinearVelocity(new Vec2(-maxVelocity, velocityY));
		
		//*/
		if(!groundPounding && !doTailwhip && this.isOnGround() && jumpCounter > 5){
//			if(this.running){
//				this.currentAnimation = animations.get("run");
//			} else {
//				this.currentAnimation = animations.get("walk");
//			}
			if (Math.abs(getBody().getLinearVelocity().x) > 1.8d) {
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
	
	public void cancelJump() {
		getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x,
											getBody().getLinearVelocity().y * 0.35f));
	}

	public void jump(){

		if( locked )
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
					jumpSpeedX = -this.jumpPower * 0.5f;
					this.jumpingFromWall = true;
					
				} else if(rightWallColliding()){
					
					this.left = true;
					jumpSpeedX = this.jumpPower * 0.5f;
					this.jumpingFromWall = true;
				
				}
			}
			
			this.body.setLinearVelocity(new Vec2(jumpSpeedX, jumpSpeedY));
		}
		
	}
	public void groundpoundInit(){
		
		if (locked || wasLasering) {
	 		return;
	 	} 
		
		if(groundPoundCounter > 50 && !groundPounding){
			

	 		lock();
	 		System.out.println("groundPoundInit.lock()");
//			FIXME wegen movement evtl doch nicht locken? spzeial lock f�r movement?
			
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

		if( this.groundPoundCounter > GROUNDPOUND_AIRTIME ) {
			this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, groundPoundPower));
			unlock();
			System.out.println("groundpound.unlock()");
		} else {
			this.getBody().setLinearVelocity(new Vec2(0f,0f));
		}
	}
	
	public void tailwhipInit(){
 
		if ( locked ) {
			return;
		}
		
		if ( !this.doTailwhip && !this.groundPounding /* && this.isOnGround()*/ ) {
			
			
			this.doTailwhip = true;
			this.tailwhipCounter = 0;
			

			float tailWidth 	= 0.7f;
			float tailHeight	= 0.2f;
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

		if ( locked ) {
			return;
		}
		
		
		if( !this.isCharging() ) {
			
			if(lockedObject != null){

				int 	directionMultiplier = (this.movesLeft()) ? -1 : 1;

				float 	playerX				= this.getBody().getPosition().x;
				float 	playerY				= this.getBody().getPosition().y;
				
				float 	targetX				= playerX + floatingDistanceX * directionMultiplier;
				float 	targetY				= playerY - floatingDistanceY;
				
				// object tracks player movement
				moveFloatingObjectToTarget( targetX, targetY);
				
			}
		} else { // charging
			
			Vec2 placement = this.shootingDirection.clone();
			placement.normalize();
			placement = placement.mul( (float)Math.sqrt(floatingDistanceX*floatingDistanceX + floatingDistanceY*floatingDistanceY));
			
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
		
		// float speed = 0.5f * 1f/Math.abs(distanceX);
		float speed = MAX_VELOCITY_RUNNING;
		
		float toleranceX = 0.5f;
		
		if(lockObjX + toleranceX < targetX)
			lockedObject.getBody().setLinearVelocity( new Vec2( speed, lockedObject.getBody().m_linearVelocity.y ));
		else if(lockObjX - toleranceX > targetX) 
			lockedObject.getBody().setLinearVelocity( new Vec2(-speed,lockedObject.getBody().m_linearVelocity.y ));
		else { // within tolerance
//			lockedObject.getBody().setLinearVelocity( new Vec2(lockedObject.getBody().m_linearVelocity.x*0.5f, lockedObject.getBody().m_linearVelocity.y ));
			lockedObject.getBody().setLinearVelocity( new Vec2(lockedObject.getBody().m_linearVelocity.x*0.5f, 0 ));
		}	
		
		lockObjY = targetY 	+ (float) ( Math.sin( Math.toRadians( ++floatingCounter % 360f ) ));
		lockedObject.getBody().setTransform(new Vec2(lockObjX, lockObjY), 0);
		
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
	
	// laser
	public void initializeLaser(){
		if(dead){
			unlock();
			System.out.println("initializeLaser.unlock()");
			dead=false;
			return;
		}
		if (locked){
			return;
		} else {
			lock();
			System.out.println("initializeLaser.lock()");
		}
		
		if (true || !this.laserStarted && this.laserAble) {
		
			this.laserStarted = true;
			
			laserCounter = 0;
			this.currentAnimation = animations.get("groundpound");
			this.currentAnimation.restart();
		}
	}
	
	private void createLaser(){

		this.laserActive = true;	
		
		Iterator<Enemy> iterator = laser.getLaserContacts().iterator();
		while (iterator.hasNext()){
			Enemy enemy = (Enemy) iterator.next();
			enemy.die();
			iterator.remove();
		}
		
		godmode = true;
	
	}
	
	public void destroyLaser(){

		this.laserStarted = false;
		this.laserActive = false;
		this.laserAble = false;
		wasLasering = true;
		godmode = false;
		
		unlock();
		System.out.println("destroyLaser.unlock()");

	}
	


	public void bite(){
		if(locked){
			return;
		} else if (this.ableToGetLaser){
			lock();
			System.out.println("bite.lock()");
//			TODO wird this.biting �haupt gebraucht, jetzt mit lock()?
			if(!this.biting){
				this.biting = true;
				this.biteCounter = 0;
				
				this.currentAnimation = animations.get("bite");
				this.currentAnimation.restart();	
			}
		}
		
	}
	
	private void biteFinalize(){
		this.biting = false;
		this.currentAnimation = animations.get("idle");
		this.laserAble = true;
		unlock();
		System.out.println("biteFinalize.unlock()");
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
		++laserCounter;
		++biteCounter;
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
		if( locked ) {
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
	
	public boolean isLaserActive() {
		return laserActive;
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
			return secondFixture; 
	}
	
//	public boolean shouldntMove() {
//		return this.dizzy && false;
////		mit charging funkt telekinese ziel steuerung nicht mehr...
////		return this.isCharging() || this.dizzy;
//	}
	
	public void setMovementButtonIsDown(boolean movementButtonIsDown) {
		this.movementButtonIsDown = movementButtonIsDown;
	}
	
	public void setAbleToGetLaser(boolean ableToGetLaser) {
		this.ableToGetLaser = ableToGetLaser;
	}
	public void setConveyorSpeed(float conveyorSpeed) {
		this.conveyorSpeed = conveyorSpeed;
	}
	public boolean isLocked() {
		return locked;
	}
	public void lock(){
		locked = true;
	}
	
	public void unlock(){
		locked = false;
	}
	public boolean isDead() {
		return dead;
	}
	public int getBoltCounter() {
		return boltCounter;
	}
	public void increaseBoltCounter(){
		++boltCounter;
	}
	public int getPigCounter() {
		return pigCounter;
	}
	public void increasePigCounterCounter(){
		++pigCounter;
	}
}
