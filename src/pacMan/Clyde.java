package pacMan;

import pacMan.TyleContainer.Tyle;


public class Clyde extends Ghost {

	public Clyde(Tyle[][] tyle_board) {
		super(GhostName.CLYDE, State.DEFAULT, tyle_board, TargetingState.ATTACK, HomeState.IS_HOME, 60);
	}
	
	public void updateAttackTarget(PacMan pacman) {
		setAttackTarget(new int[] {pacman.getX(), pacman.getY()});
	}
	
	public void updateScatterTarget() {
		setScatterTarget(new int[] {32, 544});
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
