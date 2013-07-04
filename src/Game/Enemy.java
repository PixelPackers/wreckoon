package Game;

import java.util.ArrayList;
import java.util.HashMap;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public abstract class Enemy extends GameObjectBox {
	
	protected Game game;
//	protected static final float width = 0.8f;
//	protected static final float height = width;
	
	protected float PIG_CLASS_SIZE_FACTOR;

	protected static final int DIE_TIME 			= 100;
//	TODO kA warum die zahl...
	protected static final int DIE_TIME_IN_FRAMES 	=  30;
	
	protected int dieCounter = 0;
	protected int dizzyCounter = 0;

	protected static final int DIZZY_TIME 			= 100;
	
	private static final float	MIN_SPEED		= 1f;
	private static final float	MAX_SPEED		= 1.5f;
	protected float	speed;

	private static final float	MIN_SIZE		= 0.75f;
	private static final float	MAX_SIZE		= 1.25f;
	private static float staticPigSize = (float)(Math.random() *  (MAX_SIZE-MIN_SIZE) + MIN_SIZE);
	private float pigSize;
	
	private boolean dead = false;
	protected boolean left = false;
	protected boolean dizzy = false;
	
	private float conveyorSpeed = 0f;
	
	protected ArrayList<MySensor> sensorList = new ArrayList<MySensor>();
	protected MySensor 	sensorLeft;
	protected MySensor 	sensorRight;
	protected MySensor	sensorGroundCollision;

	protected HashMap<String, Animation> animations = new HashMap<String, Animation>();
	protected Animation currentAnimation;

	public Enemy(Game game, float posX, float posY, String imgPath) throws SlickException {
		super(game.getWorld(), posX, posY, staticPigSize, staticPigSize, 3.3f, 0.3f, 0.3f, imgPath, BodyType.DYNAMIC);

		this.game = game;

		this.speed =  (float)(Math.random() *  (MAX_SPEED-MIN_SPEED) + MIN_SPEED);
		
		pigSize = staticPigSize; 
		staticPigSize = (float)(Math.random() *  (MAX_SIZE-MIN_SIZE) + MIN_SIZE);
		
		createSensors();
	}
	
	protected abstract void initAnimations() throws SlickException;
	
	private void createSensors() {

		float width = pigSize;
		float height = pigSize;
	
		// wall collision sensors
		float sensorSizeWidth	= width  * 0.1f;
		float sensorSizeHeight	= height * 0.3f;
		
		float xSpace = width*0.5f;
		float ySpace = height*0.25f;
		float putDown = 0.3f;
		{
		// left sensor
		Vec2[] vertsSensor = new Vec2[]{
				new Vec2(-sensorSizeWidth + xSpace,  sensorSizeHeight + ySpace - putDown),
				new Vec2(-sensorSizeWidth + xSpace, -sensorSizeHeight + ySpace - putDown),
				new Vec2( sensorSizeWidth + xSpace, -sensorSizeHeight + ySpace - putDown),
				new Vec2( sensorSizeWidth + xSpace,  sensorSizeHeight + ySpace - putDown)
			};

		PolygonShape sensorPolygonShape = new PolygonShape();
		sensorPolygonShape.set(vertsSensor, vertsSensor.length);
		
		FixtureDef 	fixtureDefSensor = new FixtureDef();
		fixtureDefSensor.shape = sensorPolygonShape;
		fixtureDefSensor.isSensor=true;

		sensorList.add(new MySensor(this.getBody().createFixture(fixtureDefSensor), sensorPolygonShape ) );
		}
		{
		// right sensor
		Vec2[] vertsSensor = new Vec2[]{
				new Vec2(-sensorSizeWidth - xSpace,  sensorSizeHeight + ySpace - putDown),
				new Vec2(-sensorSizeWidth - xSpace, -sensorSizeHeight + ySpace - putDown),
				new Vec2( sensorSizeWidth - xSpace, -sensorSizeHeight + ySpace - putDown),
				new Vec2( sensorSizeWidth - xSpace,  sensorSizeHeight + ySpace - putDown)
			};

		PolygonShape sensorPolygonShape = new PolygonShape();
		sensorPolygonShape.set(vertsSensor, vertsSensor.length);
		
		FixtureDef 	fixtureDefSensor = new FixtureDef();
		fixtureDefSensor.shape = sensorPolygonShape;
		fixtureDefSensor.isSensor=true;

		sensorList.add(new MySensor(this.getBody().createFixture(fixtureDefSensor), sensorPolygonShape ) );
		}
		// ground collision
		float groundCollisionSensorHeight=0.1f;
		
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
		fixtureDefSensor.isSensor=true;
		
		this.sensorGroundCollision = new MySensor( this.getBody().createFixture(fixtureDefSensor), sensorPolygonShape );
		sensorList.add(sensorGroundCollision);
		

		sensorLeft 	= sensorList.get(0);
		sensorRight = sensorList.get(1);
	}
	
	@Override
	public void drawOutline(Graphics g){
				
		Polygon polygonToDraw = new Polygon();
		Vec2[] verts = this.polygonShape.getVertices();
		for (int i=0; i< this.polygonShape.m_vertexCount; ++i) {
			Vec2 vert = verts[i];
			Vec2 worldPoint = this.getBody().getWorldPoint(vert);
			polygonToDraw.addPoint(worldPoint.x, worldPoint.y);
		}
		
		if(this.dead)
			g.setColor(Color.red);
		g.draw(polygonToDraw);
		g.setColor(Color.white);
		
		
		// draw sensors
		for (MySensor mySensor : sensorList){
			mySensor.draw(g, this.getBody());
		}
	}

//	public void drawImage(){
//		Vec2 position	= this.body.getPosition();
//		
//		// XXX MAGIC NUMBERS
//		float drawWidth =  currentAnimation.getWidth() / 150; 
//		float drawHeight = currentAnimation.getHeight() / 150;
//		drawWidth= (left) ? drawWidth: -drawWidth;
//		
//		currentAnimation.draw( position.x + drawWidth*0.5f,
//				-position.y - drawHeight*0.5f - 0.5f, // -0.5f --> sonst wuerde sprite in den boden hinein stehen 
//				-drawWidth, 
//				drawHeight);
//	}
	
	public boolean isOnGround(){
		return this.sensorGroundCollision.isColliding();
	}
	
	public boolean isOnWall(){
		return this.leftWallColliding() || this.rightWallColliding();
	}
	
	public boolean leftWallColliding(){
		return sensorLeft.isColliding();
	}

	public boolean rightWallColliding(){
		return sensorRight.isColliding();
	}
	
	public ArrayList<MySensor> getSensorList() {
		return sensorList;
	}
		
	public void jump(){

	}
	
	public void throwBack(){

		dizzy = true;
		dizzyCounter = 0;
		
		float force = 7.5f;
		float x = (game.getPlayer().movesLeft()) ? -force : force;
		
		this.getBody().setLinearVelocity(new Vec2 (x, -force) );
		
	}
	
	public void die() {

		// sprite
		game.getObjectsToRemove().add(this);
		
		this.dead = true;
		this.dieCounter = 0;
	}

	public boolean isDead() {
		return dead;
	}
	
	public void setConveyorSpeed(float conveyorSpeed) {
		this.conveyorSpeed = conveyorSpeed;
	}
	
	public void update() {
		
		if(this.conveyorSpeed != 0){
			this.getBody().setLinearVelocity( new Vec2(getBody().getLinearVelocity().x + conveyorSpeed, getBody().getLinearVelocity().y) );
		}
		
		if( dead && dieCounter == DIE_TIME_IN_FRAMES) {
			game.getEnemiesToRemove().add(this);
		}
		
		if(dizzy && dizzyCounter > DIZZY_TIME){
			dizzy = false;
		}
		
		++dieCounter;
		++dizzyCounter;
	}

	public void drawImage() {
		float drawWidth = (left) ? -pigSize : pigSize;
		float drawHeight= (dizzy) ? -pigSize : pigSize;
		currentAnimation.draw( 
				getBody().getPosition().x-drawWidth*0.5f,
				getBody().getPosition().y-drawHeight*0.5f, 
				drawWidth * PIG_CLASS_SIZE_FACTOR, 
				drawHeight * PIG_CLASS_SIZE_FACTOR);
	}
	
	public float getPigSize() {
		return pigSize;
	}
}
