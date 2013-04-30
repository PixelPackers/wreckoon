package Game;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class GameObjectPolygon extends GameObject {
	
	protected PolygonShape polygonShape;

	public GameObjectPolygon(World world, float posX, float posY, Vec2[] verts, float density, float friction, float restitution, String imgPath,
			BodyType bodyType, boolean fixedRotation) throws SlickException {
		super(world, posX, posY, density, friction, restitution, imgPath, bodyType, fixedRotation);

		this.polygonShape = new PolygonShape();
		this.polygonShape.set(verts, verts.length);
		this.fixtureDef.shape = polygonShape;
		
//		AABB aabb = this.body.getFixtureList().m_aabb;
//		this.width = aabb.lowerBound.x - aabb.upperBound.x;
//		this.height = aabb.lowerBound.y - aabb.upperBound.y;
//		this.width  = 1f;
//		this.height = 1f;

		super.getReadyToRumble(world);
	}

	public void drawImage() {
		Vec2 position = this.body.getPosition();
		float angle = this.body.getAngle();
		img.setRotation(-(float) Math.toDegrees(angle));
		img.draw(position.x, -position.y, 10, 10);
	}

	@Override
	public void drawOutline(Graphics g) {

		Polygon polygonToDraw = new Polygon();
		Vec2[] verts = this.polygonShape.getVertices();
		for (int i=0; i< this.polygonShape.m_vertexCount; ++i) {
			Vec2 vert = verts[i];
			Vec2 worldPoint = this.body.getWorldPoint(vert);
			polygonToDraw.addPoint(worldPoint.x, -worldPoint.y);
		}
		g.draw(polygonToDraw);
		
	}
	
}
