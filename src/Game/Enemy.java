package Game;

import java.util.ArrayList;
import java.util.HashMap;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;

public abstract class Enemy extends GameObjectBox {
	
	protected Game							game;
	// protected static final float width = 0.8f;
	// protected static final float height = width;
	
	protected float							PIG_CLASS_SIZE_FACTOR;
	
	protected static final int				DIE_TIME				= 100;
	// TODO kA warum die zahl...
	protected static final int				DIE_TIME_IN_FRAMES		= 30;
	
	protected int							dieCounter				= 0;
	protected int							dizzyCounter			= 0;
	private int								dizzyRotationCounter	= 0;
	
	private int								health					= (int) (35 * Math.random() + 20);
	protected boolean						getsGrilled				= false;
	
	protected static final int				DIZZY_TIME				= 300;
	
	private static final float				MIN_SPEED				= 1f;
	private static final float				MAX_SPEED				= 1.5f;
	protected float							speed;
	
	private static final float				MIN_SIZE				= 0.75f;
	private static final float				MAX_SIZE				= 1.25f;
	private static float					staticPigSize			= (float) (Math.random() * (MAX_SIZE - MIN_SIZE) + MIN_SIZE);
	private float							pigSize;
	private int								rotDir					= 0;
	
	private boolean							dead					= false;
	protected boolean						left					= false;
	protected boolean						dizzy					= false;
	private boolean							firstTimeRotation		= true;
	private boolean							originalHit				= false;
	
	private float							conveyorSpeed			= 0f;
	
	protected ArrayList<MySensor>			sensorList				= new ArrayList<MySensor>();
	protected MySensor						sensorLeft;
	protected MySensor						sensorRight;
	protected MySensor						sensorGroundCollision;
	
	protected HashMap<String, Animation>	animations				= new HashMap<String, Animation>();
	protected Animation						currentAnimation;
	protected HashMap<String, Vec2>			animationOffsets		= new HashMap<String, Vec2>();
	
	public Enemy(Game game, float posX, float posY, String imgPath) throws SlickException {
		super(game.getWorld(), posX, posY, staticPigSize, staticPigSize, 3.3f, 0.3f, 0.3f, imgPath, BodyType.DYNAMIC);
		
		this.game = game;
		
		this.speed = (float) (Math.random() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED);
		
		pigSize = staticPigSize;
		staticPigSize = (float) (Math.random() * (MAX_SIZE - MIN_SIZE) + MIN_SIZE);
		
		Filter filter = new Filter();
		filter.categoryBits = 4;
		filter.maskBits = 1 + 2 + 4;
		this.getFixture().setFilterData(filter);
		
		createSensors();
	}
	
