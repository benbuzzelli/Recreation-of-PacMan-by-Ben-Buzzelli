package pacMan;

import java.util.TimerTask;

/**
 * 12/27/18
 * @author jakob
 * Custom TimerTask class as a helper for PacMan.java to run in cast PacMan stops collecting dots
 */
public class DotTimer extends TimerTask{
	private Ghost[] ghosts;
	private PacMan pacman;
	
	public DotTimer(Ghost[] ghosts, PacMan pacman){
		this.pacman = pacman;
		this.ghosts = ghosts;
	}
	
	public void run(){
		for(int i =0; i < ghosts.length;i++){
			if(ghosts[i].getHomeState() == Ghost.HomeState.IS_HOME){
				ghosts[i].setHomeState(Ghost.HomeState.IS_EXITING);
				ghosts[i].ghostStartExit();
				pacman.updateTimer(new DotTimer(ghosts, pacman));
				break;
			}
		}		
}
	
}