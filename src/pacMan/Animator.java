package pacMan;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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

	public Image generateSpriteAnimation(int frame_gap, int num_sprites, String filename, PacManBoard pacman_board) throws IOException {
		File imgFile = new File(filename);
		BufferedImage spriteSheet = ImageIO.read(imgFile);
		int sprite_width = spriteSheet.getWidth(pacman_board) / num_sprites;
		int sprite_height = spriteSheet.getHeight(pacman_board);
						
		Image character;
		Image[] sprites = new Image[num_sprites];
		
		for (int i = 0; i < num_sprites; i++) {
			sprites[i] = spriteSheet.getSubimage(i * sprite_width, 0, sprite_width, sprite_height);
		}
		
		character = spriteAnimatorHelper(frame_gap, sprites);

		return character;
	}
	
	public Image spriteAnimatorHelper(int frame_gap, Image[] sprites) {
		int num_images = sprites.length;
		int animation_length = num_images * frame_gap;

		Image character = sprites[frame / frame_gap];

		frame++;
		if (frame == animation_length)
			frame = 0;

		return character;
	}
	
	public static BufferedImage imageToBufferedImage(Image im) {
	     BufferedImage bi = new BufferedImage
	        (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
	     Graphics bg = bi.getGraphics();
	     bg.drawImage(im, 0, 0, null);
	     bg.dispose();
	     return bi;
	  }

}
