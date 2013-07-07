package Game;

import org.newdawn.slick.SlickException;

public class SpawnPoint {
	
	// 0 - Primitive
	// 1 - Stupid Follower
	
	private int	type;
	
	private float	x, y;
	
	public SpawnPoint(float x, float y, int type) throws SlickException {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public boolean isSelected(float mouseX, float mouseY) {
		if (Math.pow((mouseX - this.x), 2) + Math.pow((mouseY - this.y), 2) > 0.25f) {
			return false;
		}
		return true;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
}
