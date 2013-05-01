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
import org.newdawn.slick.Color;
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
	
	private static int groundPoundCounter = 0;
	
	private float jumpPower = 20f;
	private float groundPoundPower = -50f;

	private boolean isRunning = false;
	private boolean isGroundPounding = false;
	private boolean jumping = false;
	
	private float	maxPlayerRotation = 10f;
	private World world;
	
	private float width = 1f;
	private float height = 2f;
	private PolygonShape polygonShape = new PolygonShape();
	
	private Body 		body;
	private BodyDef 	bodyDef = new BodyDef();
	private FixtureDef 	fixtureDef = new FixtureDef();
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
		
		this.polygonShape.set(polygonShapeVerts, polygonShapeVerts.length);
		
		this.fixtureDef.density 	= 11f;
		this.fixtureDef.friction 	= 0.3f;
		this.fixtureDef.restitution = 0f;
		this.fixtureDef.shape = polygonShape;
		
		
		this.img = new Image("images/player.png");

		this.body = world.createBody(bodyDef);
		this.body.createFixture(fixtureDef);
		this.body.setFixedRotation(true);
		

		this.createSensors();
		
	}
	
	private void createSensors() {

		// wall collision sensors
		float sensorSizeWidth	= width  * 0.25f;
		float sensorSizeHeight	= height * 0.1f;
		float dxSpace = 0.5f;
		float dySpace = 0.75f;
		float xSpace = dxSpace;
		float ySpace = dySpace;
		
		for (int i=0;i<2; ++i){

			if(i % 2 == 0){
				xSpace = -dxSpace;
			}
			for (int j=0;j<2; ++j){
				
				if(i % 2 == 0){
					xSpace = -dxSpace;
				} else {
					xSpace = dxSpace;
				}
				 
				if(j % 2 == 0){
					ySpace = -dySpace;
				} else {
					ySpace = dySpace;
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

		Polygon polygonToDraw = new Polygon();
		Vec2[] verts = this.polygonShape.getVertices();
		
		for (int i=0; i< this.polygonShape.m_vertexCount; ++i) {
			Vec2 vert = verts[i];
			Vec2 worldPoint = this.body.getWorldPoint(vert);
			polygonToDraw.addPoint(worldPoint.x, -worldPoint.y);
		}
		
		g.draw(polygonToDraw);
		
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
		
//		if(this.leftWallColliding() && !this.isJumping() ){
		if(this.leftWallColliding() ){
			if(this.body.getLinearVelocity().y < 0){
				this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, 1f));	
			}

		}
		
//		if(this.rightWallColliding() && !this.isJumping() ){
		if(this.rightWallColliding() ){
			if(this.body.getLinearVelocity().y < 0){
				this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, 1f));
			}

		}
		
		++groundPoundCounter;
		if(this.groundPoundCounter > 10 && isGroundPounding){
			this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, groundPoundPower));

		}
//		if(getSensorGroundCollision().isColliding() && this.body.getLinearVelocity().y < 0f){
		if(getSensorGroundCollision().isColliding()){
			isGroundPounding = false;
			jumping = false;
			System.out.println("250: " + getSensorGroundCollision().isColliding());
		}

	}
	

	private boolean isJumping() {
		
		return this.jumping;
	}

	public void accelerate(boolean left) {
		
		// FIXME wenn man auf max sprint beschleunigt und springt, kann man ohne sprint taste nicht gegenlenken, weil max speed f�r 'gehen' zu hoch w�re und somit eingabe ignoriert wird
		// 		man kann mit 'gehen' nie gegenlenken, wenn speed h�her als maxGehen ist...
		
		
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
		this.jumping = true;
		
		if(!isGroundPounding && (sensorGroundCollision.isColliding() || leftWallColliding() || rightWallColliding() ) ){
			
			float jumpSpeedX = this.body.getLinearVelocity().x;
			
			if(leftWallColliding()){
				jumpSpeedX = this.jumpPower;
			} else if(rightWallColliding()){
				jumpSpeedX = -this.jumpPower;
			}
			
			this.body.setLinearVelocity(new Vec2(jumpSpeedX, this.jumpPower));
			System.out.println("jump");
		}
	}
	public void groundPound(){
		if(!isGroundPounding){
			isGroundPounding = true;
			
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
