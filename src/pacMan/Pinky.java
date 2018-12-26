package pacMan;

import pacMan.Ghost.State;
import pacMan.Ghost.TargetingState;
import pacMan.TyleContainer.Tyle;

public class Pinky extends Ghost {

	public Pinky(int x, int y, Tyle[][] tyle_board) {
		super(GhostName.PINKY, State.DEFAULT, x, y, /*16, -16,*/ tyle_board, TargetingState.ATTACK);
	}
	
	public int[] getTarget(PacMan pacman) {
		int[] target = new int[2];
		return target;
	}

	public void resetGhost() {
		resetX(248);
		resetY(248);
		updateDeltaX(0);
		updateDeltaY(0);
		updateDensity(1);
		updateState(State.DEFAULT);
		updateStartCount(0);
	}

	public void ghostStart() {
		if (getStartCount() < 10) {
			updateSpeed(getSpeed());
			if (getY() == 16 * getDimension() - getDimension() / 2)
				updateDeltaY(-1);
			else if (getY() == 14 * getDimension() + getDimension() / 2) {
				updateDeltaY(1);
				updateStartCount(getStartCount() + 1);
			}
		} else {
			if (getY() != 12 * getDimension()) {
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
