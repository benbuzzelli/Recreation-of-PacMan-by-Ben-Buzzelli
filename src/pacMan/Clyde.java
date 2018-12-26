package pacMan;

import pacMan.Ghost.GhostName;
import pacMan.Ghost.State;
import pacMan.Ghost.TargetingState;

public class Clyde extends Ghost {

	public Clyde(int x, int y, PacManBoard.Tyle[][] tyle_board) {
		super(GhostName.CLYDE, State.DEFAULT, x, y, tyle_board, TargetingState.ATTACK);
	}
	
	public int[] getTarget(PacMan pacman) {
		int[] target = new int[2];
		
		target = getTargetHelper(pacman.getDeltaX(), pacman.getDeltaY());
		
		return target;
	}
	
	public int[] getTargetHelper(int dx, int dy) {
		int row = dy / PacManBoard.dimension;
		int column = dx / PacManBoard.dimension;
		int[] temp_delta = {column, row};
		
		PacManBoard.Tyle[][] board = getTyleBoard();
		
		while (true) {
			
			row += dy;
			column += dx;
			
			if (board[row][column].type != PacManBoard.TyleType.WALL || board[row][column].type != PacManBoard.TyleType.UNREACHABLE)
				return temp_delta;
			if (board[row][column].type != PacManBoard.TyleType.GHOSTGATE)
				return temp_delta;
			
			temp_delta[0] = column;
			temp_delta[1] = row;
			
		}
		
	}

	public void resetGhost() {
		resetX(280);
		resetY(232);
		updateDeltaX(0);
		updateDeltaY(0);
		updateDensity(1);
		updateState(State.DEFAULT);
		updateStartCount(0);
	}

	public void ghostStart() {
		if (getStartCount() < 30) {
			updateSpeed(getSpeed());
			if (getY() == 16 * getDimension() - getDimension() / 2)
				updateDeltaY(-1);
			else if (getY() == 14 * getDimension() + getDimension() / 2) {
				updateDeltaY(1);
				updateStartCount(getStartCount() + 1);
			}
		} else {
			if (getX() != 15 * getDimension() + getDimension() / 2) {
				updateDeltaX(-1);
				updateDeltaY(0);
			} else if (getY() != 12 * getDimension()) {
				updateDeltaX(0);
				updateDeltaY(-1);
			} else {
				updateDeltaX(-1);
				updateDeltaY(0);
				updateStartCount(-1);
				return;
			}
		}
		updateX(getDeltaX());
		updateY(getDeltaY());
	}

}
