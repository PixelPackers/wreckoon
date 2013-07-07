package Game;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

// FIXME wenn er das erste mal audio abspielt, hängts leicht, kA evtl muss der dann erst den musicManager initialisieren oder kA
// wenn dann bg music von anfang an is, sollts doch hin haun... kA

public class Sounds {
	
	private static final Sounds instance = new Sounds();

	// XXX hashmap? evtl extra sfx und music getrennt
	private Sound itemCollected;
	private Sound bgMusic;
	
	private HashMap<String, Audio> sounds = new HashMap<String, Audio>();
	
	private Sounds() {
		// TODO kA bringt das was? aendert das was?
		try {
			loadAudioFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadAudioFiles() throws SlickException, IOException {
//		itemCollected	= new Sound("audio/part_collected.wav");
//		bgMusic 		= new Sound("audio/Part 1_ Loop _Lang.wav");
		putSound("jump");
		putSound("pigdeath");
		putSound("laser", "audio/laser3.ogg");
		putSound("death");
		putSound("bite");
		putSound("pigaggro");
		putSound("tailwhip");
		putSound("fence");
	}
	
	private void putSound(String name) throws IOException {
		putSound(name, "audio/" + name + ".ogg");
	}
	
	private void putSound(String name, String path) throws IOException {
		sounds.put(name, AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(path)));
	}
	
	public static Sounds getInstance() {
		return instance;
	}

	public void play(String sound, float pitch, float volume) {
		sounds.get(sound).playAsSoundEffect(pitch, volume, false);
	}
	
	public void loop(String sound, float pitch, float volume) {
		sounds.get(sound).playAsSoundEffect(pitch, volume, true);
	}
	
	public void stop(String sound) {
		sounds.get(sound).stop();
	}
	
	public boolean isPlaying(String sound) {
		return sounds.get(sound).isPlaying();
	}

}
