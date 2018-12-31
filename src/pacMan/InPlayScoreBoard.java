package pacMan;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class InPlayScoreBoard {

	public enum Number {
		ZERO("images_score_panel/0.png", 0), ONE("images_score_panel/1.png", 1), TWO("images_score_panel/2.png", 2),
		THREE("images_score_panel/3.png", 3), FOUR("images_score_panel/4.png", 4), FIVE("images_score_panel/5.png", 5),
		SIX("images_score_panel/6.png", 6), SEVEN("images_score_panel/7.png", 7), EIGHT("images_score_panel/8.png", 8),
		NINE("images_score_panel/9.png", 9);

		String filename;
		int num;

		Number(String filename, int num) {
			this.filename = filename;
			this.num = num;
		}
	}

	private final ArrayList<String> char_panel = new ArrayList<>();
	private ScoreTyleContainer.ScoreTyle[][] score_panel;
	private Number[] number = { Number.ZERO, Number.ONE, Number.TWO, Number.THREE, Number.FOUR, Number.FIVE, Number.SIX,
			Number.SEVEN, Number.EIGHT, Number.NINE };

	public void drawScorePanel(Graphics g, PacManBoard pacman_board) {
		int rows = char_panel.size();
		int columns = char_panel.get(0).length();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				Image piece = Toolkit.getDefaultToolkit().getImage(score_panel[i][j].filename);
				g.drawImage(piece, PacManBoard.dimension * j, PacManBoard.dimension * i, PacManBoard.dimension,
						PacManBoard.dimension, pacman_board);
			}
		}
	}

	public void drawScore(Graphics g, PacManBoard pacman_board) {
		String score = Integer.toString(PacManBoard.totalScore);
		int length = score.length();
		
		if (PacManBoard.totalScore == 0) {
			Image zero = Toolkit.getDefaultToolkit().getImage(number[0].filename);
			g.drawImage(zero, PacManBoard.dimension * 8, PacManBoard.dimension*2, PacManBoard.dimension,
					PacManBoard.dimension, pacman_board);
			g.drawImage(zero, PacManBoard.dimension * 8 - PacManBoard.dimension, PacManBoard.dimension*2, PacManBoard.dimension,
					PacManBoard.dimension, pacman_board);
			return;
		}		
		
		for (int i = 0; i < length; i++) {
			int index = score.charAt((length-1) - i) - '0';
			Image piece = Toolkit.getDefaultToolkit().getImage(number[index].filename);
			g.drawImage(piece, PacManBoard.dimension * 8 - PacManBoard.dimension * i, PacManBoard.dimension*2, PacManBoard.dimension,
					PacManBoard.dimension, pacman_board);
		}
	}

	public void createScorePanel() throws FileNotFoundException {
		createBoard();
		setScorePanel(char_panel.size(), char_panel.get(0).length());
	}

	private void createBoard() throws FileNotFoundException {
		getBoard("scorePanel.txt");
	}

	private void getBoard(String file_name) throws FileNotFoundException {
		File file = new File(file_name);
		Scanner in = new Scanner(file);

		while (in.hasNextLine()) {
			char_panel.add(in.nextLine());
		}

		in.close();
	}

	private void setScorePanel(int boardRows, int boardColumns) {
		score_panel = new ScoreTyleContainer.ScoreTyle[boardRows][boardColumns];
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardColumns; j++) {
				for (ScoreTyleContainer.ScoreTyle scoreTyle : ScoreTyleContainer.ScoreTyle.values()) {
					if (scoreTyle.c == char_panel.get(i).charAt(j))
						score_panel[i][j] = scoreTyle;
				}
			}
		}
	}
	
	public ArrayList<String> getCharPanel() {
		return char_panel;
	}
	
}
