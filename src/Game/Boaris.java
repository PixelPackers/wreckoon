package Game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Boaris {
	
	private Animation	currentAnimation;
	private Animation	aniIdle;
	private Animation	aniWalk;
	
	private float x;
	private float y;
	private float width = 310f / 300f;
	private float height = 400f / 300f;
	private boolean moving;
	
	public Boaris(float x, float y) throws SlickException {
		SpriteSheet idle = Images.getInstance().getSpriteSheet("images/boarisidle.png", 310, 400);
		SpriteSheet walk = Images.getInstance().getSpriteSheet("images/boariswalkcycle.png", 330, 400);
		aniIdle = new Animation(idle, 300);
		aniWalk = new Animation(walk, 110);
		currentAnimation = aniIdle;
		this.x = x;
		this.y = y;
	}

	public void update() {
		if (moving) {
			x += 0.02;
		}
	}
	
	public void startMoving() {
		moving = true;
		currentAnimation = aniWalk;
	}
	
	public void draw(boolean debugView) {
		if (!debugView && x < 5f) {
			currentAnimation.draw(x - width/2, y - height/2, width, height);
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
}
