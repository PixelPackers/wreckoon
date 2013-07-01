package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SteelBeam {
	
	private static float DENSITY = 8f;
	private static float FRICTION = 0.5f;
	private static float RESTITUTION = 0.2f;
	
	private static final float LOWER_LENGTH = 4f;
	
	private static Image tiedRopes;
	private static Image longRope;
	
	private GameObjectBox steelBeam;
	private GameObjectBox rope[] = new GameObjectBox[2];
	
	private RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
	
	public SteelBeam(World world, float posX, float posY, float length, float width, float height) throws SlickException {

		tiedRopes = new Image("images/ropeTriangle.png");
		longRope = new Image("images/Rope.png");

		rope[0] = new GameObjectBox(world, posX, posY, 0.3f, 0.3f, 0.5f, 0.3f, 0.3f, null, BodyType.STATIC, true);
		rope[1] = new GameObjectBox(world, posX, posY - length, 0.4f, 0.4f, 6f, 0.3f, 0.3f, null, BodyType.DYNAMIC, true);
		
		steelBeam = new GameObjectBox(world, posX, posY - length - LOWER_LENGTH, width, height, DENSITY, FRICTION, RESTITUTION,
				"images/girder" + ((int) (Math.random() * 3 + 1)) + ".png", BodyType.DYNAMIC);
		steelBeam.getBody().setAngularDamping(1f);
		steelBeam.getBody().setLinearDamping(1f);
		
		revoluteJointDef.bodyA = rope[0].getBody();
		revoluteJointDef.bodyB = rope[1].getBody();
		revoluteJointDef.collideConnected = false;
		revoluteJointDef.localAnchorA.set(new Vec2(0f, 0f));
		revoluteJointDef.localAnchorB.set(new Vec2(0f, length));
		world.createJoint(revoluteJointDef);
		
		revoluteJointDef.bodyA = steelBeam.getBody();
		revoluteJointDef.bodyB = rope[1].getBody();
		revoluteJointDef.collideConnected = false;
		revoluteJointDef.localAnchorA.set(new Vec2(0f, LOWER_LENGTH));
		revoluteJointDef.localAnchorB.set(new Vec2(0f, 0f));
		world.createJoint(revoluteJointDef);
		
	}
	
	public final void draw(Graphics g, boolean debugView){
		steelBeam.draw(g, debugView);
		if (debugView) {
			for (GameObjectBox r : rope) {
				r.draw(g, debugView);
			}
		} else {
			g.pushTransform();
			Vec2 rope0Pos = rope[0].getBody().getPosition();
			g.translate(rope0Pos.x, -rope0Pos.y);
			g.rotate(0f, 0f, (float) -Math.toDegrees(rope[1].getBody().getAngle()));
			longRope.draw(-0.1f, 0f, 0.2f, 31.1f);
			g.popTransform();
			
			g.pushTransform();
			g.translate(steelBeam.getBody().getPosition().x, -steelBeam.getBody().getPosition().y);
			g.rotate(0f, 0f, (float) -Math.toDegrees(steelBeam.getBody().getAngle()));
			tiedRopes.draw(-4f, -4.3f, 8f, 3.6f);
			g.popTransform();
		}
		
	}
	
}
