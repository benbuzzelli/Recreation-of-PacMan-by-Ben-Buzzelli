package pacMan;

import java.util.Timer;
import java.util.TimerTask;

import pacMan.Ghost.TargetingState;
import pacMan.PowerUp.State;

public class GhostStateHandler{

	private Ghost[] ghosts;	
	private boolean attacking, scattering;
	
	private long temp_time = 0;
	private double time_passed = 0;
	
	public GhostStateHandler(Ghost[] ghosts) {
		this.ghosts = ghosts;
	}
	
	public void switchTargetState(long total_time, PowerUp powerup) {
		if (powerup.getState() == State.OFF) {
			time_passed = (total_time - temp_time) / 1000.0;		
			for (int i = 0; i < 4; i++) {
				Ghost ghost = ghosts[i];
				if (ghost.getTargetingState() != TargetingState.GO_HOME 
						&& ghost.getTargetingState() != TargetingState.FRIGHTENED) {
					if (time_passed >= 7 && !attacking) {
						temp_time = total_time;
						ghost.setTargetingState(TargetingState.ATTACK);
						ghost.setBackTracking(true);
						if (i == 3) {
							attacking = true;
							scattering = false;
						}
					} else if (time_passed >= 20 && !scattering) {
						temp_time = total_time;
						ghost.setTargetingState(TargetingState.SCATTER);
						ghost.setBackTracking(true);
						if (i == 3) {
							attacking = false;
							scattering = true;
						}						
					}
				}
			}
		} else {
			temp_time = (long) (total_time - time_passed * 1000);
		}
	}
	
}
