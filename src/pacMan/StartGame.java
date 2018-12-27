package pacMan;

import java.io.FileNotFoundException;

public class StartGame {

	public static void main(String[] args) throws FileNotFoundException {
		PacManBoard pacman_board = new PacManBoard();
		pacman_board.startGame();
	}
}
