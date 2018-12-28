package pacMan;

import java.util.ArrayList;
import java.util.List;

import pacMan.Ghost.GhostName;
import pacMan.Ghost.TargetingState;
import pacMan.TyleContainer.Tyle;
import pacMan.TyleContainer.TyleType;

public class PowerUp {

	public enum State {
		OFF(0), BLUE_200(200), BLUE_400(400), BLUE_800(800), BLUE_1600(1600);

		int score;

		State(int score) {
			this.score = score;
		}
	}
	
	private State state;
	private Ghost[] ghosts;
	private PacMan pacman;
	private int ghosts_remaining = 4;
	public int blueTimer = 0;
	public boolean blinking;
	private List<int[]> powerup_pos = new ArrayList<int[]>();
	
	private Tyle[][] tyle_board;
	
	public PowerUp(PacMan pacman, Ghost[] ghosts, State state, Tyle[][] tyle_board) {
		this.state = state;
		this.tyle_board = tyle_board;
		this.pacman = pacman;
		this.ghosts = ghosts;
		getPowerUpLocations();
	}
	
	public void powerupHandler(PacMan pacman) {
		updateState();
		incrementState();
		getPowerupCollision();
		getPacManCollision();
	}
	
	public void updateState() {
		if (state != State.OFF) {
			if (blueTimer == 0) {
				setGhostStates(Ghost.State.BLUE);
				setGhostTargetingStates(TargetingState.SCATTER);
			}
			blueTimer++;
		}
		if (blueTimer == 300) {
			setGhostStates(Ghost.State.BLINKING);
			setGhostTargetingStates(TargetingState.SCATTER);
			blinking = true;		
		}
		if (blueTimer == 480 || blueTimer == 0) {
			setGhostStates(Ghost.State.DEFAULT);
			state = State.OFF;
			resetGhostCount();
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
			System.out.println("<-----------Powerup collision detected------------>");
			pacman.state = PacMan.State.POWERED;
			tyle_board[row][column] = Tyle.POWERUP_USED;
		}
	}
	
	public void getPacManCollision() {
		for (int i = 0; i < 4; i++) {
			Ghost ghost = ghosts[i];
			int gRow = ghost.getY() / PacManBoard.dimension;
			int gCol = ghost.getX() / PacManBoard.dimension;
			int pRow = pacman.getY() / PacManBoard.dimension;
			int pCol = pacman.getX() / PacManBoard.dimension;
			if (gRow == pRow && gCol == pCol && ghost.getState() != Ghost.State.DEFAULT && ghost.getState() != Ghost.State.HEAD_HOME) {
				ghost.setTargetingState(TargetingState.GO_HOME);
				ghost.updateState(Ghost.State.HEAD_HOME);
				ghost.updateDensity(0);
				ghost.setBackTracking(true);
			}
		}
	}
	
	private void setGhostTargetingStates(TargetingState targeting_state) {
		for (int i = 0; i < 4; i++) {
			Ghost ghost = ghosts[i];
			if (ghost.getTargetingState() == TargetingState.GO_HOME)
				continue;
			ghost.setTargetingState(targeting_state);
		}
	}
	
	private void setGhostStates(Ghost.State state) {
		for (int i = 0; i < 4; i++) {
			Ghost ghost = ghosts[i];
			if (ghost.getState() != Ghost.State.BLUE && state == Ghost.State.BLINKING) {
				continue;
			}
			if (ghost.getState() == Ghost.State.HEAD_HOME)
				continue;
			ghost.setState(state);
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
	
	public void resetGhostCount() {
		ghosts_remaining = 4;
	}

}
