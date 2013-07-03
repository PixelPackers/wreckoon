package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Generator extends GameObjectBox {

	private Vec2 position = new Vec2();
	private float width;
	private float height;
	
	private Animation animation;
	
	public Generator(World world, float posX, float posY, float width, float height) throws SlickException {
		super(world, posX, posY, width, height, 0, 0, 0, "images/generator.png", BodyType.STATIC, true);

		this.position.x = posX;
		this.position.y = posY;
		this.width 		= width;
		this.height 	= height;
		initializeSprite();
	}

	private void initializeSprite() {
		SpriteSheet spriteSheet = null;
		
		try {
			spriteSheet = new SpriteSheet("images/generatoranimated.png", 	770, 1135);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		animation = new Animation(spriteSheet, 100);
		animation.setPingPong(true);
		animation.start();
	}
	
	@Override
	public void drawImage(){
		
		animation.draw( position.x-width*0.5f, position.y-height*0.5f, width, height );
		
	}
	
}
