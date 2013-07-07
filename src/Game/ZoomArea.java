package Game;

import org.newdawn.slick.SlickException;

public class ZoomArea {
	
	private float	x1, y1, x2, y2, zoom;
	private boolean	flipped;
	
	public ZoomArea(float x1, float y1, float x2, float y2, float zoom) throws SlickException {
		if (x1 > x2) {
			float tmp = x1;
			x1 = x2;
			x2 = tmp;
		}
		if (y1 > y2) {
			float tmp = y1;
			y1 = y2;
			y2 = tmp;
		}
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.zoom = zoom;
	}
	
	public float getHeight() {
		return y2 - y1;
	}
	
	public float getWidth() {
		return x2 - x1;
	}
	
	public float getX1() {
		return x1;
	}
	
	public float getX2() {
		return x2;
	}
	
	public float getY1() {
		return y1;
	}
	
	public float getY2() {
		return y2;
	}
	
	public float getZoom() {
		return this.zoom;
	}
	
	public boolean isFlipped() {
		return flipped;
	}
	
	public boolean isInArea(float x, float y) {
		if (x < this.x1 || y < this.y1 || x > this.x2 || y > this.y2) {
			return false;
		}
		return true;
	}
	
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}
	
	public void setX1(float x1) {
		this.x1 = x1;
	}
	
	public void setX2(float x2) {
		this.x2 = x2;
	}
	
	public void setY1(float y1) {
		this.y1 = y1;
	}
	
	public void setY2(float y2) {
		this.y2 = y2;
	}
	
	public void setZoom(float zoom) {
		this.zoom = zoom;
	}
	
}
