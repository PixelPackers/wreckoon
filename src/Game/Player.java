package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;

public class Player {
	
	private static final float			DENSITY						= 11f;
	private static final float			TAILWHIP_DISTANCE			= 5f;
	private static final int			TAILWHIP_TIME				= 110;
	private static final int			TAILWHIP_DELAY				= 5;
	private static final int			TAILWHIP_DURATION			= 20;
	private static final int			GROUNDPOUND_AIRTIME			= 30;
	private static final int			MAX_LASER_DURATION			= 250;
	private static final int			DEATH_WAIT_TIME				= 70;
	private static final int			GROUNDPOUND_DELAY			= 50;
	private static final int			REPAIR_BOLT_PRICE			= 100;
	private static final int			REPAIR_TIME					= 100;
	private static final int			GODMODE_DURATION			= 100;
	
	private final float					MAX_VELOCITY				= 5f;
	private final float					ACC_RUNNING					= 0.4375f;
	private final float					FRICTION					= 1f;
	
	private int							groundPoundCounter			= 0;
	private int							tailwhipCounter				= 0;
	private int							tailwhipDelayCounter		= 0;
	private int							idleCounter					= 0;
	private int							jumpCounter					= 0;
	private int							laserCounter				= 0;
	private int							biteCounter					= 0;
	private int							floatingCounter				= 0;
	private int							boltCounter					= 0;
	private int							repairTimeCounter			= 0;
	private int							godmodeCounter				= 0; // nach spawn kurz unverwundbar
	
	private int							tmpBoltAmount				= 110;
	private int							pigCounter					= 0;
	private int							deathTimeCounter			= 0;
	private int							laserTime					= 50;
	
	private float						jumpPower					= -10f * 2f;
	private float						wallJumpPowerFactor			= 0.3f;
	private float						groundPoundPower			= 12.5f;
	
	private boolean						left						= false;
	private boolean						running						= false;
	private boolean						doTailwhip					= false;
	private boolean						groundPounding				= false;
	private boolean						jumpingFromWall				= false;
	private boolean						dizzy						= false;
	private boolean						dead						= false;
	private boolean						deadAndOnGround				= false;
	private boolean						biting						= false;
	private boolean						biteShockLoading			= false;
	private boolean						laserActive					= false;
	private boolean						ableToGetLaser				= false;
	// private boolean laserAble = false;
	private boolean						laserStarted				= false;
	private boolean						wasLasering					= false;
	private boolean						waitingForLaserToBeKilled	= false;
	private boolean						frontflipping				= false;
	private boolean						stopBiting					= false;
	private boolean						isGoingToCreateTailwhip		= false;
	private boolean						repairing					= false;
	
	private boolean						locked						= false;
	private boolean						godmode						= false;
	
	private GameObjectCircle			wheel;
	
	private float						conveyorSpeed				= 0f;
	
	private Game						game;
//	private World						world;
	
	private float						width						= 0.48f;
	private float						height						= 0.48f;
	private PolygonShape				boxPolygonShape				= new PolygonShape();
	
	private Body						body;
	private BodyDef						bodyDef						= new BodyDef();
	private Fixture						boxFixture;
	private FixtureDef					boxFixtureDef				= new FixtureDef();
	
	private Fixture						fixtureTail					= new Fixture();
	private FixtureDef					fixtureDefTail				= new FixtureDef();
	private PolygonShape				polygonShapeTail			= new PolygonShape();
	
	private MySensor					sensorTopLeft;
	private MySensor					sensorTopRight;
	private MySensor					sensorBottomLeft;
	private MySensor					sensorBottomRight;
	private MySensor					sensorGroundCollision;
	
	private Laser						laser;
	
	private ArrayList<DropItem>			dropItemsToCollect			= new ArrayList<DropItem>();
	
	private ArrayList<MySensor>			sensorList					= new ArrayList<MySensor>();
	
	private HashMap<String, Animation>	animations					= new HashMap<String, Animation>();
	private HashMap<String, Vec2>		animationOffsets			= new HashMap<String, Vec2>();
	private Animation					currentAnimation;
	private Checkpoint					lastCheckpoint				= new Checkpoint(2f, 10f, 3f, 11f);
	private Generator					generator					= null;
	
