package Game;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public class Generator extends GameObjectBox {

	public Generator(World world, float posX, float posY, float width, float height) throws SlickException {
		super(world, posX, posY, width, height, 0, 0, 0, "images/generator.png", BodyType.STATIC, true);
		// TODO Auto-generated constructor stub
	}

	
}
