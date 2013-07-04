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
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;

public class Spike {

	private World	world;
	private Body 	body;
	
	private float 	x;
	private float 	y;
	private int 	type;
	private int 	angle;
	private boolean flipped;
	
	private static final float TILE_SIZE = 1f;
	
//	public Spikes(World world, float posX, float posY, float width, float height ) throws SlickException {
//		super(world, posX, posY, width, height, 0,0,0, null, BodyType.STATIC, true);
//	}
	
	public Spike (World world, float x, float y, int type, int angle, boolean flipped) throws SlickException {
	
		this.world 		= world;
		this.x			= x;
		this.y			= y;
		this.flipped	= flipped;
		this.angle 		= angle;
		this.type 		= type;
		
		BodyDef bodyDef = new BodyDef(); 
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(x, y);
		bodyDef.angle = (float) Math.toRadians(angle);
		
		ArrayList<Vec2[]> arrayList = new ArrayList<Vec2[]>();
		arrayList = chooseTile();

		this.body = this.world.createBody(bodyDef);

		FixtureDef fixtureDef	= new FixtureDef();
//		fixtureDef.friction		= 0.8f;
		fixtureDef.restitution	= 0f;
		fixtureDef.isSensor = true;
		
		for (Vec2[] verts : arrayList) {

			PolygonShape polygonShape = new PolygonShape();
			polygonShape.set(verts, verts.length);
			fixtureDef.shape		= polygonShape;
			
			this.body.createFixture(fixtureDef);
		}
			
	}
	
	public ArrayList<Vec2[]> chooseTile() {

		ArrayList<Vec2[]> fixtures = new ArrayList<Vec2[]>();
		Vec2[] verts ;
		int crap = 0;
		float shit = (flipped) ? -1f : 1f;
		
		switch (type) {
			case 41:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				break;
		
			case 50:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.26171875f, -0.28320312f);
				fixtures.add(verts);
				break;
		
			case 51:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.21289062f, -0.33007812f);
				fixtures.add(verts);
				break;
		
			case 52:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.28125f, -0.30859375f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.21679688f, -0.3359375f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.24023438f, 0.1953125f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.28125f, -0.30859375f);
				fixtures.add(verts);
				break;
		
			case 53:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				break;
		
			case 54:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.27539062f, -0.28515625f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.36914062f, 0.2109375f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.27539062f, -0.28515625f);
				fixtures.add(verts);
				break;
		
			case 55:
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * -0.3671875f, -0.35742188f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.40429688f, 0.1640625f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.453125f, 0.24609375f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.5f, 0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.296875f, -0.3515625f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.3671875f, -0.35742188f);
				fixtures.add(verts);
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.4375f, 0.23632812f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.38085938f, 0.1640625f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * 0.296875f, -0.3515625f);
				fixtures.add(verts);
				break;
		
			case 58:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.25390625f, -0.24414062f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.25390625f, -0.24414062f);
				fixtures.add(verts);
				break;
		}
		return fixtures;
	}
	
	public Fixture getFixture() {
		return this.body.getFixtureList();
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
	
	public void draw(Graphics g, boolean debugView){
		if (debugView) {
			this.drawOutline(g);
		}
	}

	public void drawOutline(Graphics g) {
		Fixture fixtureList = this.body.getFixtureList();
		// System.out.println(f.m_shape);
		while (fixtureList != null) {
			Polygon polygon = new Polygon();
			PolygonShape polygonShape = (PolygonShape) fixtureList.getShape();

			Vec2[] verts = polygonShape.getVertices();
			for (int i = 0; i < polygonShape.getVertexCount(); ++i) {
				Vec2 worldPoint = body.getWorldPoint(verts[i]);
				System.out.println(worldPoint.x);
				polygon.addPoint(worldPoint.x, worldPoint.y);
				// p.addPoint(b.getPosition().x+ verts[i].x,
				// -(b.getPosition().y +verts[i].y));
			}
			g.draw(polygon);
			fixtureList = fixtureList.getNext();
		}

	}
}
