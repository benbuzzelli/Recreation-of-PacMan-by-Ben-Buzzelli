package pacMan;

import java.util.ArrayList;
import java.util.List;

import pacMan.TyleContainer.Tyle;
import pacMan.TyleContainer.TyleType;

public class DotState {

	public enum State {
		OFF(0), BLUE_200(200), BLUE_400(400), BLUE_800(800), BLUE_1600(1600);

		int score;

		State(int score) {
			this.score = score;
		}
	}
	
	public DotState(PacMan pacman, State state, Tyle[][] tyle_board) {
		this.state = state;
		this.tyle_board = tyle_board;
		this.pacman = pacman;
		getPowerUpLocations();
	}
	
	private State state;
	private PacMan pacman;
	private int ghosts_remaining = 4;
	public int blueTimer = 0;
	public boolean blinking;
	private List<int[]> powerup_pos = new ArrayList<int[]>();
	
	private Tyle[][] tyle_board;
	
	public void powerupHandler(PacMan pacman) {
		updateState();
		incrementState();
		getPowerupCollision();
	}
	
	public void updateState() {
		if (state != State.OFF) {
			blueTimer++;
		}
		if (blueTimer >= 300 && blueTimer < 480) {
			blinking = true;		
		}
		if (blueTimer == 480) {
			state = State.OFF;
			resetGhosts();
			blueTimer = 0;
			blinking = false;
		}
	}

	public void setStateToBlue() {
		state = State.BLUE_200;
		blueTimer = 0;
		blinking = false;
	}
	
	public void incrementState() {
		if (ghosts_remaining == 3)
			state = State.BLUE_400;
		if (ghosts_remaining == 2)
			state = State.BLUE_800;
		if (ghosts_remaining == 1)
			state = State.BLUE_1600;
		if (ghosts_remaining == 0)
			state = State.OFF;
	}
	
	public State getState() {
		return state;
	}
	
	public void getPowerupCollision() {
		int row = pacman.getY()/PacManBoard.dimension;
		int column = pacman.getX()/PacManBoard.dimension;
		
		if (tyle_board[row][column].type == TyleType.POWERUP) {
			setStateToBlue();
			pacman.state = PacMan.State.POWERED;
			tyle_board[row][column] = Tyle.POWERUP_USED;
		}
	}
	
	public void getPowerUpLocations() {
		int boardRows = tyle_board.length;
		int boardColumns = tyle_board[0].length;
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardColumns; j++) {
				if (tyle_board[i][j] == Tyle.POWERUP) {
					int[] position = new int[2];
					position[0] = i;
					position[1] = j;
					powerup_pos.add(position);
				}
			}
		}
	}

	public void blinkPowerUps(int frame, int rate) {
		if (frame % rate == 0) {
			for (int i = 0; i < powerup_pos.size(); i++) {
				if (tyle_board[powerup_pos.get(i)[0]][powerup_pos.get(i)[1]] == Tyle.POWERUP) {
					tyle_board[powerup_pos.get(i)[0]][powerup_pos.get(i)[1]] = Tyle.POWERUP_BLINKED;
				} else if (tyle_board[powerup_pos.get(i)[0]][powerup_pos.get(i)[1]] == Tyle.POWERUP_BLINKED) {
					tyle_board[powerup_pos.get(i)[0]][powerup_pos.get(i)[1]] = Tyle.POWERUP;
				}
			}
		}
	}
	
	public void decrementGhosts() {
		ghosts_remaining--;
	}
	
	public void resetGhosts() {
		ghosts_remaining = 4;
	}

}
