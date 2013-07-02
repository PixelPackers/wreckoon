package Game;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import Game.MusicManager.sfx;

public class MusicPortal extends GameObjectBox {

	sfx sound;
	
	public MusicPortal( World world, float posX, float posY, float width, float height, MusicManager.sfx sound) throws SlickException {
		super(world, posX, posY, width, height, 0, 0, 0, null, BodyType.STATIC, true);
		this.sound = sound;
		
	}

	
}