	public Player(Game game, float posX, float posY) throws SlickException {
		
		this.game = game;
//		this.world = world;
		this.bodyDef.type = BodyType.DYNAMIC;
		this.bodyDef.position.set(posX, posY);
		
		// @formatter:off
		Vec2[] polygonShapeVerts = new Vec2[] {
				new Vec2(-width * 0.5f,  height * 0.5f),
				new Vec2(-width * 0.5f, -height * 0.5f),
				new Vec2( width * 0.5f, -height * 0.5f),
				new Vec2( width * 0.5f,  height * 0.5f)
		};
		// @formatter:on
		
		initAnimations();
		
		this.body = game.getWorld().createBody(bodyDef);
		
		wheel = new GameObjectCircle(game.getWorld(), 0f, 0f, width * 0.5f, 5f, FRICTION, 0f, null, BodyType.DYNAMIC);
		wheel.getBody().setAngularDamping(100000000f);
		
		Filter filter = new Filter();
		filter.maskBits = 1 + 4 + 8;
		filter.categoryBits = 2;
		wheel.getFixture().setFilterData(filter);
		
		RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
		
		// revoluteJointDef.enableMotor = true;
		// revoluteJointDef.motorSpeed = 200;
		// revoluteJointDef.maxMotorTorque = 100;
		
		revoluteJointDef.bodyA = getBody();
		revoluteJointDef.bodyB = wheel.getBody();
		revoluteJointDef.collideConnected = false;
		revoluteJointDef.localAnchorA.set(new Vec2(0f, 0f));
		revoluteJointDef.localAnchorB.set(new Vec2(0f, 0f));
		game.getWorld().createJoint(revoluteJointDef);
		
		this.body.setFixedRotation(true);
		
		// @formatter:off
		Vec2[] vertsPlayerBox = new Vec2[] {
				new Vec2(-width * 0.45f,  height *   0f),
				new Vec2(-width * 0.45f, -height * 0.5f),
				new Vec2( width * 0.45f, -height * 0.5f),
				new Vec2( width * 0.45f,  height *   0f)
		};
		// @formatter:on
		
		PolygonShape playerPolygonShape = new PolygonShape();
		playerPolygonShape.set(vertsPlayerBox, vertsPlayerBox.length);
		
		this.boxFixtureDef.density = DENSITY;
		this.boxFixtureDef.friction = FRICTION;
		this.boxFixtureDef.shape = playerPolygonShape;
		this.boxPolygonShape = playerPolygonShape;
		
		boxFixtureDef.filter.categoryBits = 2;
		boxFixtureDef.filter.maskBits = 1 + 4 + 8;
		
		this.boxFixture = this.body.createFixture(boxFixtureDef);
		
		this.createSensors();
	}
	
	public void accelerate(float magnitude) {
		if (!locked || (locked && groundPounding) ) {
			float velocityX = this.body.getLinearVelocity().x;
			float velocityY = this.body.getLinearVelocity().y;
			
			float speed = (left) ? -50 * magnitude : 50 * magnitude;
			
			this.body.applyForce(new Vec2(speed, 0), this.body.getPosition());
			if (velocityX > MAX_VELOCITY)
				this.body.setLinearVelocity(new Vec2(MAX_VELOCITY, velocityY));
			if (velocityX < -MAX_VELOCITY)
				this.body.setLinearVelocity(new Vec2(-MAX_VELOCITY, velocityY));
			
			if (!groundPounding && !isGoingToCreateTailwhip && !doTailwhip && this.isOnGround() && jumpCounter > 5) {
				if (Math.abs(getBody().getLinearVelocity().x) > 1.8d) {
					this.currentAnimation = animations.get("run");
				} else {
					this.currentAnimation = animations.get("walk");
				}
			}
			idleCounter = 0;
		}
	}
	
	private void accountTmpBoltAmount() {
		int amount = (tmpBoltAmount < 0) ? -tmpBoltAmount : tmpBoltAmount;
		int max = 7;
		// int max = (int) (tmpBoltAmount/20)+1; // "curve value" je kleiner
		// desto weniger wird verrechnet
		
		int incDec = (tmpBoltAmount < 0) ? 1 : -1;
		
		int currAmount = (amount >= max) ? max : amount;
		
		for (int i = 0; i < currAmount; ++i) {
//			increaseBoltCounter();
			boltCounter -= incDec;
			tmpBoltAmount += incDec;
		}
		
	}
	
	public void adjustLaserTime() {
		
		if (laserActive) {
			--laserTime;
		}
		
		if (biteShockLoading) {
			if(laserTime < MAX_LASER_DURATION){
				int step = (boltCounter > 2) ? 2 : boltCounter;
				
				laserTime += step;
				dropElectroBolt();
			} else  {
				biteFinalize();
			}
			
		}
		
	}
	
	public void bite() {
		
		if (!this.biting && !locked && this.isOnGround() && !maxPower()) {

			if ( generator != null) {

				getBody().setLinearVelocity(new Vec2(0,0));
				
				if(!generator.isRepaired()){
					if(boltCounter >= REPAIR_BOLT_PRICE){

						lock();
						game.addSpreadBolts(REPAIR_BOLT_PRICE);
						
						repairGenerator();
					}
					
				} else {

					lock();
					this.biting = true;
					this.biteCounter = 0;
					
					this.body.setLinearVelocity(new Vec2(0f, 0f));
					
					this.currentAnimation = animations.get("bite");
					this.currentAnimation.restart();
					
				}
			}
		}		
	}
	
