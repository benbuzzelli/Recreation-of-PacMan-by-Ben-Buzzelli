package pacMan;

import pacMan.Ghost.HomeState;
import pacMan.TyleContainer.Tyle;

public class Pinky extends Ghost {

	public Pinky(Tyle[][] tyle_board) {
		super(GhostName.PINKY, State.DEFAULT, tyle_board, TargetingState.ATTACK, HomeState.IS_HOME, 0);
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
