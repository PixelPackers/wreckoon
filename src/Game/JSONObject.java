package Game;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

// TODO make everything private
public class JSONObject {

	// TODO private
	List<RigidBody> rigidBodies;
	
	public void createShapes(World world, ArrayList<Body> jsonObjects){
		
		// for each object
		for (int i = 0; i < this.rigidBodies.size(); ++i) {
			
			BodyDef jsonBodyDef = new BodyDef();
			jsonBodyDef.type = BodyType.STATIC;
			jsonBodyDef.position.set(18f * i, 25f);
			Body jsonBody;
			jsonBody = world.createBody(jsonBodyDef);
				
			// for each polygon 
			for (int j = 0; j < this.rigidBodies.get(i).getPolygons().size(); ++j) {
				FixtureDef jsonFixtureDef = new FixtureDef();
				PolygonShape jsonShape = new PolygonShape();
				
				Vec2[] jsonPoints = new Vec2[this.rigidBodies.get(i).getPolygons().get(j).size()];
				
				// iterate over verts
				for (int k = 0; k < this.rigidBodies.get(i).getPolygons().get(j).size(); ++k) {
					jsonPoints[k] = this.rigidBodies.get(i).getPolygons().get(j).get(k).mul(1);
				}
				
				jsonShape.set(jsonPoints, this.rigidBodies.get(i).getPolygons().get(j).size());
				jsonFixtureDef.shape = jsonShape;
				jsonFixtureDef.density = 0.5f;
				jsonBody.createFixture(jsonFixtureDef);
			}
			// da gameJSON anlegen?
			jsonObjects.add(jsonBody);

		}
		
	}
	

	class RigidBody {
		private String name;
		private String imagePath;
		private Vec2 origin;
		private List<List<Vec2>> polygons;
		private List<Circle> circles;

		class Circle {
			float cx;
			float cy;
			float r;
		}

		public List<List<Vec2>> getPolygons() {
			return polygons;
		}
		
	}
	
	

}
