package pacMan;

import java.io.FileNotFoundException;

public class StartGame {

	public static void main(String[] args) throws FileNotFoundException {
		PacManBoard pacman_board = new PacManBoard();
		pacman_board.gameStartUp();
		while (true) {
			pacman_board.startGame();
			pacman_board.resetGame();
			for (int i = 0; i < 60; i++)
				PacManBoard.sleep();
		}
	}
}
