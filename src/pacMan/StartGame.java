package pacMan;

import java.io.FileNotFoundException;
import java.io.IOException;

public class StartGame {

	public static void main(String[] args) throws IOException {
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