	private void createSensors() {
		
		float width = pigSize;
		float height = pigSize;
		
		// wall collision sensors
		float sensorSizeWidth = width * 0.1f;
		float sensorSizeHeight = height * 0.45f;
		
		float xSpace = width * 0.5f;
		float ySpace = height * 0.0f;
		float putDown = 0.0f;
		{
			// left sensor
			Vec2[] vertsSensor = new Vec2[] { new Vec2(-sensorSizeWidth + xSpace, sensorSizeHeight + ySpace - putDown),
					new Vec2(-sensorSizeWidth + xSpace, -sensorSizeHeight + ySpace - putDown),
					new Vec2(sensorSizeWidth + xSpace, -sensorSizeHeight + ySpace - putDown),
					new Vec2(sensorSizeWidth + xSpace, sensorSizeHeight + ySpace - putDown) };
			
			PolygonShape sensorPolygonShape = new PolygonShape();
			sensorPolygonShape.set(vertsSensor, vertsSensor.length);
			
			FixtureDef fixtureDefSensor = new FixtureDef();
			fixtureDefSensor.shape = sensorPolygonShape;
			fixtureDefSensor.isSensor = true;
			
			sensorList.add(new MySensor(this.getBody().createFixture(fixtureDefSensor), sensorPolygonShape));
		}
		{
			// right sensor
			Vec2[] vertsSensor = new Vec2[] { new Vec2(-sensorSizeWidth - xSpace, sensorSizeHeight + ySpace - putDown),
					new Vec2(-sensorSizeWidth - xSpace, -sensorSizeHeight + ySpace - putDown),
					new Vec2(sensorSizeWidth - xSpace, -sensorSizeHeight + ySpace - putDown),
					new Vec2(sensorSizeWidth - xSpace, sensorSizeHeight + ySpace - putDown) };
			
			PolygonShape sensorPolygonShape = new PolygonShape();
			sensorPolygonShape.set(vertsSensor, vertsSensor.length);
			
			FixtureDef fixtureDefSensor = new FixtureDef();
			fixtureDefSensor.shape = sensorPolygonShape;
			fixtureDefSensor.isSensor = true;
			
			sensorList.add(new MySensor(this.getBody().createFixture(fixtureDefSensor), sensorPolygonShape));
		}
		// ground collision
		float groundCollisionSensorHeight = 0.1f;
		
		Vec2[] vertsGroundSensor = new Vec2[] { new Vec2(-width * 0.45f, height * 0.5f - groundCollisionSensorHeight),
				new Vec2(-width * 0.45f, height * 0.5f + groundCollisionSensorHeight),
				new Vec2(width * 0.45f, height * 0.5f + groundCollisionSensorHeight),
				new Vec2(width * 0.45f, height * 0.5f - groundCollisionSensorHeight) };
		
		PolygonShape sensorPolygonShape = new PolygonShape();
		sensorPolygonShape.set(vertsGroundSensor, vertsGroundSensor.length);
		
		FixtureDef fixtureDefSensor = new FixtureDef();
		fixtureDefSensor.shape = sensorPolygonShape;
		fixtureDefSensor.isSensor = true;
		
		this.sensorGroundCollision = new MySensor(this.getBody().createFixture(fixtureDefSensor), sensorPolygonShape);
		sensorList.add(sensorGroundCollision);
		
		sensorLeft = sensorList.get(0);
		sensorRight = sensorList.get(1);
	}
	
	public void die() {
		float distance = getBody().getPosition().sub(game.getPlayer().getBody().getPosition()).length();
		if (distance < 15f)
			Sounds.getInstance().play("pigdeath", Functions.randomRange(0.8f, 1.2f), 1f - distance / 15f);
		
		// sprite
		Game.getObjectsToRemove().add(this);
		
		this.dead = true;
		this.dieCounter = 0;
		
		killStatistic();
		
		getBody().getFixtureList().setSensor(true);
	}
	
	private void killStatistic() {

		Statistics.getInstance().incKilledPigsCounter();
		
		if (getsGrilled){
			Statistics.getInstance().incLaserKills();
		} else if(game.getPlayer().isGroundPounding()){
			Statistics.getInstance().incGroundPoundKills();
		} else if(!dizzy){
			Statistics.getInstance().incSuicides();
		}
	}

	public void drawImage() {
		// float drawWidth = (left) ? -pigSize : pigSize;
		// // float drawHeight= (dizzy) ? -pigSize : pigSize;
		// float drawHeight= pigSize;
		
		float drawWidth = currentAnimation.getWidth() / 150f * pigSize;
		float drawHeight = currentAnimation.getHeight() / 150f * pigSize;
		drawWidth = (left) ? drawWidth : -drawWidth;
		
		Vec2 offset = animationOffsets.get(currentAnimation.getImage(0).getResourceReference());
		float scale = 0.7f;
		
		if (!this.dead && !this.isOnGround() && dizzy && (dizzyRotationCounter * 12 % 540 != 0) && firstTimeRotation) {
			Image tmpImg = currentAnimation.getCurrentFrame();
			tmpImg.setRotation(rotDir * dizzyRotationCounter * 12);
			tmpImg.draw(getBody().getPosition().x + ((left) ? -offset.x : offset.x) * pigSize, getBody().getPosition().y + offset.y
					* pigSize, -drawWidth * scale, drawHeight * scale);
			tmpImg.setRotation(0);
			
		} else {
			firstTimeRotation = true;
			currentAnimation.draw(getBody().getPosition().x + ((left) ? -offset.x : offset.x) * pigSize, getBody().getPosition().y
					+ offset.y * pigSize, -drawWidth * scale, drawHeight * scale);
		}
		
	}
	
