package pacMan;

import pacMan.TyleContainer.Tyle;

public class Blinky extends Ghost {

	public Blinky(Tyle[][] tyle_board) {
		super(GhostName.BLINKY, State.DEFAULT, tyle_board, TargetingState.ATTACK, 0);
	}
	
	public int[] getTarget(PacMan pacman) {
		int[] target = {pacman.getX(), pacman.getDeltaY()};
		return target;
	}
	
	
	public void updateAttackTarget(PacMan pacman) {
		setAttackTarget(new int[] {pacman.getX(), pacman.getY()});
	}
	
	public void updateScatterTarget() {
		setScatterTarget(new int[] {432, -32});
	}
	
	public void setHomeTarget() {
		
	}
	

	public void resetGhost() {
		resetX(248);
		resetY(192);
		updateDeltaX(0);
		updateDeltaY(0);
		updateDensity(1);
		updateState(State.DEFAULT);
		updateStartCount(0);
		setHomeState(HomeState.IS_HOME);
		setDotCounterState(DotCounterState.INACTIVE);
		resetDotsCaptured();
	}
	
	public void ghostStart(boolean global_counter) {
		if (getHomeState() == HomeState.IS_HOME) {
			updateSpeed(getSpeed());
			updateDeltaX(-1);
			updateDeltaY(0);
			updateX(getDeltaX());
			updateY(getDeltaY());
			setDotCounterState(DotCounterState.INACTIVE);
			setHomeState(HomeState.HAS_EXITED);
		}
	}
	
	public void ghostStartExit() {
		
	}

}
