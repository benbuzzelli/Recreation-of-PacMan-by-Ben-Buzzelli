package pacMan;

import java.util.Timer;
import java.util.TimerTask;

public class GhostStateHandler{

	private Ghost[] ghosts;
	public GhostStateTimer ghostStateTimer; 
	
	public GhostStateHandler(Ghost[] ghosts) {
		this.ghosts = ghosts;
		this.ghostStateTimer = new GhostStateTimer();
		//comment
	}
	
	public void switchTargetState() {
		for (int i = 0; i < 4; i++) {
			Ghost ghost = ghosts[i];
			ghost.setTargetingState(ghostStateTimer.getTargetState());
		}
	}
	
}
