package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class DropItem extends GameObjectPolygon {

	
	private static float	FACTOR		= 0.15f;
	protected float			DRAW_FACTOR;
	protected int			MIN_TIME;
	protected int			MAX_TIME	= 1500;
	protected int			FADE_TIME	= 100;
	
	protected Game			game;
	protected Image			image;
	protected int			counter		= 0;
	private boolean			collectable	= false;
	
	// @formatter:off
	private static Vec2[] verts = new Vec2[]{
			new Vec2(-0.37695312f * FACTOR, -0.40429688f * FACTOR),
			new Vec2(0.15625f * FACTOR, -0.46875f * FACTOR),
			new Vec2(0.49414062f * FACTOR, -0.080078125f * FACTOR),
			new Vec2(0.3203125f * FACTOR, 0.40039062f * FACTOR),
			new Vec2(-0.16796875f * FACTOR, 0.46875f * FACTOR),
			new Vec2(-0.46679688f * FACTOR, 0.13867188f * FACTOR),
	};
	// @formatter:on
	
	public DropItem(Game game, World world, Vec2 pos, String imgPath) throws SlickException {
		
		super(world, pos.x, pos.y, verts, 1f, 0.5f, 0f, imgPath, BodyType.DYNAMIC);
		
		this.game = game;
		this.image = Images.getInstance().getImage(imgPath);
	}
	
	public abstract void collect();
	
	public void drawImage() {
		float radius = DRAW_FACTOR;
		
		float angle = this.getBody().getAngle();
		image.setRotation((float) Math.toDegrees(angle));
		
		// if(counter > MAX_TIME-FADE_TIME){
		
		//
		// float blub = 1f - (counter - (MAX_TIME - FADE_TIME)) / FADE_TIME;
		// System.out.println(blub);
		// image.setAlpha(blub );
		float factor = 0.5f;
		image.setAlpha(1f - (counter - MAX_TIME * factor) / MAX_TIME / factor);
		// }
		image.draw(this.getBody().getPosition().x - radius, this.getBody().getPosition().y - radius, radius * 2f, radius * 2f);
		
	}
	
	public boolean isCollectable() {
		return collectable;
	}
	
	public void throwback() {
		
		// TODO player position vektor zum objekt bestimmen und in die richtung
		// dann thrown
		// TODO random
		float force = 7.5f;
		float x = (game.getPlayer().movesLeft()) ? -force : force;
		
		this.getBody().setLinearVelocity(new Vec2(x, -force * 0.75f));
		
	}
	
	public void update() {
		++counter;
		
		if (counter > MIN_TIME) {
			collectable = true;
		}
		if (counter > MAX_TIME) {
			
			Game.getObjectsToRemove().add(this);
			Game.getDropItemsToRemove().add(this);
		}
	}
}
