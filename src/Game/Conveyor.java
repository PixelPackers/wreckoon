package Game;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Conveyor extends GameObjectBox {

	private float		speed		= 0.3f;
	private float		x, y;
	
	private Animation	animation;
	
	public Conveyor(World world, float posX, float posY, float width, float height, float density, float friction, float restitution, boolean left)
			throws SlickException {
		
		super(world, posX, posY, width, height, 0, 0.3f, 0, null, BodyType.STATIC);
		
		System.out.println(left);
		if (left) speed = -speed;
		
		x = posX;
		y = posY;
		this.width = width;
		this.height = height;
		initializeSprite();
		
	}
	
	public void drawImage() {
		animation.draw(x - width * 0.5f, y - height * 0.5f, width, height);
	}
	
	public float getSpeed() {
		return speed;
	}
	
	private void initializeSprite() {
		
		SpriteSheet spriteSheet = null;
		
		try {
			spriteSheet = Images.getInstance().getSpriteSheet("images/conveyor.png", 450, 100);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		animation = new Animation(spriteSheet, 100);
		animation.start();
		
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	public boolean isLeft() {
		return speed < 0f;
	}
}
