package Game;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;



public class Spikes extends GameObjectBox{

	public Spikes(World world, float posX, float posY, float width, float height ) throws SlickException {
		super(world, posX, posY, width, height, 0,0,0, null, BodyType.STATIC, true);
	}

	

}
