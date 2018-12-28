package pacMan;

import pacMan.Ghost.HomeState;
import pacMan.TyleContainer.Tyle;

public class Blinky extends Ghost {

	public Blinky(Tyle[][] tyle_board) {
		super(GhostName.BLINKY, State.DEFAULT, tyle_board, TargetingState.ATTACK, HomeState.IS_EXITING, 0);
	}
	
	public void updateAttackTarget(PacMan pacman) {
		setAttackTarget(new int[] {pacman.getX(), pacman.getY()});
	}
	
	public void updateScatterTarget() {
		setScatterTarget(new int[] {432, -32});
	}
	
	public void setSpawnLocation() {
		Tyle[][] tyleBoard = getTyleBoard();
		int rows = tyleBoard.length;
		int columns = tyleBoard[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (tyleBoard[i][j] == Tyle.GHOST_WALL_TOP_LEFT) {
					spawnX = j * PacManBoard.dimension + 3 * PacManBoard.dimension + PacManBoard.dimension / 2;
					spawnY = i * PacManBoard.dimension - PacManBoard.dimension;
					setExitX(spawnX);
					setExitY(spawnY);
					int[] target = new int[2];
					target[0] = j * PacManBoard.dimension + 3 * PacManBoard.dimension;
					target[1] = i * PacManBoard.dimension + 2 * PacManBoard.dimension;
					setHomeTarget(target);
				}
			}
		}
	}

	public void resetGhost() {
		resetX(spawnX);
		resetY(spawnY);
		updateDeltaX(0);
		updateDeltaY(0);
		updateDensity(1);
		updateState(State.DEFAULT);
		updateStartCount(0);
		setHomeState(HomeState.IS_EXITING);
		setDotCounterState(DotCounterState.INACTIVE);
		resetDotsCaptured();
	}

}
