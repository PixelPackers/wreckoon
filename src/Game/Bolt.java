package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bolt extends GameObjectPolygon {
	
	private static final float FACTOR = 0.2f;
	
	private Game game;
	private Image image;
	
	private static Vec2[] verts = new Vec2[]{
		new Vec2(-0.1875f 		* FACTOR, 	-0.38671875f 	* FACTOR),
		new Vec2(0.265625f		* FACTOR,	-0.40234375f 	* FACTOR),
		new Vec2(0.48046875f	* FACTOR,	 0.07421875f 	* FACTOR),
		new Vec2(0.2578125f		* FACTOR, 	 0.40429688f 	* FACTOR),
		new Vec2(-0.20507812f 	* FACTOR, 	 0.359375f		* FACTOR),
		new Vec2(-0.47851562f 	* FACTOR, 	 0.0078125f 	* FACTOR)
	};
		
	public Bolt(Game game, World world, Vec2 pos, String imgPath)
			throws SlickException {
		
		super(world, pos.x, pos.y, verts, 1f, 0.5f, 0f, imgPath, BodyType.DYNAMIC);
		
		this.game = game;
		this.image = new Image(imgPath);		
	}
	
	public void drawImage(){
		float radius = 0.1f;

		float angle = this.getBody().getAngle();
		image.setRotation(-(float) Math.toDegrees(angle));
		
		image.draw(this.getBody().getPosition().x - radius, this.getBody().getPosition().y -radius, radius*2f, radius*2f);
	}
}
