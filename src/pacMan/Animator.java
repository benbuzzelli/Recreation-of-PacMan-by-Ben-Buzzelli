package pacMan;

import java.awt.Image;
import java.awt.Toolkit;

public class Animator {
	
	private int frame = 0;
	
	public Image generateAnimation(int frame_gap, String[] filenames) {
		int num_images = filenames.length;
		int animation_length = num_images * frame_gap;
		
		Image character = Toolkit.getDefaultToolkit().getImage(filenames[frame / frame_gap]);
		
		frame++;
		if (frame == animation_length)
			frame = 0;
		
		return character;
	}
	
}
