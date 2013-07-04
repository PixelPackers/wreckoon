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
			verts[Math.abs(crap - 0)] = new Vec2(shit * -0.12695312f, -0.31054688f);
			verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.25f);
			verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, 0.0f);
			verts[Math.abs(crap - 3)] = new Vec2(shit * -0.087890625f, -0.04296875f);
			fixtures.add(verts);
			break;

		case 50:
			verts = new Vec2[4];
			crap = (flipped) ? 3 : 0;
			verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
			verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.0f);
			verts[Math.abs(crap - 2)] = new Vec2(shit * 0.00390625f, -0.03125f);
			verts[Math.abs(crap - 3)] = new Vec2(shit * 0.0f, -0.5f);
			fixtures.add(verts);
			break;

		case 51:
			verts = new Vec2[4];
			crap = (flipped) ? 3 : 0;
			verts[Math.abs(crap - 0)] = new Vec2(shit * -0.14257812f, -0.3125f);
			verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.25f);
			verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, 0.0f);
			verts[Math.abs(crap - 3)] = new Vec2(shit * -0.099609375f, -0.029296875f);
			fixtures.add(verts);
			break;

		case 52:
			verts = new Vec2[4];
			crap = (flipped) ? 3 : 0;
			verts[Math.abs(crap - 0)] = new Vec2(shit * -0.064453125f, -0.37890625f);
			verts[Math.abs(crap - 1)] = new Vec2(shit * 0.38085938f, -0.37695312f);
			verts[Math.abs(crap - 2)] = new Vec2(shit * 0.328125f, 0.091796875f);
			verts[Math.abs(crap - 3)] = new Vec2(shit * -0.13867188f, 0.068359375f);
			fixtures.add(verts);
			break;

		case 53:
			verts = new Vec2[4];
			crap = (flipped) ? 3 : 0;
			verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.25f);
			verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.25f);
			verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, 0.0f);
			verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, 0.0f);
			fixtures.add(verts);
			break;

		case 54:
			verts = new Vec2[4];
			crap = (flipped) ? 3 : 0;
			verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.25f);
			verts[Math.abs(crap - 1)] = new Vec2(shit * 0.38476562f, -0.38867188f);
			verts[Math.abs(crap - 2)] = new Vec2(shit * 0.375f, -0.013671875f);
			verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, 0.0f);
			fixtures.add(verts);
			break;

		case 55:
			verts = new Vec2[4];
			crap = (flipped) ? 3 : 0;
			verts[Math.abs(crap - 0)] = new Vec2(shit * -0.3984375f, -0.40429688f);
			verts[Math.abs(crap - 1)] = new Vec2(shit * 0.3984375f, -0.40429688f);
			verts[Math.abs(crap - 2)] = new Vec2(shit * 0.4140625f, -0.01171875f);
			verts[Math.abs(crap - 3)] = new Vec2(shit * -0.43164062f, 0.013671875f);
			fixtures.add(verts);
			break;

		case 58:
			verts = new Vec2[4];
			crap = (flipped) ? 3 : 0;
			verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.25f);
			verts[Math.abs(crap - 1)] = new Vec2(shit * 0.32617188f, -0.28710938f);
			verts[Math.abs(crap - 2)] = new Vec2(shit * -0.0859375f, 0.099609375f);
			verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, 0.0f);
			fixtures.add(verts);
			verts = new Vec2[4];
			crap = (flipped) ? 3 : 0;
			verts[Math.abs(crap - 0)] = new Vec2(shit * 0.32617188f, -0.28710938f);
			verts[Math.abs(crap - 1)] = new Vec2(shit * 0.25f, 0.5f);
			verts[Math.abs(crap - 2)] = new Vec2(shit * 0.0f, 0.5f);
			verts[Math.abs(crap - 3)] = new Vec2(shit * -0.0859375f, 0.099609375f);
			fixtures.add(verts);
			break;
		}
		return fixtures;
	}
	
	public Fixture getFixture() {
		return this.body.getFixtureList();
	}
	
	public void draw(Graphics g, boolean debugView){
		if (debugView) {
			this.drawOutline(g);
		}
	}

	public void drawOutline(Graphics g) {
		Fixture fixtureList = this.body.getFixtureList();

		while (fixtureList != null) {
			Polygon polygon = new Polygon();
			PolygonShape polygonShape = (PolygonShape) fixtureList.getShape();
			Vec2[] verts = polygonShape.getVertices();
			for (int i = 0; i < polygonShape.getVertexCount(); ++i) {
				Vec2 worldPoint = body.getWorldPoint(verts[i]);
				polygon.addPoint(worldPoint.x, worldPoint.y);
			}
			g.draw(polygon);
			fixtureList = fixtureList.getNext();
		}

	}
}
