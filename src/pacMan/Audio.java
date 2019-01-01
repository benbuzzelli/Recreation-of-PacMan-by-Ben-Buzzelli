package pacMan;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.*;

public class Audio {
	
	private String chomp = "audio_files/pacman_chomp.wav";
	private String start = "audio_files/pacman_beginning.wav";
	private String die = "audio_files/pacman_death.wav";
	private String munch = "audio_files/pacman_eatghost.wav";
	private InputStream in;
	private InputStream inStart;
	private InputStream inDie;
	private InputStream inMunch;
	private AudioStream audioStream;
	private AudioStream audioStreamStart;
	private AudioStream audioStreamDie;
	private AudioStream audioStreamMunch;
	
	public Audio() throws IOException {
		in = new FileInputStream(chomp);
		inStart = new FileInputStream(start);
		inDie = new FileInputStream(die); 
		inMunch = new FileInputStream(munch);
		audioStream = new AudioStream(in);
		audioStreamStart = new AudioStream(inStart);
		audioStreamDie = new AudioStream(inDie);
		audioStreamMunch = new AudioStream(inMunch);
	}  

	public void dotCaptureSound() {
	    AudioPlayer.player.start(audioStream);
	}
	
	public void startSound() {
		AudioPlayer.player.start(audioStreamStart);
	}
	
	public void dieSound() {
		AudioPlayer.player.start(audioStreamDie);
	}
	
	public void munchSound() {
		AudioPlayer.player.start(audioStreamMunch);
	}
	
}
