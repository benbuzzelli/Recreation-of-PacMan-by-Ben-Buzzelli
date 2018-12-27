package pacMan;

import pacMan.TyleContainer.Tyle;

public class Inky extends Ghost {

	public Inky(Tyle[][] tyle_board) {
		super(GhostName.INKY, State.DEFAULT, tyle_board, TargetingState.ATTACK, 30);
	}
	
	public void updateAttackTarget(PacMan pacman) {
		setAttackTarget(new int[] {pacman.getX(), pacman.getY()});
	}
	
	public void updateScatterTarget() {
		setScatterTarget(new int[] {464, 544});
	}
	
	public void setHomeTarget() {
		
	}

	public void resetGhost() {
		resetX(216);
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
			if (getDeltaY() == 0)
				updateDeltaY(1);
			if (getY() == 16 * PacManBoard.dimension - PacManBoard.dimension / 2)
				updateDeltaY(-1);
			else if (getY() == 14 * PacManBoard.dimension + PacManBoard.dimension / 2) {
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
		if (getX() != 15 * PacManBoard.dimension + PacManBoard.dimension / 2) {
			updateDeltaX(1);
			updateDeltaY(0);
		} else if (getY() != 12 * PacManBoard.dimension) {
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
