package Game;

import java.util.List;

import org.jbox2d.common.Vec2;

// TODO make everything private
public class JSONObject {

	List<RigidBody> rigidBodies;
	
	class RigidBody {
		String name;
		String imagePath;
		Vec2 origin;
		List<List<Vec2>> polygons;
		List<Circle> circles;
		
		class Circle {
			float cx;
			float cy;
			float r;
		}
	}
	
}
