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

public class Tile {

	private World	world;
	private Body 	body;
	
	private float 	x;
	private float 	y;
	private int 	type;
	private int 	angle;
	private boolean flipped;
	
	private static SpriteSheet img;
	
	private static final float TILE_SIZE = 1f;
	
	public Tile (World world, float x, float y, int type, int angle, boolean flipped) throws SlickException {
	
		this.world 		= world;
		this.x			= x;
		this.y			= y;
		this.flipped	= flipped;
		this.angle 		= angle;
		this.type 		= type;
		
		Tile.img = Images.getInstance().getSpriteSheet("images/tiles.png", 256, 256);
		
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
		
		for(Vec2[] verts : arrayList){

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
			case 2:
				verts = new Vec2[6];
				crap = (flipped) ? 5 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.35546875f, -0.25f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * 0.28125f, -0.26757812f);
				verts[Math.abs(crap - 5)] = new Vec2(shit * 0.24804688f, -0.32421875f);
				fixtures.add(verts);
				break;
	
			case 3:
				verts = new Vec2[6];
				crap = (flipped) ? 5 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.025390625f, -0.24804688f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.20507812f, -0.32617188f);
				verts[Math.abs(crap - 5)] = new Vec2(shit * -0.25f, -0.40429688f);
				fixtures.add(verts);
				break;
	
