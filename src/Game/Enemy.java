package Game;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public abstract class Enemy extends GameObjectBox {
	
	protected Game game;
	protected float width;
	protected float height;
	
	private boolean dead = false;
	
	protected ArrayList<MySensor> sensorList = new ArrayList<MySensor>();
	protected MySensor 	sensorLeft;
	protected MySensor 	sensorRight;
	protected MySensor	sensorGroundCollision;

	public Enemy(Game game, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath,
			BodyType bodyType) throws SlickException {
		super(game.getWorld(), posX, posY, width, height, density, friction, restitution, imgPath, bodyType);
		
		this.game = game;
		this.width = width;
		this.height = height;
		
		createSensors();
	}
	
	private void createSensors() {

		float width = this.width;
		float height = this.height;
	
		// wall collision sensors
		float sensorSizeWidth	= width  * 0.125f;
		float sensorSizeHeight	= height * 0.4f;
		float default_xSpace = width*0.5f;
		float default_ySpace = height*0.25f;
		float xSpace = default_xSpace;
		float ySpace = default_ySpace;
		float putDown = 0f;
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
			polygonToDraw.addPoint(worldPoint.x, -worldPoint.y);
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

		float force = 15f;
		float x = (game.getPlayer().movesLeft()) ? -force : force;
		
		this.getBody().setLinearVelocity(new Vec2 (x, force) );
		
	}
	
	public void die(){
		this.dead = true;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public abstract void update();
}
