package Game;

import org.newdawn.slick.SlickException;

public class BackgroundObject implements Comparable<BackgroundObject> {
	
	private int	type;
	private float	x, y, z;
	private boolean	flipped;
	
	public BackgroundObject(float x, float y, float z, int type, boolean flipped) throws SlickException {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.flipped = flipped;
	}
	
	@Override
	public int compareTo(BackgroundObject o) {
		if (o.z > this.z) {
			return -1;
		} else if (o.z < this.z) {
			return 1;
		} else {
			return 0;
		}
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
	
	public float getZ() {
		return z;
	}
	
	public boolean isFlipped() {
		return flipped;
	}
	
	public boolean isSelected(float mouseX, float mouseY) {
		if (mouseX < this.x - 2 || mouseY < this.y - 3 || mouseX > this.x + 2 || mouseY > this.y + 3) {
			return false;
		}
		return true;
	}
	
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
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
	
	public void setZ(float z) {
		this.z = z;
	}
	
}
