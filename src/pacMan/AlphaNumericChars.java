package pacMan;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AlphaNumericChars {
	public BufferedImage[][] alphaNumImages = new BufferedImage[6][39];
	
	public AlphaNumericChars(PacManBoard pacman_board) throws IOException {
		File imgFile = new File("images/alphaNumCharsSpriteSheet.png");
		BufferedImage spriteSheet = ImageIO.read(imgFile);
		int sprite_width = spriteSheet.getWidth(pacman_board) / 39;
		int sprite_height = spriteSheet.getHeight(pacman_board) / 6;
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 39; j++) {
				alphaNumImages[i][j] = spriteSheet.getSubimage(j * sprite_width, i * sprite_height, sprite_width, sprite_height);
			}
		}
	}
	
}
