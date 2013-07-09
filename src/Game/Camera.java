package Game;

import org.jbox2d.common.Vec2;

public class Camera {
	
	private final float	WIGGLE_INTENSITY	= 0.2f;
	
	private float		x;
	private float		y;
	private float		wiggleX;
	private float		wiggleY;
	
	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void follow(float destX, float destY, float smoothness) {
		x = Functions.curveValue(destX, x, smoothness);
		y = Functions.curveValue(destY, y, smoothness);
	}
	
	public float getX() {
		return x + wiggleX;
	}
	
	public float getY() {
		return y + wiggleY;
	}
	
	public void move(float dX, float dY) {
		this.x += dX;
		this.y += dY;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void wiggle(float magnitude) {
		wiggleX = (float) Math.random() * WIGGLE_INTENSITY * magnitude;
		wiggleY = (float) Math.random() * WIGGLE_INTENSITY * magnitude;
	}

	public void setPosition(Vec2 startingPoint) {
		this.x = startingPoint.x;
		this.y = startingPoint.y;
	}
	
}
