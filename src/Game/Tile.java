package Game;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

public class Tile {

	private World	world;
	private Body 	body;
	
	private float 	posX;
	private float 	posY;
	private int 	type;
	private int 	angle;
	private boolean flipped;
	
	
	
	
	public Tile (World world, float posX, float posY, int type, int angle, boolean flipped) {
	
		this.world 		= world;
		this.posX		= posX;
		this.posY		= posY;
		this.flipped	= flipped;
		this.angle 		= angle;
		this.type 		= type;
		
		BodyDef bodyDef = new BodyDef(); 
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(posX, posY);
		
		ArrayList<Vec2[]> arrayList = new ArrayList<Vec2[]>();
		arrayList = chooseTile(type);

		this.body = this.world.createBody(bodyDef);

		FixtureDef fixtureDef	= new FixtureDef();
		fixtureDef.friction		= 0.3f;
		fixtureDef.restitution	= 0f;
		
		for(Vec2[] verts : arrayList){

			PolygonShape polygonShape = new PolygonShape();
			polygonShape.set(verts, verts.length);
			fixtureDef.shape		= polygonShape;
			
			this.body.createFixture(fixtureDef);
		}
			
	}
	
	public ArrayList<Vec2[]> chooseTile(int type) {

		ArrayList<Vec2[]> fixtures = new ArrayList<Vec2[]>();
		Vec2[] verts ;
		int crap = 0;
		
		switch (type) {		
			
			case 2:
				verts = new Vec2[6];
				crap = (flipped) ? 6 : 0;
				
				verts[crap - 0] = new Vec2(0.75f, 			0.0f);
				verts[crap - 1] = new Vec2(1.0f, 			0.0f);
				verts[crap - 2] = new Vec2(1.0f, 			0.25f);
				verts[crap - 3] = new Vec2(0.8828125f, 	0.26171875f);
				verts[crap - 4] = new Vec2(0.77734375f, 	0.23242188f);
				verts[crap - 5] = new Vec2(0.7421875f, 	0.17382812f);

				fixtures.add(verts);
				break;
				
			case 4:
				verts = new Vec2[3];
				crap = (flipped) ? 3 : 0;
				
				verts[0] = new Vec2(0.25f, 			0.0f);
				verts[1] = new Vec2(1.0f, 			0.0f);
				verts[2] = new Vec2(0.26171875f, 	0.26367188f);
				fixtures.add(verts);
			
				verts = new Vec2[6];
				verts[0] = new Vec2(1.0f, 			0.0f);
				verts[1] = new Vec2(0.5800781f, 	0.7128906f);
				verts[2] = new Vec2(0.37695312f, 	0.7441406f);
				verts[3] = new Vec2(0.28515625f, 	0.7089844f);
				verts[4] = new Vec2(0.2421875f, 	0.5546875f);
				verts[5] = new Vec2(0.26171875f, 	0.26367188f);
				fixtures.add(verts);
			
				verts = new Vec2[4];
				verts[0] = new Vec2(1.0f, 		0.0f);
				verts[1] = new Vec2(1.0f, 		0.75f);
				verts[2] = new Vec2(0.7578125f, 0.7578125f);
				verts[3] = new Vec2(0.5800781f, 0.7128906f);
				fixtures.add(verts);
				
				break;
				
			}
		float factor = 4;
		return multiplyFixtures(fixtures, factor);
	}
	
	
	private ArrayList<Vec2[]> multiplyFixtures(ArrayList<Vec2[]> fixtures, float factor) {
		
		ArrayList<Vec2[]> newFixtures = new ArrayList<>();
		
		for(Vec2[] verts : fixtures){
			
			Vec2[] newVerts = new Vec2[verts.length];
			
			for (int i=0; i<verts.length; ++i) {
				newVerts[i] = verts[i].mul(factor);
			}
			
			newFixtures.add(newVerts);
		}
		
		return newFixtures;
	}

	public void draw(Graphics g) {
		Fixture fixtureList = this.body.getFixtureList();
		// System.out.println(f.m_shape);
		while (fixtureList != null) {
			Polygon polygon = new Polygon();
			PolygonShape polygonShape = (PolygonShape) fixtureList.getShape();

			Vec2[] verts = polygonShape.getVertices();
			for (int i = 0; i < polygonShape.getVertexCount(); ++i) {
				Vec2 worldPoint = body.getWorldPoint(verts[i]);
				polygon.addPoint(worldPoint.x, -worldPoint.y);
				// p.addPoint(b.getPosition().x+ verts[i].x,
				// -(b.getPosition().y +verts[i].y));
			}
			g.pushTransform();
			g.draw(polygon);

			g.popTransform();
			fixtureList = fixtureList.getNext();
		}

	}
}
