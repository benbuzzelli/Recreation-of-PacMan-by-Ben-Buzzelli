package pacMan;

import pacMan.Ghost.HomeState;
import pacMan.TyleContainer.Tyle;

public class Inky extends Ghost {
	
	public Inky(Tyle[][] tyle_board) {
		super(GhostName.INKY, State.DEFAULT, tyle_board, TargetingState.ATTACK, HomeState.IS_HOME, 30);
	}
	
	public void updateAttackTarget(PacMan pacman) {
		setAttackTarget(new int[] {pacman.getX(), pacman.getY()});
	}
	
	public void updateScatterTarget() {
		setScatterTarget(new int[] {464, 544});
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
