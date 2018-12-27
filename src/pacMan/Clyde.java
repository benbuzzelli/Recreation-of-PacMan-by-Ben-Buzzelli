package pacMan;

import pacMan.TyleContainer.Tyle;


public class Clyde extends Ghost {

	public Clyde(int x, int y, Tyle[][] tyle_board) {
		super(GhostName.CLYDE, State.DEFAULT, x, y, tyle_board, TargetingState.ATTACK, 40);
	}
	
	public void updateAttackTarget(PacMan pacman) {
		setAttackTarget(new int[] {pacman.getX(), pacman.getY()});
	}
	
	public void updateScatterTarget() {
		setScatterTarget(new int[] {32, 544});
	}
	
	public void setHomeTarget() {
		
	}

	public void resetGhost() {
		resetX(280);
		resetY(232);
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
			if (getY() == 16 * getDimension() - getDimension() / 2)
				updateDeltaY(-1);
			else if (getY() == 14 * getDimension() + getDimension() / 2) {
				updateDeltaY(1);
			}
			updateX(getDeltaX());
			updateY(getDeltaY());
			if (getNumDotsCaptured() >= getDotTriggerCount()  && !global_counter) {
				setDotCounterState(DotCounterState.INACTIVE);
				setHomeState(HomeState.IS_EXITING);
			}
		} else if (getHomeState() == HomeState.IS_EXITING) {
			ghostStartExit();
		}
	}
	
	public void ghostStartExit() {
		updateSpeed(1);
		if (getX() != 15 * getDimension() + getDimension() / 2) {
			updateDeltaX(-1);
			updateDeltaY(0);
		} else if (getY() != 12 * getDimension()) {
			updateDeltaX(0);
			updateDeltaY(-1);
		} else {
			updateDeltaX(-1);
			updateDeltaY(0);
			setHomeState(HomeState.HAS_EXITED);
			updateSpeed(2);
			return;
		}
		updateX(getDeltaX());
		updateY(getDeltaY());
	}

}
