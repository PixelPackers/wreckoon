package Game;

public class BackgroundObject {

	private float x, y, z;
	private int imgNumber;
	
	public BackgroundObject(float x, float y, float z, int imgNumber) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.imgNumber = imgNumber;
	}
	public float getX() {
		return x;
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
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public int getImgNumber() {
		return imgNumber;
	}
	public void setImgNumber(int imgNumber) {
		this.imgNumber = imgNumber;
	}
	
}
