package pacMan;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class PlayGame extends JPanel  {
	
	private JFrame frame = new JFrame();
	
	/*
	public void paint(Graphics g) {
		char[][] cBoard = charBoard(board.size(), board.get(0).length());
		int rows = cBoard.length;
		int columns = cBoard[0].length;
		for (int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				String image_name = Character.toString(cBoard[i][j]);
				image_name += ".png";
				Image piece = Toolkit.getDefaultToolkit().getImage(image_name);
				g.drawImage(piece, 50 + 16*j, 50 + 16*i, 16, 16, this);
			}
		}
	}*/
	
	private void setFrame() {
		frame.setSize(1000, 1000);
		frame.getContentPane().add(this);
		frame.setLocationRelativeTo(null);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	public void createBoard() throws FileNotFoundException  {
		PacManBoard pacman_board = new PacManBoard();
		pacman_board.getBoard("textBoard.txt");
		setFrame();
	}
/*
	public static void main(String[] args) throws FileNotFoundException  {
		PlayGame pacman_game = new PlayGame();
		pacman_game.createBoard();
	}*/
	
}
