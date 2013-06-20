package Game;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class MusicManager {
	
	private static final MusicManager instance = new MusicManager();

	// XXX hashmap? evtl extra sfx und music getrennt
	private Music itemCollected;
	private Music bgMusic;
	
	private MusicManager() {
		
		// TODO kA bringt das was? aendert das was?
		try {
			loadAudioFiles();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private void loadAudioFiles() throws SlickException {
		itemCollected	= new Music("audio/part_collected.wav");
		bgMusic 		= new Music("audio/Part 1_ Loop _Lang.wav");
	}
	
	public static MusicManager getInstance() {
		return instance;
	}
	
	public void bgMusic() {
		this.bgMusic.play();
		this.bgMusic.loop();
	}
	
	public void itemCollected(){
		itemCollected.play();
	}

}
