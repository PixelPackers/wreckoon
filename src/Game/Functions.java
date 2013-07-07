package Game;

public class Functions {
	
	private static class Vector2d {
		public double	x;
		public double	y;
		
		public Vector2d(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public Vector2d add(Vector2d vector) {
			return new Vector2d(x + vector.x, y + vector.y);
		}
		
		public double dot(Vector2d vector) {
			return x * vector.x + y * vector.y;
		}
		
		public Vector2d mult(double s) {
			return new Vector2d(x * s, y * s);
		}
	}
	
	public static double curveAngle(double from, double to, double step) {
		Vector2d fromVector = new Vector2d(Math.cos(from), Math.sin(from));
		Vector2d toVector = new Vector2d(Math.cos(to), Math.sin(to));
		
		Vector2d currentVector = slerp(fromVector, toVector, step);
		
		double returnValue = Math.atan2(currentVector.y, currentVector.x);
		
		return (float) returnValue;
	}
	
	public static float curveValue(float dest, float current, float smoothness) {
		return current + (dest - current) / smoothness;
	}
	
	public static float randomRange(float min, float max) {
		return (float) Math.random() * (max - min) + min;
	}
	
	private static Vector2d slerp(Vector2d from, Vector2d to, double step) {
		if (step == 0)
			return from;
		if (from == to || step == 1)
			return to;
		
		double theta = Math.acos(from.dot(to));
		if (theta == 0)
			return to;
		
		double sinTheta = Math.sin(theta);
		return from.mult((Math.sin((1 - step) * theta) / sinTheta)).add(to.mult((Math.sin(step * theta) / sinTheta)));
	}
	
}
