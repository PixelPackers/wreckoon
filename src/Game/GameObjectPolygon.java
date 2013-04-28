package Game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

public class GameObjectPolygon {
	
	private Body body;
	private BodyDef bodyDef;
	private PolygonShape polygonShape;
	private FixtureDef fixtureDef;
	
	
	public GameObjectPolygon(World world, float posX, float posY, Vec2[] polygonVertices){
		
		bodyDef 		= new BodyDef();
		// FIXME funkt nur mit static
		bodyDef.type = BodyType.DYNAMIC;
//		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(posX, posY);

		polygonShape	= new PolygonShape();
		polygonShape.set(polygonVertices, polygonVertices.length);
		
		fixtureDef 		= new FixtureDef();
		fixtureDef.density = 0.11f;
		fixtureDef.friction = 0.1f;
		fixtureDef.shape = polygonShape;

		
//		XXX was is der unterschied zwischen den naechsten 2 zeilen?
//		body			= new Body(bodyDef, world);
		body = world.createBody( bodyDef );
		body.createFixture( fixtureDef );
		
	}


	public void draw(Graphics g) {
		Polygon p = new Polygon();
		Vec2[] verts = polygonShape.getVertices();
		for (Vec2 v : verts){
			Vec2 worldPoint = body.getWorldPoint(v);
			p.addPoint(worldPoint.x, -worldPoint.y);
//			p.addPoint(bodyDef.position.x+ v.x, -(bodyDef.position.y +v.y));
		}
		g.draw(p);
		
		
	}
}
