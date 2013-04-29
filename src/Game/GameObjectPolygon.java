package Game;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class GameObjectPolygon extends GameObject {
	
	protected PolygonShape polygonShape;

	public GameObjectPolygon(World world, float posX, float posY, Vec2[] verts, float density, float friction, String imgPath,
			BodyType bodyType) throws SlickException {
		super(world, posX, posY, density, friction, imgPath, bodyType);

		polygonShape = new PolygonShape();
		polygonShape.set(verts, verts.length);
		fixtureDef.shape = polygonShape;
		
//		AABB aabb = this.body.getFixtureList().m_aabb;
//		width = aabb.lowerBound.x - aabb.upperBound.x;
//		height = aabb.lowerBound.y - aabb.upperBound.y;

		super.getReadyToRumble(world);
	}

	public void drawImage() {

	}

	@Override
	public void drawOutline(Graphics g) {

		Polygon polygon = new Polygon();
		Vec2[] verts = polygonShape.getVertices();
		for (int i=0; i< polygonShape.m_vertexCount; ++i) {
			Vec2 vert = verts[i];
			Vec2 worldPoint = body.getWorldPoint(vert);
			polygon.addPoint(worldPoint.x, -worldPoint.y);
		}
		g.draw(polygon);
		
	}
	
}
