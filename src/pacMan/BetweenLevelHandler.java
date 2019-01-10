package pacMan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import pacMan.TyleContainer.Tyle;

public class BetweenLevelHandler {
	
	private PacManBoard pacman_board;
	private ScoreBoard scoreBoard;
	private Image READY = Toolkit.getDefaultToolkit().getImage("images_between_levels/READY!.png");
	private Image GAME_OVER = Toolkit.getDefaultToolkit().getImage("images_between_levels/GAME_OVER.png");
	
	private boolean game_over;
	private boolean show_score_board;
	private boolean ready;
	private int readyX;
	private int readyY;
	
	public int ready_frame = 0;
	
	private Tyle[][] tyleBoard;
	
	public BetweenLevelHandler(Tyle[][] tyleBoard, PacManBoard pacman_board) throws IOException {
		this.tyleBoard = tyleBoard;
		setReadyLocation();
		this.pacman_board = pacman_board;
	}
	
	public void flashPanelAfterWin() {
		for (int i = 0; i < 120; i++) {
			if ((i / 20) % 2 == 0)
				PacManBoard.frame.setBackground(Color.WHITE);
			else
				PacManBoard.frame.setBackground(Color.BLUE);
			PacManBoard.sleep();
		}
	}
	
	public void flashReady() {
		for (int i = 0; i < 60; i++) {
			ready = false;
			PacManBoard.sleep();
		}
		for (int i = 0; i < 90; i++) {
			ready = true;
			PacManBoard.frame.repaint();
			PacManBoard.sleep();
		}
		for (int i = 0; i < 120; i++) {
			if ((i / 20) % 2 == 0)
				ready = true;
			else
				ready = false;
			PacManBoard.frame.repaint();
			PacManBoard.sleep();
		}
	}
	
	public void doGameOver(InPlayScoreBoard inPlayScoreBoard) throws IOException {
		inPlayScoreBoard.updateHighScores();
		scoreBoard = new ScoreBoard(pacman_board);
		for (int i = 0; i < 90; i++) {
			game_over = true;
			PacManBoard.frame.repaint();
			PacManBoard.sleep();
		}
		show_score_board = true;
		for (int i = 0; i < 600; i++) {
			PacManBoard.frame.repaint();
			PacManBoard.sleep();
		}
		show_score_board = false;
		game_over = false;
		PacManBoard.totalScore = 0;
		PacManBoard.lives = 0;
	}
	
	public void drawScoreBoard(Graphics g) throws IOException {
		if (show_score_board)
			scoreBoard.drawScoreBoard(g);
	} 
	
	public void drawREADY(Graphics g) {
		int width = READY.getWidth(pacman_board);
		int height = READY.getHeight(pacman_board);
		int xplus = -(width / 2 - PacManBoard.dimension) / 2;
		int yplus = -(height / 2 - PacManBoard.dimension) / 2;
		if (ready)
			g.drawImage(READY, readyX + xplus, readyY + yplus, width / 2, height / 2, pacman_board);
	} 
	
	public void drawGameOver(Graphics g) {
		int width = GAME_OVER.getWidth(pacman_board);
		int height = GAME_OVER.getHeight(pacman_board);
		int xplus = -(width / 2 - PacManBoard.dimension) / 2;
		int yplus = -(height / 2 - PacManBoard.dimension) / 2;
		if (game_over)
			g.drawImage(GAME_OVER, readyX + xplus, readyY + yplus, width / 2, height / 2, pacman_board);
	} 
	
	public void setReadyLocation() {
		int rows = tyleBoard.length;
		int columns = tyleBoard[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (tyleBoard[i][j] == Tyle.GHOST_WALL_TOP_LEFT) {
					readyX = j * PacManBoard.dimension + 3 * PacManBoard.dimension + PacManBoard.dimension / 2;
					readyY = i * PacManBoard.dimension + 5 * PacManBoard.dimension;
				}
			}
		}
	}
	
	
}