	private void payForRepair() {

		generator.repair();
//		boltCounter -= REPAIR_BOLT_PRICE;
		tmpBoltAmount -= REPAIR_BOLT_PRICE;
		repairing = false;
		unlock();
	}
	
	private void repairGenerator() {
		repairing = true;
		repairTimeCounter = 0;
	}
	
	private void biteFinalize() {

		this.biting = false;
		biteShockLoading = false;
		this.currentAnimation = animations.get("idle");
		stopBiting = false;
		// this.laserAble = true;
		Sounds.getInstance().stop("bite");
		unlock();
	}
	
	public void boltsCollected(int amount) {
		this.tmpBoltAmount += amount;
		
	}
	
	public void cancelJump() {
		getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, getBody().getLinearVelocity().y * 0.35f));
	}
	
	private void createLaser() {
		
		if (!waitingForLaserToBeKilled) {
			this.laserActive = true;
			
			Sounds.getInstance().loop("laser", Functions.randomRange(0.8f, 1.2f), 1f);
			
			Iterator<GameObject> iterator = laser.getLaserContacts().iterator();
			while (iterator.hasNext()) {
				GameObject gameObject = (GameObject) iterator.next();
				if (gameObject instanceof Enemy) {
					Enemy enemy = (Enemy) gameObject;
					enemy.laserHit();
				}
				
				if (gameObject instanceof Shred) {
					Shred shred = (Shred) gameObject;
					try {
						shred.grilled();
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}
				iterator.remove();
				
			}
			
			godmode = true;
		} else {
			destroyLaser();
		}
		
	}
	
	private void createSensors() {
			
		// wall collision sensors
		float sensorSizeWidth = width * 0.125f;
		float sensorSizeHeight = height * 0.1f;
		float default_xSpace = width * 0.5f;
		float default_ySpace = height * 0.45f;
		float xSpace = default_xSpace;
		float ySpace = default_ySpace;
		
		for (int i = 0; i < 2; ++i) {
			if (i % 2 == 0) {
				xSpace = -default_xSpace;
			} else {
				xSpace = default_xSpace;
			}
			for (int j = 0; j < 2; ++j) {
				if (j % 2 == 0) {
					ySpace = -default_ySpace;
				} else {
					ySpace = default_ySpace;
				}
				
				Vec2[] vertsSensor = new Vec2[] { new Vec2(-sensorSizeWidth + xSpace, sensorSizeHeight + ySpace),
						new Vec2(-sensorSizeWidth + xSpace, -sensorSizeHeight + ySpace),
						new Vec2(sensorSizeWidth + xSpace, -sensorSizeHeight + ySpace),
						new Vec2(sensorSizeWidth + xSpace, sensorSizeHeight + ySpace) };
				
				PolygonShape sensorPolygonShape = new PolygonShape();
				sensorPolygonShape.set(vertsSensor, vertsSensor.length);
				
				FixtureDef fixtureDefSensor = new FixtureDef();
				fixtureDefSensor.shape = sensorPolygonShape;
				fixtureDefSensor.isSensor = true;
				
				sensorList.add(new MySensor(this.body.createFixture(fixtureDefSensor), sensorPolygonShape));
			}
		}
		
		// ground collision
		float groundCollisionSensorHeight = 0.0625f;
		
		Vec2[] vertsGroundSensor = new Vec2[] { new Vec2(-width * 0.45f, height * 0.5f - groundCollisionSensorHeight),
				new Vec2(-width * 0.45f, height * 0.5f + groundCollisionSensorHeight),
				new Vec2(width * 0.45f, height * 0.5f + groundCollisionSensorHeight),
				new Vec2(width * 0.45f, height * 0.5f - groundCollisionSensorHeight) };
		
		PolygonShape sensorPolygonShape = new PolygonShape();
		sensorPolygonShape.set(vertsGroundSensor, vertsGroundSensor.length);
		
		FixtureDef fixtureDefSensor = new FixtureDef();
		fixtureDefSensor.shape = sensorPolygonShape;
		fixtureDefSensor.isSensor = true;
		
		this.sensorGroundCollision = new MySensor(this.body.createFixture(fixtureDefSensor), sensorPolygonShape);
		sensorList.add(sensorGroundCollision);
		
		sensorBottomLeft = sensorList.get(0);
		sensorTopLeft = sensorList.get(1);
		sensorBottomRight = sensorList.get(2);
		sensorTopRight = sensorList.get(3);
	}
	
	public float curveValue(float dest, float current, float smoothness) {
		return current + (dest - current) / smoothness;
	}
	
	public void destroyLaser() {
		
		// if(laserActive){ // wenn laser taste gedrückt wurde, aber bevor
		// geschossen wurde, abgebrochen
		// this.laserAble = false;
		// }
		
		Sounds.getInstance().stop("laser");
		if (laserActive) Sounds.getInstance().play("laserreverb", 0.5f, 1f);
		
		this.laserStarted = false;
		this.laserActive = false;
		wasLasering = true;
		godmode = false;
		
		unlock();
		
	}
	
	public void die(boolean throwback) {
		if (!dead && godmodeCounter > GODMODE_DURATION) {
			if (!godmode) {
				this.currentAnimation = animations.get("deathAir");
				this.dead = true;
				this.deadAndOnGround = false;
			}
			
			if (throwback) {
				float force = 5f;
				this.getBody().setLinearVelocity(new Vec2(body.getLinearVelocity().x, -force));
			}
			
			Sounds.getInstance().play("death", 1f, Functions.randomRange(0.9f, 1.0f));
			
			deathTimeCounter = 0;
			lock();
		}
	}
	
	public void draw(Graphics g, boolean debugView) {
		if (debugView) {
			this.drawOutline(g);
			wheel.drawOutline(g);
			laser.drawOutline(g);
		} else {
			this.drawImage();
		}
		
		// // sprite testing
		// this.drawImage();
		// this.drawOutline(g);
		
	}
	
	public void drawImage() {
		Vec2 position = this.body.getPosition();
		
		// XXX MAGIC NUMBERS
		float drawWidth = currentAnimation.getWidth() / 150f;
		float drawHeight = currentAnimation.getHeight() / 150f;
		drawWidth = (left) ? drawWidth : -drawWidth;
		
		Vec2 offset = animationOffsets.get(currentAnimation.getImage(0).getResourceReference());
		currentAnimation.draw(position.x + ((left) ? -offset.x : offset.x), position.y + offset.y, -drawWidth * 0.5f, drawHeight * 0.5f);
	}
	
	public void drawOutline(Graphics g) {
		
		// body hitbox
		Polygon polygonToDraw = new Polygon();
		Vec2[] verts = this.boxPolygonShape.getVertices();
		
		for (int i = 0; i < this.boxPolygonShape.m_vertexCount; ++i) {
			Vec2 vert = verts[i];
			Vec2 worldPoint = this.body.getWorldPoint(vert);
			polygonToDraw.addPoint(worldPoint.x, worldPoint.y);
		}
		g.draw(polygonToDraw);
		
		// draw sensors
		for (MySensor mySensor : sensorList) {
			mySensor.draw(g, this.body);
		}
		
		// draw tail while attacking
		if (this.doTailwhip) {
			Polygon polygonTailwhipToDraw = new Polygon();
			Vec2[] vertsTailwhip = this.polygonShapeTail.getVertices();
			
			for (int i = 0; i < this.polygonShapeTail.m_vertexCount; ++i) {
				Vec2 vert = vertsTailwhip[i];
				Vec2 worldPoint = this.body.getWorldPoint(vert);
				polygonTailwhipToDraw.addPoint(worldPoint.x, worldPoint.y);
			}
			g.draw(polygonTailwhipToDraw);
		}
		
	}
	
	private void dropElectroBolt() {
		
	}
	
	public Body getBody() {
		return body;
	}
	
	public int getBoltCounter() {
		return boltCounter;
	}
	
	public Checkpoint getCheckpoint() {
		return lastCheckpoint;
	}
	
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}
	
	public Vec2 getCurrentVelocity() {
		return this.body.getLinearVelocity();
	}
	
	public ArrayList<DropItem> getDropItemsToCollect() {
		return dropItemsToCollect;
	}
	
	public Fixture getFixture() {
		return boxFixture;
	}
	
	public Laser getLaser() {
		return this.laser;
	}
	
	public int getLaserTime() {
		return laserTime;
	}
	
	public int getPigCounter() {
		return pigCounter;
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
	
	public ArrayList<MySensor> getSensorList() {
		return this.sensorList;
	}
	
	public MySensor getSensorTopLeft() {
		return sensorTopLeft;
	}
	
	public MySensor getSensorTopRight() {
		return sensorTopRight;
	}
	
	public Fixture getTailFixture() {
		return this.fixtureTail;
	}
	
	public GameObjectCircle getWheel() {
		return wheel;
	}
	
	private void groundpound() {
		if (this.groundPoundCounter > GROUNDPOUND_AIRTIME) {
			this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, groundPoundPower));
			// unlock();
		} else {
			this.getBody().setLinearVelocity(new Vec2(0f, 0f));
		}
	}
	
	public void groundpoundInit() {
		if (locked || wasLasering) {
			return;
		}
		
		if (groundPoundCounter > GROUNDPOUND_DELAY && !groundPounding) {
			
			lock();
			// FIXME wegen movement evtl doch nicht locken? spezial lock für
			// movement?
			
			this.currentAnimation = animations.get("groundpound");
			this.currentAnimation.restart();
			
			this.groundPounding = true;
			this.groundPoundCounter = 0;
			
			godmode = true;
			
			this.groundpound();
			
		}
		
	}
	
	public void increaseBoltCounter() {
		++boltCounter;
	}
	
	public void increaseBoltCounter(int i) {
		boltCounter += i;
	}
	
	private void increaseCounters() {
		++groundPoundCounter;
		++tailwhipCounter;
		++idleCounter;
		++jumpCounter;
		++laserCounter;
		++biteCounter;
		++deathTimeCounter;
		++tailwhipDelayCounter;
		++repairTimeCounter;
		++godmodeCounter;
	}
	
	public void increasePigCounter() {
		++pigCounter;
	}
	
	private void initAnimations() throws SlickException {
		// XXX evtl auch da eine hashmap verwenden?
		SpriteSheet sheetWalk = Images.getInstance().getSpriteSheet("images/walkcycle.png", 300, 290);
		SpriteSheet sheetRun = Images.getInstance().getSpriteSheet("images/runcycle.png", 368, 192);
		SpriteSheet sheetWallJump = Images.getInstance().getSpriteSheet("images/walljump.png", 310, 342);
		SpriteSheet sheetWallIdle = Images.getInstance().getSpriteSheet("images/wallidle.png", 310, 342);
		SpriteSheet sheetTailwhip = Images.getInstance().getSpriteSheet("images/tailwhip.png", 385, 180);
		SpriteSheet sheetIdle = Images.getInstance().getSpriteSheet("images/idle.png", 227, 288);
		SpriteSheet sheetGroundpoundRoll = Images.getInstance().getSpriteSheet("images/groundpoundroll.png", 300, 270);
		SpriteSheet sheetGroundpoundAir = Images.getInstance().getSpriteSheet("images/groundpoundair.png", 300, 270);
		SpriteSheet sheetGroundpoundImpact = Images.getInstance().getSpriteSheet("images/groundpoundimpact.png", 300, 271);
		SpriteSheet sheetDeath = Images.getInstance().getSpriteSheet("images/death.png", 364, 160);
		SpriteSheet sheetDeathAir = Images.getInstance().getSpriteSheet("images/deathair.png", 365, 160);
		SpriteSheet sheetWalkJump = Images.getInstance().getSpriteSheet("images/jump.png", 337, 288);
		SpriteSheet sheetWalkJumpAir = Images.getInstance().getSpriteSheet("images/jumpair.png", 338, 288);
		SpriteSheet sheetRunJump = Images.getInstance().getSpriteSheet("images/flycycle.png", 368, 172);
		SpriteSheet sheetBite = Images.getInstance().getSpriteSheet("images/bite.png", 227, 288);
		SpriteSheet sheetShock = Images.getInstance().getSpriteSheet("images/shock.png", 227, 288);
		SpriteSheet sheetLaser = Images.getInstance().getSpriteSheet("images/lasercycle.png", 300, 270);
		SpriteSheet sheetRepair = Images.getInstance().getSpriteSheet("images/repair.png", 275, 288);
		
		Animation animationWallJump = new Animation(sheetWallJump, 70);
		Animation animationTailwhip = new Animation(sheetTailwhip, TAILWHIP_TIME);
		Animation animationIdle = new Animation(sheetIdle, 200);
		animationIdle.setPingPong(true);
		Animation animationGroundpoundRoll = new Animation(sheetGroundpoundRoll, 100);
		animationGroundpoundRoll.setLooping(false);
		Animation animationGroundpoundAir = new Animation(sheetGroundpoundAir, 50);
		Animation animationGroundpoundImpact = new Animation(sheetGroundpoundImpact, 80);
		Animation animationDeath = new Animation(sheetDeath, 200);
		Animation animationWalkJump = new Animation(sheetWalkJump, 80);		
		Animation animationBite = new Animation(sheetBite, 100);
		animationBite.setLooping(false);
		
		animations.put("run", new Animation(sheetRun, 100));
		animations.put("walk", new Animation(sheetWalk, 100));
		animations.put("wallJump", animationWallJump);
		animations.put("wallIdle", new Animation(sheetWallIdle, 200));
		animations.put("tailwhip", animationTailwhip);
		animations.put("idle", animationIdle);
		animations.put("groundpound", animationGroundpoundRoll);
		animations.put("groundpoundAir", animationGroundpoundAir);
		animations.put("groundpoundImpact", animationGroundpoundImpact);
		animations.put("death", animationDeath);
		animations.put("deathAir", new Animation(sheetDeathAir, 150));
		animations.put("repair", new Animation(sheetRepair, 150));
		
		// animations.put("walkJump", animationWalkJump);
		// animations.put("walkJumpAir", new Animation(sheetWalkJumpAir, 100));
		
		animations.put("runJump", new Animation(sheetRunJump, 100));
		animations.put("bite", animationBite);
		animations.put("shock", new Animation(sheetShock, 100));
		animations.put("laser", new Animation(sheetLaser, 100));
		
		animationOffsets.put("images/runcycle.png", new Vec2(-0.65f, -0.3f));
		animationOffsets.put("images/walkcycle.png", new Vec2(-0.55f, -0.7f));
		animationOffsets.put("images/walljump.png", new Vec2(-0.7f, -0.5f));
		animationOffsets.put("images/wallidle.png", new Vec2(-0.7f, -0.5f));
		animationOffsets.put("images/tailwhip.png", new Vec2(-0.65f, -0.3f));
		animationOffsets.put("images/idle.png", new Vec2(-0.45f, -0.7f));
		animationOffsets.put("images/groundpoundroll.png", new Vec2(-0.5f, -0.6f));
		animationOffsets.put("images/groundpoundair.png", new Vec2(-0.5f, -0.6f));
		animationOffsets.put("images/groundpoundimpact.png", new Vec2(-0.5f, -0.6f));
		animationOffsets.put("images/death.png", new Vec2(-0.6f, -0.3f));
		animationOffsets.put("images/deathair.png", new Vec2(-0.6f, -0.3f));
		animationOffsets.put("images/repair.png", new Vec2(-0.45f, -0.7f));
		
		// animationOffsets.put("images/walkjump.png", new Vec2(0f, 0f));
		// animationOffsets.put("images/walkjumpAir.png", new Vec2(0f, 0f));
		
		animationOffsets.put("images/flycycle.png", new Vec2(-0.65f, -0.3f)); // runjump
		animationOffsets.put("images/bite.png", new Vec2(-0.45f, -0.7f));
		animationOffsets.put("images/shock.png", new Vec2(-0.45f, -0.7f));
		animationOffsets.put("images/lasercycle.png", new Vec2(-0.7f, -0.62f));
		
		currentAnimation = animations.get("idle");
	}
	
	// laser
	public void initializeLaser() {
		
		if (!locked) {
			lock();
			
			this.laserStarted = true;
			
			laserCounter = 0;
			this.currentAnimation = animations.get("groundpound");
			this.currentAnimation.restart();
		}
	}
	
	public boolean isAttacking() {
		return doTailwhip || laserStarted || laserActive || groundPounding;
	}
	
	public boolean isBiting() {
		return biting;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public boolean isGroundPounding() {
		return groundPounding;
	}
	
	public boolean isJumpingFromWall() {
		return this.jumpingFromWall;
	}
	
	public boolean isLaserActive() {
		return laserActive;
	}
	
	public boolean isLaserStarted() {
		return laserStarted;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public boolean isOnGround() {
		return this.sensorGroundCollision.isColliding();
	}
	
	public boolean isOnWall() {
		return this.leftWallColliding() || this.rightWallColliding();
	}
	
	// public void setShootingPower(int shootingPower) {
	// this.shootingPower = shootingPower;
	// }
	
	public boolean isWaitingForLaserToBeKilled() {
		return waitingForLaserToBeKilled;
	}
	
	public void jump() {
		
		if (locked)
			return;
		
		if (!groundPounding && (sensorGroundCollision.isColliding() || leftWallColliding() || rightWallColliding())) {
			
			jumpCounter = 0;
			
			// normal jump / on ground
			float jumpSpeedX = this.body.getLinearVelocity().x;
			float jumpSpeedY = this.jumpPower;
			
			Sounds.getInstance().play("jump", Functions.randomRange(0.9f, 1.1f), 1f);
			
			if (this.isOnGround()) {
				
				this.currentAnimation = animations.get("runJump");
				this.currentAnimation.restart();
				
			} else {
				
				if (leftWallColliding()) {
					
					jumpSpeedX = -this.jumpPower * 0.5f;
					this.jumpingFromWall = true;
					this.left = false;
					
				} else if (rightWallColliding()) {
					
					jumpSpeedX = this.jumpPower * 0.5f;
					this.jumpingFromWall = true;
					this.left = true;
					
				}
				
				this.currentAnimation = animations.get("wallJump");
				this.currentAnimation.restart();
			}
			
			this.body.setLinearVelocity(new Vec2(jumpSpeedX, jumpSpeedY));
			// frontflip();
		}
		
	}
	
	public boolean leftWallColliding() {
		return sensorBottomLeft.isColliding() && sensorTopLeft.isColliding();
	}
	
	
	public void lock() {
		getBody().setLinearVelocity( new Vec2(0f, 0f) );
		locked = true;
	}
	

	public boolean isLookingLeft() {
		return this.left;
	}
	
	private void revive() {
		body.setTransform(lastCheckpoint.getMidPoint(), 0f);
		wheel.getBody().setTransform(lastCheckpoint.getMidPoint(), 0f);
		body.setLinearVelocity(new Vec2(0f, 0f));
		// TODO eine resetVariables() wäre praktisch...
		dead = false;
		deadAndOnGround = false;
		biting = false;
		repairing = false;
		unlock();
		godmodeCounter = 0;
	}
	
	public boolean rightWallColliding() {
		return sensorBottomRight.isColliding() && sensorTopRight.isColliding();
	}
	
	public void setAbleToGetLaser(boolean ableToGetLaser) {
		this.ableToGetLaser = ableToGetLaser;
	}
	
	
	public void setCheckpoint(Checkpoint cp) {
		lastCheckpoint = cp;
	}
	
	public void setConveyorSpeed(float conveyorSpeed) {
		this.conveyorSpeed += conveyorSpeed;
	}
	
	public void setLaser(Laser laser) {
		this.laser = laser;
	}
	
	public void setLeft(boolean left) {
		if ( (!locked || laserActive) || (locked && groundPounding) ) {
			this.left = left;
		}
	}
	
	public void setMovementButtonIsDown(boolean movementButtonIsDown) {
	}
	
	public void setWaitingForLaserToBeKilled(boolean waitingForLaserToBeKilled) {
		this.waitingForLaserToBeKilled = waitingForLaserToBeKilled;
	}
	
	public void stopBiting() {
		stopBiting = true;
	}
	
	public void stopGroundpounding() {
		if (groundPounding) {
			// currentAnimation.stop();
			this.groundPounding = false;
			unlock();
			godmode = false;
			
		}
		
		currentAnimation = animations.get("groundpoundImpact");
	}
	
	private void tailwhip() {
		isGoingToCreateTailwhip = false;
		
		this.doTailwhip = true;
		this.fixtureTail = this.body.createFixture(this.fixtureDefTail);
	}
	
	private void tailwhipFinalize() {
		
		float distance = TAILWHIP_DISTANCE;
		
		if (this.isLookingLeft()) {
			distance = -distance;
		}
		
		this.body.destroyFixture(this.fixtureTail);
		this.doTailwhip = false;
		
	}
	
	public void tailwhipInit() {
		
		if (locked) {
			return;
		}
		
		if (!doTailwhip && !this.isGoingToCreateTailwhip && !this.groundPounding) {
			
			this.tailwhipCounter = 0;
			
			Sounds.getInstance().play("tailwhip", 0.6f, Functions.randomRange(0.8f, 1.2f));
			
			float tailWidth = 0.5f;
			float tailHeight = 0.2f;
			float direction = this.width - tailWidth * 0.35f;
			float heightSpace = tailHeight * 0.5f;
			float distance = TAILWHIP_DISTANCE;
			
			if (this.isLookingLeft()) {
				direction = -direction;
				distance = -distance;
			}
			
			this.getBody().setLinearVelocity(new Vec2(distance, body.getLinearVelocity().y));
			
			Vec2[] vertsTail = new Vec2[] { new Vec2(-tailWidth * 0.5f + direction, tailHeight * 0.5f + heightSpace),
					new Vec2(-tailWidth * 0.5f + direction, -tailHeight * 0.5f + heightSpace),
					new Vec2(tailWidth * 0.5f + direction, -tailHeight * 0.5f + heightSpace),
					new Vec2(tailWidth * 0.5f + direction, tailHeight * 0.5f + heightSpace) };
			
			this.polygonShapeTail = new PolygonShape();
			this.polygonShapeTail.set(vertsTail, vertsTail.length);
			
			this.fixtureDefTail = new FixtureDef();
			this.fixtureDefTail.shape = this.polygonShapeTail;
			this.fixtureDefTail.isSensor = true;
			
			this.currentAnimation = animations.get("tailwhip");
			this.currentAnimation.restart();
			
			isGoingToCreateTailwhip = true;
			tailwhipDelayCounter = 0;
			
		}
	}
	
	public void unlock() {
		locked = false;
	}
	
	public void update() {
		if (!dead) {
			
			adjustLaserTime();
			
			// // slow down player if no directionmovment button is pressed
			// if( this.conveyorSpeed == 0 && !this.movementButtonIsDown){
			// float slowDownForce = 0.5f;
			// float slowDownThreshold = 0.15f;
			// // left
			// if(this.getBody().getLinearVelocity().x < -slowDownThreshold &&
			// this.isOnGround() ) {
			// this.getBody().applyLinearImpulse(new Vec2(slowDownForce,0),
			// this.getBody().getPosition());
			// } else
			//
			// // right
			// if(this.getBody().getLinearVelocity().x > slowDownThreshold &&
			// this.isOnGround() ) {
			// this.getBody().applyLinearImpulse(new Vec2(-slowDownForce,0),
			// this.getBody().getPosition());
			// }
			// }
			
			// float in air while shooting laser
			if (this.laserStarted) {
				// this.body.setTransform(this.laserStartingPosition,
				// this.body.getAngle());
				// FIXME magic number
				this.body.setLinearVelocity(new Vec2(0f, -0.3f));
			}
			
			// XXX evtl da checken, welche hitbox ausrichtung angebracht is
			
			// // abwaerts bewegung an wand
			// if( this.isOnWall() ){
			// if(this.body.getLinearVelocity().y < 0){
			// if( (this.leftWallColliding() && this.body.getLinearVelocity().x
			// < 0f ) || (this.rightWallColliding() &&
			// this.body.getLinearVelocity().x > 0f )){
			// this.body.setLinearVelocity(new
			// Vec2(this.body.getLinearVelocity().x, 1f));
			// } else {
			// this.body.setLinearVelocity(new
			// Vec2(this.body.getLinearVelocity().x, -2f));
			// }
			// }
			// // if( !this.isOnGround()){
			// // this.setRunning(false);
			// // }
			// }
			
			if (this.groundPounding) {
				this.groundpound();
			}
			
			// if(getSensorGroundCollision().isColliding() &&
			// this.body.getLinearVelocity().y < 0f){
			if (isOnGround() || isOnWall()) {
				
				if (wasLasering) {
					unlock();
				}
				this.jumpingFromWall = false;
				
				wasLasering = false;
			}
			
			if (isOnGround() /* && !this.isRunning() */&& !isGoingToCreateTailwhip && !doTailwhip && idleCounter > 2 && !dead && !biting
					&& !locked) {
				this.currentAnimation = animations.get("idle");
			}
			
			if (!this.running && !this.isOnGround() && this.currentAnimation.isStopped()) {
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
			
			if (!isOnGround() && !isJumpingFromWall() && !groundPounding && !dead && !laserStarted && !isGoingToCreateTailwhip
					&& !doTailwhip && !frontflipping) {
				this.currentAnimation = animations.get("runJump");
			}
			
			if (frontflipping && currentAnimation.isStopped()) {
				frontflipping = false;
			}
			
			if (isGroundPounding() && !isOnGround() && (currentAnimation == null || currentAnimation.isStopped())) {
				currentAnimation = animations.get("groundpoundAir");
			}
			
			if (this.isOnGround() && this.dizzy) {
				// gugu
				this.currentAnimation = animations.get("groundpoundImpact");
			}
			
			if (this.isOnWall() && !this.isJumpingFromWall() && !this.isOnGround() && !laserStarted) {
				this.currentAnimation = animations.get("wallIdle");
			}
			
			// if( laserCounter == LASER_DURATION ){
			if (laserTime <= 0) {
				laserTime = 0;
				if (laserStarted) {
					destroyLaser();
				}
			}
			
			if (this.biting && (currentAnimation == null || this.currentAnimation.isStopped())) {
				this.currentAnimation = animations.get("shock");
				biteShockLoading = true;
				Sounds.getInstance().loop("bite", 1f, 1f);
			}
			
			if (this.biting && stopBiting ){
					biteFinalize();
			
			}
			
			if (this.laserStarted && (currentAnimation == null || currentAnimation.isStopped())) {
				this.currentAnimation = animations.get("laser");
				createLaser();
			}
			
			// TODO überprüfen ob das jetzt mit lauf band funkt
			if (this.conveyorSpeed != 0) {
				this.getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x + conveyorSpeed, getBody().getLinearVelocity().y));
			}
			
			if (!doTailwhip && isGoingToCreateTailwhip && tailwhipDelayCounter > TAILWHIP_DELAY) {
				tailwhip();
			}
			
			if (this.tailwhipCounter > TAILWHIP_DURATION && this.doTailwhip) {
				this.tailwhipFinalize();
			}
			
			if (repairing){
				if( repairTimeCounter < REPAIR_TIME){
					game.addSpreadBolts(REPAIR_BOLT_PRICE);
					this.currentAnimation = animations.get("repair");
				} else {
					payForRepair();
				}
			}
			
		} else { // dead
			if (deathTimeCounter > DEATH_WAIT_TIME) {
				revive();
			}
			if (this.isOnGround() && !this.deadAndOnGround) {
				this.deadAndOnGround = true;
				this.currentAnimation = animations.get("death");
				this.currentAnimation.restart();
			}
		}
		
		accountTmpBoltAmount();
		
		increaseCounters();
	}
	
	public Generator getGenerator() {
		return generator;
	}
	public void setGenerator(Generator generator) {
		this.generator = generator;
	}

	public int getMaxLaserTime() {
		return MAX_LASER_DURATION;
	}
	
	public boolean maxPower(){
		System.out.println("lasertime: " + laserTime);
		System.out.println("max laser dur: " + MAX_LASER_DURATION);
		return laserTime >= MAX_LASER_DURATION;
	}
	
	public boolean isRepairing() {
		return repairing;
	}
}
