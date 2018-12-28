package pacMan;

import java.io.FileNotFoundException;

public class StartGame {

	public static void main(String[] args) throws FileNotFoundException {
		while (true) {
			PacManBoard pacman_board = new PacManBoard();
			if (PacManBoard.TOTAL_DOTS == 0) {
				pacman_board.startGame();
			}
		}
	}
}
