package pacMan;

import pacMan.PacManBoard.Tyle;
import pacMan.PacManBoard.TyleType;

public class DotState {

	public enum State {
		OFF(0), BLUE_200(200), BLUE_400(400), BLUE_800(800), BLUE_1600(1600);

		int score;

		State(int score) {
			this.score = score;
		}
	}
	
	public DotState(State state, int powerups_remaining) {
		this.state = state;
	}
	
	private State state;
	public int blueTimer = 0;
	public boolean blinking;
	
	public void updateState() {
		if (state != State.OFF) {
			blueTimer++;
		}
		if (blueTimer >= 300 && blueTimer < 480) {
			state = State.OFF;
			blinking = true;
		}
		if (blueTimer == 480) {
			state = State.OFF;
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
		if (state == State.BLUE_200)
			state = State.BLUE_400;
		if (state == State.BLUE_400)
			state = State.BLUE_800;
		if (state == State.BLUE_800)
			state = State.BLUE_1600;
		if (state == State.BLUE_1600)
			state = State.OFF;
	}
	
	public State getState() {
		return state;
	}
	
	public void getPowerupCollision(Character pacman, PacManBoard.Tyle[][] tyle_board, int dimension) {
		int row = pacman.getY()/dimension;
		int column = pacman.getX()/dimension;
		
		if (tyle_board[row][column].type == TyleType.POWERUP) {
			setStateToBlue();
			// pacman.updateBlueState(isBlue, tyle_board)
			tyle_board[row][column] = Tyle.POWERUP_USED;
		}
	}

}
