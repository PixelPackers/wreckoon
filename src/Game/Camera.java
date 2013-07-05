package Game;

public class Camera {

	private final float WIGGLE_INTENSITY = 0.2f;
	
	private float x;
	private float y;
	private float wiggleX;
	private float wiggleY;

	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x + wiggleX;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y + wiggleY;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void move(float dX, float dY) {
		this.x += dX;
		this.y += dY;
	}

	public void follow(float destX, float destY, float smoothness) {
		x = curveValue(destX, x, smoothness);
		y = curveValue(destY, y, smoothness);
	}
	
	public void wiggle(float magnitude) {
		wiggleX = (float) Math.random() * WIGGLE_INTENSITY * magnitude;
		wiggleY = (float) Math.random() * WIGGLE_INTENSITY * magnitude;
	}

	public float curveValue(float dest, float current, float smoothness) {
		return current + (dest - current) / smoothness;
	}

}
