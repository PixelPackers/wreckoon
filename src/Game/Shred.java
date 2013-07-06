package Game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Shred extends DropItem {
	
	private float pigSizeFactor;
	private boolean grilled = false;
		
	public Shred(Game game, World world, Vec2 pos, String imgPath, float pigSizeFactor)
			throws SlickException {
		super(game, world, pos, imgPath);
		
		this.pigSizeFactor = pigSizeFactor;
		MIN_TIME = 115;
		MAX_TIME = (int) ( 1800 * Math.random() );
		
		DRAW_FACTOR = 0.17f * pigSizeFactor;
		
		Filter filter = new Filter();
		filter.maskBits = 1 + 8 + 16;
		filter.categoryBits = 16;
		this.getFixture().setFilterData(filter);
	}
	
	public void collect(){
	}
	
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public boolean isCollectable() {
		return false;
	}
	
	public void grilled() throws SlickException{
		if(!grilled){
			super.image = new Image( image.getResourceReference().replace(".png", "laser.png") ) ;
			this.grilled = true;
		}
	}
	
}
