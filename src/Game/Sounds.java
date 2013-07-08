package Game;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

// FIXME wenn er das erste mal audio abspielt, hängts leicht, kA evtl muss der
// dann erst den musicManager initialisieren oder kA
// wenn dann bg music von anfang an is, sollts doch hin haun... kA

public class Sounds {
	
	private static final Sounds	instance	= new Sounds();
	
	public static Sounds getInstance() {
		return instance;
	}
	
	private HashMap<String, Audio>	sounds	= new HashMap<String, Audio>();
	
	private Sounds() {
	}
	
	public boolean isPlaying(String sound) {
		return sounds.get(sound).isPlaying();
	}
	
	public void loadAudioFiles() {
		try {
			putSound("jump");
			putSound("pigdeath");
			putSound("laser", "audio/laser3.ogg");
			putSound("laserreverb");
			putSound("death");
			putSound("bite");
			putSound("pigaggro");
			putSound("tailwhip");
			putSound("fence");
			putSound("bgmusic", "audio/menu.ogg");
			putSound("bgmusic2", "audio/menu.ogg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Audio getSound(String sound) {
		return sounds.get(sound);
	}
	
	public void loopMusic(String sound, float pitch, float volume) {
		sounds.get(sound).playAsMusic(pitch, volume, true);
	}
	
	public void loop(String sound, float pitch, float volume) {
		sounds.get(sound).playAsSoundEffect(pitch, volume, true);
	}
	
	public void play(String sound, float pitch, float volume) {
		sounds.get(sound).playAsSoundEffect(pitch, volume, false);
	}
	
	// public void playAsMusic(String sound, float pitch, float volume) {
	// sounds.get(sound).playAsMusic(pitch, volume, false);
	// }
	
	private void putSound(String name) throws IOException {
		putSound(name, "audio/" + name + ".ogg");
	}
	
	private void putSound(String name, String path) throws IOException {
		sounds.put(name, AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(path)));
	}
	
	public void stop(String sound) {
		sounds.get(sound).stop();
	}
	
}