	@Override
	public void drawOutline(Graphics g) {
		
		Polygon polygonToDraw = new Polygon();
		Vec2[] verts = this.polygonShape.getVertices();
		for (int i = 0; i < this.polygonShape.m_vertexCount; ++i) {
			Vec2 vert = verts[i];
			Vec2 worldPoint = this.getBody().getWorldPoint(vert);
			polygonToDraw.addPoint(worldPoint.x, worldPoint.y);
		}
		
		if (this.dead)
			g.setColor(Color.red);
		g.draw(polygonToDraw);
		g.setColor(Color.white);
		
		// draw sensors
		for (MySensor mySensor : sensorList) {
			mySensor.draw(g, this.getBody());
		}
		
		Rectangle r = new Rectangle(this.getBody().getPosition().x - this.getWidth() / 2,
				this.getBody().getPosition().y - this.getHeight(), health / 50f, 0.05f);
		g.setColor(Color.red);
		g.fill(r);
		g.draw(r);
		g.setColor(Color.white);
	}
	
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}
	
	public float getPigSize() {
		return pigSize;
	}
	
	public ArrayList<MySensor> getSensorList() {
		return sensorList;
	}
	
	protected abstract void initAnimations() throws SlickException;
	
	public void initRotation() {
		this.rotDir = (game.getPlayer().isLookingLeft()) ? -1 : 1;;
		firstTimeRotation = true;
		rotate();
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public boolean isDizzy() {
		return dizzy;
	}
	
	public boolean isOnGround() {
		return this.sensorGroundCollision.isColliding();
	}
	
	public boolean isOnWall() {
		return this.leftWallColliding() || this.rightWallColliding();
	}
	
	public boolean isOriginalHit() {
		return originalHit;
	}
	
	public void jump() {
		
	}
	
	public void laserHit() {
		// this.throwBack();
		// this.die();
		getsGrilled = true;
	}
	
	public void laserHitEnd() {
		getsGrilled = false;
	}
	
	public boolean leftWallColliding() {
		return sensorLeft.isColliding();
	}
	
	public boolean rightWallColliding() {
		return sensorRight.isColliding();
	}
	
	public void rotate() {
		
		float force = 7.5f;
		float x = (game.getPlayer().isLookingLeft()) ? -force : force;
		
		this.getBody().setLinearVelocity(new Vec2(x, -force * 0.75f));
		
		if (Math.random() < 0.5) {
			left = !left;
		}
		
	}
	
	public void setConveyorSpeed(float conveyorSpeed) {
		this.conveyorSpeed = conveyorSpeed;
	}
	
	public void setOriginalHit(boolean originalHit) {
		this.originalHit = originalHit;
	}
	
	public void throwBack(boolean originalHit) {
		
		this.originalHit = originalHit;
		dizzy = true;
		dizzyCounter = 0;
		dizzyRotationCounter = 0;
		
		// FIXME magic numbers
		health -= 4;
		
		if(health < 0){
			if(originalHit){
				Statistics.getInstance().incOriginalTailwhipKill();
			} else {
				Statistics.getInstance().incBounceofTailwhipKill();
			}
			Statistics.getInstance().incTailwhipKills();
		}
		
		initRotation();
	}
	
	public void update() {
		
		if (this.conveyorSpeed != 0) {
			this.getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x + conveyorSpeed, getBody().getLinearVelocity().y));
		}
		
		if (dead && dieCounter == DIE_TIME_IN_FRAMES) {
			Game.getEnemiesToRemove().add(this);
		}
		
		if (dizzy && dizzyCounter > DIZZY_TIME) {
			dizzy = false;
		}
		
		if (health < 0 && !dead) {
			die();
		}
		
		if (getsGrilled && !game.getPlayer().isLaserActive()) {
			getsGrilled = false;
		}
		
		if (getsGrilled) {
			--health;
		}
		
		if (getBody().getLinearVelocity().x < 1f) {
			originalHit = false;
		}
		
		++dieCounter;
		++dizzyCounter;
		++dizzyRotationCounter;
	}
}
