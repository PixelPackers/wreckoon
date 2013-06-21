package Game;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.openal.SoundStore;

// FIXME wenn er das erste mal audio abspielt, hängts leicht, kA evtl muss der dann erst den musicManager initialisieren oder kA
// wenn dann bg music von anfang an is, sollts doch hin haun... kA

public class MusicManager {
	
	private static final MusicManager instance = new MusicManager();

	// XXX hashmap? evtl extra sfx und music getrennt
	private Sound itemCollected;
	private Sound bgMusic;
	
	private MusicManager() {
		
		// TODO kA bringt das was? aendert das was?
		try {
			loadAudioFiles();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private void loadAudioFiles() throws SlickException {
		itemCollected	= new Sound("audio/part_collected.wav");
		bgMusic 		= new Sound("audio/Part 1_ Loop _Lang.wav");
	}
	
	public static MusicManager getInstance() {
		return instance;
	}
	
	public void bgMusic() {
		this.bgMusic.play(1f, 0.2f);
		this.bgMusic.loop();
	}
	
	public void itemCollected(){
		itemCollected.play();
	}

}
