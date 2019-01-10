package pacMan;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class InPlayScoreBoard {

	private AlphaNumericChars alphaNumChars;
	private PacManBoard pacman_board;
	private final ArrayList<String> char_panel = new ArrayList<>();
	private ScoreTyleContainer.ScoreTyle[][] score_panel;
	
	public InPlayScoreBoard(PacManBoard pacman_board) throws IOException {
		alphaNumChars = new AlphaNumericChars(pacman_board);
	}

	public void drawScorePanel(Graphics g) {
		int rows = char_panel.size();
		int columns = char_panel.get(0).length();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				Image piece;
				int posX = PacManBoard.dimension * j;
				int posY = PacManBoard.dimension * i;
				int index = charIndexHelper(char_panel.get(i).charAt(j));
				if (index < 0)
					piece = Toolkit.getDefaultToolkit().getImage("images/black_square.png");
				else
					piece = alphaNumChars.alphaNumImages[0][index];
				
				if (i == 1)
					posY -= PacManBoard.dimension / 4;
				
				g.drawImage(piece, posX, posY, PacManBoard.dimension,
						PacManBoard.dimension, pacman_board);
			}
		}
	}
	
	private int charIndexHelper(char character) {
		int index = character - 'A';
		if (index >= 0 && index < 26)
			index += 10;
		else
			index += 17;
		
		return index;
	}
	
	public void drawHighScore(Graphics g) {
		if (PacManBoard.topHighScore < PacManBoard.totalScore)
			PacManBoard.topHighScore = PacManBoard.totalScore;
		String score = Integer.toString(PacManBoard.topHighScore);
		int length = score.length();
		
		if (PacManBoard.topHighScore > 0) {
			for (int i = 0; i < length; i++) {
				int index = score.charAt((length-1) - i) - '0';
				Image piece = alphaNumChars.alphaNumImages[0][index];
				g.drawImage(piece, PacManBoard.dimension * 18 - PacManBoard.dimension * i, PacManBoard.dimension*2, PacManBoard.dimension,
					PacManBoard.dimension, pacman_board);
			}
		}
	}
	
	public void updateHighScores() throws FileNotFoundException, UnsupportedEncodingException {
		PacManBoard.highscores.add(PacManBoard.totalScore);
		Collections.sort(PacManBoard.highscores);
		
		
		while (PacManBoard.highscores.size() > 10) {
			PacManBoard.highscores.remove(0);
		}
		
		PrintWriter writer = new PrintWriter("high_score.txt", "UTF-8");
		for (int i = PacManBoard.highscores.size() - 1; i >= 0; i--) {
			writer.println(PacManBoard.highscores.get(i));
		}
		
		PacManBoard.topHighScore = PacManBoard.highscores.get(PacManBoard.highscores.size() - 1);
		
		while (PacManBoard.highscores.size() > 0) {
			PacManBoard.highscores.remove(0);
		}
		
		writer.close();
	}

	public void drawScore(Graphics g) {
		String score = Integer.toString(PacManBoard.totalScore);
		int length = score.length();
		
		if (PacManBoard.totalScore == 0) {
			Image zero = alphaNumChars.alphaNumImages[0][0];
			g.drawImage(zero, PacManBoard.dimension * 8, PacManBoard.dimension*2, PacManBoard.dimension,
					PacManBoard.dimension, pacman_board);
			g.drawImage(zero, PacManBoard.dimension * 8 - PacManBoard.dimension, PacManBoard.dimension*2, PacManBoard.dimension,
					PacManBoard.dimension, pacman_board);
			return;
		}		
		
		for (int i = 0; i < length; i++) {
			int index = score.charAt((length-1) - i) - '0';
			Image piece = alphaNumChars.alphaNumImages[0][index];
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
