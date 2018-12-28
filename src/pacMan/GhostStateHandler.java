package pacMan;

import java.util.Timer;
import java.util.TimerTask;

import pacMan.Ghost.TargetingState;

public class GhostStateHandler{

	private Ghost[] ghosts;
	public GhostStateTimer ghostStateTimer; 
	
	public GhostStateHandler(Ghost[] ghosts) {
		this.ghosts = ghosts;
		this.ghostStateTimer = new GhostStateTimer();
	}
	
	public void switchTargetState() {
		for (int i = 0; i < 4; i++) {
			Ghost ghost = ghosts[i];
			if (ghost.getTargetingState() != TargetingState.GO_HOME 
					&& ghost.getTargetingState() != TargetingState.FRIGHTENED) {
				ghost.setTargetingState(ghostStateTimer.getTargetState());
			}
		}
	}
	
}
