package pacMan;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ScoreBoard {
	
	private final ArrayList<String> char_panel = new ArrayList<>();
	private AlphaNumericChars alphaNumChars;
	private PacManBoard pacman_board;
	private List<Integer> highscores = new ArrayList<>();
	
	private int[] scoreRows = new int[10];
	
	public ScoreBoard(PacManBoard pacman_board) throws IOException {
		alphaNumChars = new AlphaNumericChars(pacman_board);
		getBoard();
		getScores("high_score.txt");
	}

	private void getBoard() throws FileNotFoundException {
		File file = new File("score_board.txt");
		Scanner in = new Scanner(file);

		while (in.hasNextLine()) {
			char_panel.add(in.nextLine());
		}

		in.close();
	}
	
	public void drawScoreBoard(Graphics g) throws IOException {
		int rows = char_panel.size();
		int columns = char_panel.get(0).length();
		int[] colorIndex = new int[38];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (j == 0) {
					if (char_panel.get(i).charAt(j) != ' ') {
						scoreRows[char_panel.get(i).charAt(j) - '0'] = i;
					}
					continue;
				}
				File imgFile = new File("images/black_square.png");
				BufferedImage piece;
				int posX = PacManBoard.dimension * (j-1);
				int posY = PacManBoard.dimension * i;
				int index = charIndexHelper(char_panel.get(i).charAt(j));
				if (index < 0)
					piece = ImageIO.read(imgFile);
				else if (index > 41) {
					colorIndex[i] = index - 42;
					piece = ImageIO.read(imgFile);
				} else
					piece = alphaNumChars.alphaNumImages[colorIndex[i]][index];
				
				if (i == 1)
					posY -= PacManBoard.dimension / 4;
				g.drawImage(piece, posX, posY, PacManBoard.dimension,
						PacManBoard.dimension, pacman_board);
			}
		}
		
		drawScore(g, highscores, scoreRows, colorIndex);
	}
	
	private int charIndexHelper(char character) {
		int index = character - 'A';
		if (index >= 0 && index < 26)
			index += 10;
		else if (index < 0)
			index += 17;
		else if (index > 25)
			index += 10;

		return index;
	}
	
	public void drawScore(Graphics g, List<Integer> highscores, int[] scoreRows, int[] colorIndex) {
		for (int n = 0; n < scoreRows.length; n++) {
			
			if (n >= highscores.size()) {
				Image zero = alphaNumChars.alphaNumImages[0][0];
				g.drawImage(zero, PacManBoard.dimension * 19, PacManBoard.dimension*scoreRows[n], PacManBoard.dimension,
						PacManBoard.dimension, pacman_board);
				g.drawImage(zero, PacManBoard.dimension * 19 - PacManBoard.dimension, PacManBoard.dimension*scoreRows[n], PacManBoard.dimension,
						PacManBoard.dimension, pacman_board);
				return;
			}
			
			String score = Integer.toString(highscores.get(n));
			int length = score.length();
			
			for (int i = 0; i < length; i++) {
				int index = score.charAt((length-1) - i) - '0';
				Image piece = alphaNumChars.alphaNumImages[colorIndex[scoreRows[n]]][index];
				g.drawImage(piece, PacManBoard.dimension * 19 - PacManBoard.dimension * i, PacManBoard.dimension*scoreRows[n], PacManBoard.dimension,
						PacManBoard.dimension, pacman_board);
			}
		}
	}
	
	public void getScores(String file_name) throws FileNotFoundException {
		File file = new File(file_name);
		Scanner in = new Scanner(file);

		while (in.hasNextLine()) {
			highscores.add(Integer.parseInt(in.nextLine()));
		}
		Collections.sort(PacManBoard.highscores);
		in.close();
	}
	
}
