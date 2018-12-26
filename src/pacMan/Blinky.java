package pacMan;

import pacMan.Ghost.State;

public class Blinky extends Ghost {

	public Blinky(int x, int y, PacManBoard.Tyle[][] tyle_board) {
		super(GhostName.BLINKY, State.DEFAULT, x, y, tyle_board, TargetingState.ATTACK);
	}
	
	public int[] getTarget(PacMan pacman) {
		int[] target = {pacman.getX(), pacman.getDeltaY()};
		return target;
	}
	
	
	public void updateAttackTarget(PacMan pacman) {
		setAttackTarget(new int[] {pacman.getX(), pacman.getDeltaY()});
	}
	
	public void updateScatterTarget() {
		
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
	}

	public void ghostStart() {
		updateDeltaX(-1);
		updateDeltaY(0);
		updateStartCount(-1);
		updateX(getDeltaX());
		updateY(getDeltaY());
	}

}
