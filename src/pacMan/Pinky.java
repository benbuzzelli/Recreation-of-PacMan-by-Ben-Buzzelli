package pacMan;

import pacMan.Ghost.HomeState;
import pacMan.TyleContainer.Tyle;

public class Pinky extends Ghost {

	public Pinky(Tyle[][] tyle_board) {
		super(GhostName.PINKY, State.DEFAULT, tyle_board, TargetingState.ATTACK, HomeState.IS_HOME, 0);
	}
	
	public void setSpawnLocation() {
		Tyle[][] tyleBoard = getTyleBoard();
		int rows = tyleBoard.length;
		int columns = tyleBoard[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (tyleBoard[i][j] == Tyle.GHOST_WALL_TOP_LEFT) {
					spawnX = j * PacManBoard.dimension + 3 * PacManBoard.dimension + PacManBoard.dimension / 2;
					spawnY = i * PacManBoard.dimension + 2 * PacManBoard.dimension;
					
					int exitX = j * PacManBoard.dimension + 3 * PacManBoard.dimension + PacManBoard.dimension / 2;
					int exitY = i * PacManBoard.dimension - PacManBoard.dimension;
					setExitX(exitX);
					setExitY(exitY);
					
					int[] target = new int[2];
					target[0] = spawnX - PacManBoard.dimension / 2;
					target[1] = spawnY;
					setHomeTarget(target);
				}
			}
		}
	}
	
	public void updateAttackTarget(PacMan pacman) {
		setAttackTarget(new int[] {pacman.getX(), pacman.getY()});
	}
	
	public void updateScatterTarget() {
		setScatterTarget(new int[] {64, -32});
	}

	public void resetGhost() {
		resetX(spawnX);
		resetY(spawnY);
		updateDeltaX(0);
		updateDeltaY(0);
		updateDensity(1);
		updateState(State.DEFAULT);
		updateStartCount(0);
		setHomeState(HomeState.IS_HOME);
		setDotCounterState(DotCounterState.INACTIVE);
		resetDotsCaptured();
	}

}
