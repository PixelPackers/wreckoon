package Game;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Enemy extends GameObjectBox {

	private Game game;
	private float width;
	private float height;
	
	private MySensor 	sensorTopLeft;
	private MySensor 	sensorTopRight;
	private MySensor	sensorBottomLeft;
	private MySensor 	sensorBottomRight;
	private ArrayList<MySensor> sensorList = new ArrayList<MySensor>();

	
	public Enemy(Game game, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath,
			BodyType bodyType) throws SlickException {
		super(game.getWorld(), posX, posY, width, height, density, friction, restitution, imgPath, bodyType);
		
		this.game = game;
		this.width = width;
		this.height = height;
		
		createSensors();
	}
	
	public void update(){
		Player player = this.game.getPlayer();
		float speed = 5;
		float x = speed;
		if(player.getBody().getPosition().x	< this.getBody().getPosition().x){
			x = -speed;
		}
		
		this.body.setLinearVelocity(new Vec2(x, this.body.getLinearVelocity().y) );
		
		
		
		// wenn man bei der wand ansteht --> springen
	}

	public void createSensors(){
		this.sensorList = new ArrayList<MySensor>();

		float width = this.width;
		float height = this.height;

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

		sensorBottomLeft 	= sensorList.get(0);
		sensorTopLeft		= sensorList.get(1);
		sensorBottomRight 	= sensorList.get(2);
		sensorTopRight 		= sensorList.get(3);
	}
	
	public ArrayList<MySensor> getSensorList() {
		return sensorList;
	}
	
	@Override
	public void drawOutline(Graphics g){
		super.drawOutline(g);
		
		// draw sensors
		for (MySensor mySensor : sensorList){
			mySensor.draw(g, this.body);
		}
		
	}
}