			case 4:
				verts = new Vec2[6];
				crap = (flipped) ? 5 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.15039062f, 0.24804688f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.21289062f, 0.21875f);
				verts[Math.abs(crap - 5)] = new Vec2(shit * -0.24804688f, 0.09375f);
				fixtures.add(verts);
				break;
	
			case 5:
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.109375f, -0.24609375f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				break;
	
			case 6:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.32421875f, -0.23242188f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.28515625f, 0.19140625f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.03515625f, 0.005859375f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.32421875f, -0.23242188f);
				fixtures.add(verts);
				break;
				
			case 7:
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.064453125f, 0.2578125f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.5f, 0.25f);
				fixtures.add(verts);
				break;
	
			case 8:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.2734375f, 0.31835938f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, 0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.2734375f, 0.31835938f);
				fixtures.add(verts);
				break;
	
			case 9:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.24804688f, -0.203125f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.24804688f, -0.203125f);
				fixtures.add(verts);
				break;
	
			case 10:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.099609375f, -0.25195312f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[3];
				crap = (flipped) ? 2 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.24609375f, -0.08984375f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.099609375f, -0.25195312f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.24609375f, -0.08984375f);
				fixtures.add(verts);
				break;
	
			case 11:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * -0.2109375f, 0.21484375f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, 0.5f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.21875f, 0.21484375f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.2109375f, 0.21484375f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.21875f, 0.21484375f);
				fixtures.add(verts);
				break;
	
			case 14:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.23046875f, -0.4453125f);
				fixtures.add(verts);
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.23046875f, -0.4453125f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.15625f, -0.25195312f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.12109375f, -0.29101562f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * 0.16210938f, -0.40625f);
				fixtures.add(verts);
				break;
	
			case 15:
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.1875f, -0.3203125f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.20117188f, -0.3203125f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.2578125f, -0.40820312f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.36523438f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.1875f, -0.3203125f);
				fixtures.add(verts);
				break;
	
			case 16:
				verts = new Vec2[3];
				crap = (flipped) ? 2 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, 0.25f);
				fixtures.add(verts);
				break;
	
			case 17:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				break;
	
			case 18:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.2421875f, -0.18359375f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.16210938f, 0.041015625f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.119140625f, -0.0703125f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.2421875f, -0.18359375f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.375f, 0.22265625f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.16210938f, 0.041015625f);
				fixtures.add(verts);
				break;
	
			case 19:
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.15625f, 0.18359375f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.07421875f, 0.24804688f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.5f, 0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.1953125f, 0.24804688f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.15625f, 0.18359375f);
				fixtures.add(verts);
				break;
	
			case 20:
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.25f, 0.5f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.5f, 0.25f);
				fixtures.add(verts);
				break;
	
			case 21:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.37304688f, -0.037109375f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.37304688f, -0.037109375f);
				fixtures.add(verts);
				break;
	
			case 22:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.25f, -0.24804688f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.25f, -0.24804688f);
				fixtures.add(verts);
				break;
	
			case 23:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * -0.25390625f, -0.22460938f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, 0.5f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.25195312f, -0.22460938f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.25390625f, -0.22460938f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.25195312f, -0.22460938f);
				fixtures.add(verts);
				break;
	
			case 26:
				verts = new Vec2[3];
				crap = (flipped) ? 2 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				fixtures.add(verts);
				break;
	
			case 28:
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.25f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.15234375f, -0.30273438f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.15039062f, -0.30664062f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.23632812f, -0.390625f);
				fixtures.add(verts);
				verts = new Vec2[3];
				crap = (flipped) ? 2 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.29492188f, -0.17773438f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.15234375f, -0.30273438f);
				fixtures.add(verts);
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.36523438f, 0.22460938f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.28710938f, 0.119140625f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * 0.29492188f, -0.17773438f);
				fixtures.add(verts);
				break;
	
			case 29:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.5f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				break;
	
			case 30:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.23632812f, -0.24804688f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[3];
				crap = (flipped) ? 2 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.31835938f, -0.14648438f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.23632812f, -0.24804688f);
				fixtures.add(verts);
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.390625f, 0.21875f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.31835938f, 0.109375f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * 0.31835938f, -0.14648438f);
				fixtures.add(verts);
				break;
	
			case 31:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * -0.37109375f, -0.34179688f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.41992188f, 0.20117188f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, 0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.27929688f, -0.33984375f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.37109375f, -0.34179688f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.41210938f, 0.21484375f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.27929688f, -0.33984375f);
				fixtures.add(verts);
				break;
	
			case 34:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.26367188f, -0.25f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.25f, 0.19921875f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.087890625f, 0.103515625f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.23828125f, -0.060546875f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.26367188f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.25f, 0.5f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.25f, 0.19921875f);
				fixtures.add(verts);
				break;
	
			case 42:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * -0.375f, -0.29296875f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.45507812f, -0.25195312f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, -0.25f);
				fixtures.add(verts);
				verts = new Vec2[5];
				crap = (flipped) ? 4 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * -0.109375f, -0.36132812f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.203125f, -0.25976562f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.2890625f, -0.22851562f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * -0.375f, -0.29296875f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.29492188f, -0.2890625f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.109375f, -0.36132812f);
				fixtures.add(verts);
				verts = new Vec2[6];
				crap = (flipped) ? 5 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.45898438f, 0.24804688f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.40625f, 0.19921875f);
				verts[Math.abs(crap - 4)] = new Vec2(shit * 0.36914062f, 0.11328125f);
				verts[Math.abs(crap - 5)] = new Vec2(shit * 0.29492188f, -0.2890625f);
				fixtures.add(verts);
				break;
	
			case 43:
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * -0.20117188f, 0.0546875f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * -0.30664062f, 0.16796875f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.5f, 0.25f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * -0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.22460938f, 0.0f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * -0.20117188f, 0.0546875f);
				fixtures.add(verts);
				verts = new Vec2[4];
				crap = (flipped) ? 3 : 0;
				verts[Math.abs(crap - 0)] = new Vec2(shit * 0.5f, -0.5f);
				verts[Math.abs(crap - 1)] = new Vec2(shit * 0.5f, 0.25f);
				verts[Math.abs(crap - 2)] = new Vec2(shit * 0.33203125f, 0.15625f);
				verts[Math.abs(crap - 3)] = new Vec2(shit * 0.22460938f, 0.0f);
				fixtures.add(verts);
				break;
				
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
//		return multiplyFixtures(fixtures, TILE_SIZE);
		return fixtures;
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
		if (debugView || this.img == null) {
			this.drawOutline(g);
		} else { 
			this.drawImage();
		}
	}
	
	public void drawImage() {
		// FIXME magic numbers
		//Image image = Tile.img.getSubImage(this.type % 12, this.type / 12).getFlippedCopy(flipped, false);
		Image image = Tile.img.getSubImage((this.type % 12) * 512 + 2, (this.type / 12) * 512 + 2, 508, 508).getFlippedCopy(!flipped, false);
		image.setRotation(180 + angle);
		image.draw(this.x - TILE_SIZE * 0.5f, this.y - TILE_SIZE * 0.5f, TILE_SIZE, TILE_SIZE);
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
				// p.addPoint(b.getPosition().x+ verts[i].x,
				// -(b.getPosition().y +verts[i].y));
			}
			g.draw(polygon);
			fixtureList = fixtureList.getNext();
		}

	}
}
