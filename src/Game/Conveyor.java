package Game;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Conveyor extends GameObjectBox {
	
	private static final float		SPEED	= 0.3f;
	private boolean		left;
	private float		x, y;
	
	private Animation	animation;
	
	public Conveyor(World world, float posX, float posY, float width, float height, float density, float friction, float restitution,
			boolean left) throws SlickException {
		
		super(world, posX, posY, width, height, 0, 0.3f, 0, null, BodyType.STATIC);
	
		this.left = left;  
		
		x = posX;
		y = posY;
		this.width = width;
		this.height = height;
		initializeSprite();
		
	}
	
	public void drawImage() {
		float drawWidth = (left) ? -width : width;
		animation.draw(x - drawWidth * 0.5f, y - height * 0.5f, drawWidth, height);
	}
	
	public float getSpeed() {
		return (left) ? -SPEED : SPEED;
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

	public boolean getLeft() {
		return left;
	}
}
