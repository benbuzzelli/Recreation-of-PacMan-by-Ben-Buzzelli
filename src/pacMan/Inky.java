package pacMan;

import pacMan.Ghost.GhostName;
import pacMan.Ghost.State;
import pacMan.Ghost.TargetingState;
import pacMan.TyleContainer.Tyle;

public class Inky extends Ghost {

	public Inky(int x, int y, Tyle[][] tyle_board) {
		super(GhostName.INKY, State.DEFAULT, x, y, tyle_board, TargetingState.ATTACK);
	}
	
	public void updateAttackTarget(PacMan pacman) {
		setAttackTarget(new int[] {pacman.getX(), pacman.getDeltaY()});
	}
	
	public void updateScatterTarget() {
		
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
	}

	public void ghostStart() {
		if (getStartCount() < 20) {
			updateSpeed(getSpeed());
			if (getY() == 16 * getDimension() - getDimension() / 2)
				updateDeltaY(-1);
			else if (getY() == 14 * getDimension() + getDimension() / 2) {
				updateDeltaY(1);
				updateStartCount(getStartCount() + 1);
			}
		} else {
			if (getX() != 15 * getDimension() + getDimension() / 2) {
				updateDeltaX(1);
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
