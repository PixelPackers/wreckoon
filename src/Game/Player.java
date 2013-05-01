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
	
	private float jumpPower = 20f;
	private float groundPoundPower = -50f;

	private boolean isRunning = false;
	private boolean isGroundPounding = false;
	
	private float	maxPlayerRotation = 10f;
	private World world;
	
	private float width = 1f;
	private float height = 2f;
	private PolygonShape polygonShape = new PolygonShape();
	
	private Body 		body;
	private BodyDef 	bodyDef = new BodyDef();
	private FixtureDef 	fixtureDef = new FixtureDef();
	private Fixture 	sensorTopLeft;
	private Fixture 	sensorTopRight;
	private Fixture 	sensorBottomLeft;
	private Fixture 	sensorBottomRight;

	private ArrayList<PolygonShape> sensorPolygonShapeList 	= new ArrayList<PolygonShape>();
	private ArrayList<Fixture> sensorFixtureList			= new ArrayList<Fixture>();
	
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
				sensorPolygonShapeList.add(sensorPolygonShape);
				
				FixtureDef 	fixtureDefSensor = new FixtureDef();
				fixtureDefSensor.shape = sensorPolygonShape;
				fixtureDefSensor.isSensor=true;
		
				sensorFixtureList.add(this.body.createFixture(fixtureDefSensor));
				
			}
		}
		
		// ground collision
		float groundCollisionSensorHeight=0.2f;
		
		Vec2[] vertsGroundSensor = new Vec2[]{
			new Vec2(-this.width * 0.5f, -this.height * 0.5f - groundCollisionSensorHeight),
			new Vec2(-this.width * 0.5f, -this.height * 0.5f + groundCollisionSensorHeight),
			new Vec2( this.width * 0.5f, -this.height * 0.5f + groundCollisionSensorHeight),
			new Vec2( this.width * 0.5f, -this.height * 0.5f - groundCollisionSensorHeight)
		};

		PolygonShape sensorPolygonShape = new PolygonShape();
		sensorPolygonShape.set(vertsGroundSensor, vertsGroundSensor.length);
		sensorPolygonShapeList.add(sensorPolygonShape);
		
		FixtureDef 	fixtureDefSensor = new FixtureDef();
		fixtureDefSensor.shape = sensorPolygonShape;
		fixtureDefSensor.isSensor=true;

		sensorFixtureList.add(this.body.createFixture(fixtureDefSensor));
	
		
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
		for(PolygonShape polygonShape : sensorPolygonShapeList){
			polygonToDraw 		= new Polygon();
			Vec2[] sensorVerts 	= polygonShape.getVertices();
			
			for (int i=0; i< polygonShape.m_vertexCount; ++i) {
				
				Vec2 sensorVert 		= sensorVerts[i];
				Vec2 sensorWorldPoint	= this.body.getWorldPoint(sensorVert);
				polygonToDraw.addPoint(sensorWorldPoint.x, -sensorWorldPoint.y);
				
			}
			
			g.setColor(Color.green);
			g.draw(polygonToDraw);
			g.setColor(Color.white);
		}

		
	}
	
	public void update() {
		
//		 FIXME radian / degree problem 
//		if ( Math.abs( Math.toDegrees( this.body.getAngle() ) ) > this.maxPlayerRotation){
//			this.body.setTransform(this.body.getPosition(), -10);
//			this.body.m_angularVelocity = -(this.body.m_angularVelocity * 0.75f);
//		}
		
		float currentRotation = (float) (Math.toDegrees( this.body.getAngle()) );
		if( currentRotation < -this.maxPlayerRotation){
			this.body.setTransform(this.body.getPosition(), (float)Math.toRadians(-this.maxPlayerRotation));
		}
		if( currentRotation > this.maxPlayerRotation){
			this.body.setTransform(this.body.getPosition(), (float)Math.toRadians(this.maxPlayerRotation));
		}
		
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
		if(!isGroundPounding){
			this.body.setLinearVelocity(new Vec2(this.body.getLinearVelocity().x, this.jumpPower));
		}
	}
	public void groundPound(){
		isGroundPounding = true;
		
		// TODO in game.update() eine variable hochzählen
		// dann ab bestimmten wert ausführen und anschließend wieder auf 0 setzten
		
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
	
	public ArrayList<Fixture> getSensorList(){
		return this.sensorFixtureList;
	}

}
