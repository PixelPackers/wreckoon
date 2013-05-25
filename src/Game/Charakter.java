package Game;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;

public abstract class Charakter extends GameObjectBox {

	public Charakter(World world, float posX, float posY, float width, float height, float density, float friction, float restitution, String imgPath,
			BodyType bodyType) throws SlickException {
		super(world, posX, posY, width, height, density, friction, restitution, imgPath, bodyType);
		// TODO Auto-generated constructor stub
	}

	/*
	 * extends gameobjectbox
	 * walking speed
	 * jump height
	 * 
	 * sensorlist
	 * width heihgt
	 */
	
	public void jump(){
		
	}
	
	public void die(){
		
	}
	
}
