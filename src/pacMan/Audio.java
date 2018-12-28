package pacMan;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.*;

public class Audio {

	public void dotCaptureSound() throws IOException {
		// open the sound file as a Java input stream
	    String chomp = "pacman_chomp.wav";
	    InputStream in = new FileInputStream(chomp);

	    // create an audiostream from the inputstream
	    AudioStream audioStream = new AudioStream(in);

	    // play the audio clip with the audioplayer class
	    AudioPlayer.player.start(audioStream);
	}
	
}
