package pacMan;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 12/27/18
 * @author jakob
 * Custom TimerTask class as a helper for PacMan.java to run in cast PacMan stops collecting dots
 */
public class DotTimer {
	
	private Timer timer = new Timer();
	private boolean timer_is_active;
	private Ghost[] ghosts;
	
	public DotTimer(Ghost[] ghosts) {
		this.ghosts = ghosts;
	}
	
	public void updateTimer() {
		if (timer_is_active) {
			timer.cancel();
			timer_is_active = false;
		} else {
			timer_is_active = true;
			timer = new Timer();
			timer.schedule(new FreeGhost(), 4000);
		}
	}
	
	public void cancelTimer() {
		if (timer_is_active) {
			timer.cancel();
			timer_is_active = false;
		}
	}
	
	public void restartTimer() {
		if (!timer_is_active) {
			timer_is_active = true;
			timer = new Timer();
			timer.schedule(new FreeGhost(), 4000);
		}
	}
	
	class FreeGhost extends TimerTask {
		
		public void run() {
			for(int i =0; i < ghosts.length;i++){
				if(ghosts[i].getHomeState() == Ghost.HomeState.IS_HOME){
					ghosts[i].setHomeState(Ghost.HomeState.IS_EXITING);
					ghosts[i].ghostStartExit();
					updateTimer();
					break;
				}
			}
		}
	}
}