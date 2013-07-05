package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class DropItem extends GameObjectPolygon {

	private static float 	FACTOR = 0.15f;
	protected float 	DRAW_FACTOR;
	protected int	MIN_TIME;
	protected int	MAX_TIME = 1500;
	protected int	FADE_TIME = 100;
	
	protected Game game;
	protected Image image;
	protected int counter = 0;
	private boolean collectable = false;
	
	private static Vec2[] verts = new Vec2[]{
		new Vec2(-0.1875f 		* FACTOR, 	-0.38671875f 	* FACTOR),
		new Vec2(0.265625f		* FACTOR,	-0.40234375f 	* FACTOR),
		new Vec2(0.48046875f	* FACTOR,	 0.07421875f 	* FACTOR),
		new Vec2(0.2578125f		* FACTOR, 	 0.40429688f 	* FACTOR),
		new Vec2(-0.20507812f 	* FACTOR, 	 0.359375f		* FACTOR),
		new Vec2(-0.47851562f 	* FACTOR, 	 0.0078125f 	* FACTOR)
	};
		
	public DropItem(Game game, World world, Vec2 pos, String imgPath)
			throws SlickException {
		
		super(world, pos.x, pos.y, verts, 1f, 0.5f, 0f, imgPath, BodyType.DYNAMIC);
		
		this.game = game;
		this.image = Images.getInstance().getImage(imgPath);		
	}
	
	public void drawImage(){
		float radius = DRAW_FACTOR;

		float angle = this.getBody().getAngle();
		image.setRotation(-(float) Math.toDegrees(angle));
		
		
//		if(counter > MAX_TIME-FADE_TIME){
			
			
//			
//			float blub = 1f - (counter - (MAX_TIME - FADE_TIME)) / FADE_TIME;
//			System.out.println(blub);
//			image.setAlpha(blub  );
			float factor = 0.5f;
			image.setAlpha(1f - (counter-MAX_TIME*factor)/MAX_TIME/factor);
//		}
		image.draw(this.getBody().getPosition().x - radius, this.getBody().getPosition().y -radius, radius*2f, radius*2f);
		
	}
	
	public abstract void collect();
	
	public boolean isCollectable() {
		return collectable;
	}
	
	public void update(){
		++counter;
		
		if(counter > MIN_TIME){
			collectable = true;
		} 
		if (counter > MAX_TIME){

			game.getObjectsToRemove().add(this);
			game.getDropItemsToRemove().add(this);
		}
	}
}
