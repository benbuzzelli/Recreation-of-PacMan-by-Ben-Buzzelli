package pacMan;

import java.io.FileNotFoundException;

public class StartGame {

	public static void main(String[] args) throws FileNotFoundException {
		PacManBoard pacman_board = new PacManBoard();
		while (true) {
			if (PacManBoard.TOTAL_DOTS == 0) {
				pacman_board.startGame();
			} else {
				for (int i = 0; i < 60; i++)
					PacManBoard.sleep();
			}
		}
	}
}
