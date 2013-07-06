package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Conveyor extends GameObjectBox {
	
	private Vec2 position = new Vec2();
	private float speed = 0.3f;
	
	private Animation animation;
	
	public Conveyor(World world, float posX, float posY, float width, float height, float density, float friction, float restitution) throws SlickException {
		
		super(world, posX, posY, width, height, 0,0.3f,0, null, BodyType.STATIC);
		
		this.position.x = posX;
		this.position.y = posY;
		this.width 		= width;
		this.height 	= height;
		initializeSprite();
		
	}
	
	private void initializeSprite() {
	
		SpriteSheet spriteSheet = null;
		
		try {
			// TODO richtiges bild verwendenen
			spriteSheet = Images.getInstance().getSpriteSheet("images/generatoranimated.png", 	770, 1135);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		animation = new Animation(spriteSheet, 100);
		animation.start();
		
	}
	
	public void drawImage(){
		
		animation.draw( position.x-width*0.5f, -position.y-height*0.5f, width, height );	
	}
	
	public float getSpeed() {
		return speed;
	}
}
