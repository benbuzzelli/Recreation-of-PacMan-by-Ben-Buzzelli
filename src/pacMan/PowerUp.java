package pacMan;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pacMan.Ghost.GhostName;
import pacMan.Ghost.TargetingState;
import pacMan.Ghost.Visibility;
import pacMan.TyleContainer.Tyle;
import pacMan.TyleContainer.TyleType;

public class PowerUp {

	public enum State {
		OFF(0, null), BLUE_200(200, "images_score_panel/200.png"), BLUE_400(400, "images_score_panel/400.png"),
		BLUE_800(800, "images_score_panel/800.png"), BLUE_1600(1600, "images_score_panel/1600.png");

		int score;
		String filename;
		Image image;

		State(int score, String filename) {
			this.score = score;
			this.image = Toolkit.getDefaultToolkit().getImage(filename);
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

	public void powerupHandler(PacMan pacman) throws IOException {
		updateState();
		incrementState();
		getPowerupCollision();
		getPacManCollision();
	}

	public void updateState() {
		if (state != State.OFF) {
			if (blueTimer == 0) {
				setGhostStates(Ghost.State.BLUE);
				setGhostTargetingStates(TargetingState.FRIGHTENED);
			}
			blueTimer++;
		}
		if (blueTimer == 300) {
			setGhostStates(Ghost.State.BLINKING);
			setGhostTargetingStates(TargetingState.FRIGHTENED);
			blinking = true;
		}
		if (blueTimer == 480) {
			setGhostStates(Ghost.State.DEFAULT);
			setGhostTargetingStates(TargetingState.ATTACK);
			state = State.OFF;
			pacman.state = pacman.state.DEFAULT;
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
		if (ghosts_remaining == 0) {
			ghosts_remaining = 4;
			state = State.OFF;
		}
	}

	public State getState() {
		return state;
	}

	public void getPowerupCollision() {
		int row = pacman.getY() / PacManBoard.dimension;
		int column = pacman.getX() / PacManBoard.dimension;

		if (tyle_board[row][column].type == TyleType.POWERUP) {
			setStateToBlue();
			PacManBoard.TOTAL_DOTS--;
			PacManBoard.totalScore += 10;
			System.out.println("<-----------Powerup collision detected------------>");
			pacman.state = PacMan.State.POWERED;
			tyle_board[row][column] = Tyle.POWERUP_USED;
		}
	}

	public void getPacManCollision() throws IOException {
		for (int i = 0; i < 4; i++) {
			Ghost ghost = ghosts[i];
			int gRow = ghost.getY() / PacManBoard.dimension;
			int gCol = ghost.getX() / PacManBoard.dimension;
			int pRow = pacman.getY() / PacManBoard.dimension;
			int pCol = pacman.getX() / PacManBoard.dimension;
			if (gRow == pRow && gCol == pCol && ghost.getState() != Ghost.State.DEFAULT
					&& ghost.getState() != Ghost.State.HEAD_HOME) {

				PacManBoard.totalScore += state.score;
				Audio audio = new Audio();
				audio.munchSound();
				decrementGhosts();
				ghost.setTargetingState(TargetingState.GO_HOME);
				ghost.updateState(Ghost.State.HEAD_HOME);
				ghost.updateDensity(0);

				setBlackTemporarily(ghost, 1);
			}
		}
	}

	private void setBlackTemporarily(Ghost ghost, int seconds) {
		pacman.changeVisibility(PacMan.Visibility.NOT_VISIBLE);
		Image gImage = ghost.getImage();
		ghost.changeImage(null, state.image);
		PacManBoard.frame.repaint();
		for (int j = 0; j < seconds * 60; j++) {
			PacManBoard.sleep();
		}
		ghost.changeImage(null, gImage);
		pacman.changeVisibility(PacMan.Visibility.VISIBLE);
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
