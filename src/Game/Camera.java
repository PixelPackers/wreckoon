package Game;

public class Camera {
	
	private float x, y;

	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return -x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
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
	
	public float curveValue(float dest, float current, float smoothness) {
		return current + (dest - current) / smoothness;
	}
	
}
