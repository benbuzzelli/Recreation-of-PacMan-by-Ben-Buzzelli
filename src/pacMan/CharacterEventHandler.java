package pacMan;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;

import pacMan.Ghost.DotCounterState;
import pacMan.Ghost.HomeState;
import pacMan.Ghost.TargetingState;
import pacMan.Ghost.Visibility;
import pacMan.TyleContainer.Tyle;
import pacMan.TyleContainer.TyleType;


public class CharacterEventHandler {

	// *********************************************************************************//
	// VARIABLE DECLARATION
	// *********************************************************************************//
	private int cycle_frame = 0;
	private final int frames_per_cycle;
	
	
	private long time_at_start;
	private long time_at_end;
	
	private long total_time = 0;

	private PacMan pacman;
	private Ghost[] ghosts;
	
	private PowerUp power_up; // This class keeps track of things to do with power-ups
	
	private int global_dots_captured = 0;
	private boolean global_dot_counter;
	
	private int pacman_frames_passed = 0;
	private int curPacMan_speed_percent = 0;
	
	private int[] ghost_frames_passed = {0, 0, 0, 0};
	private int[] curGhost_speed_percent = {0, 0, 0, 0};
	
	private DotTimer dotTimer;
	private GhostStateHandler ghostStateHandler;
		
	private int[] globat_dot_limit = {0, 7, 17, 32};
	
	private Tyle[][] tyle_board;
	// *********************************************************************************//
	// *********************************************************************************//
	// FUNCTIONS TO BE CALLED BEFORE postKeyPressEventHandler()
	// *********************************************************************************//
	public CharacterEventHandler(int frames_per_cycle, PacMan pacman, Ghost[] ghosts, Tyle[][] tyle_board) {
		this.frames_per_cycle = frames_per_cycle;
		this.pacman = pacman;
		this.ghosts = ghosts;
		this.dotTimer = new DotTimer(ghosts);
		this.ghostStateHandler = new GhostStateHandler(ghosts);
		dotTimer.updateTimer();
		this.power_up = new PowerUp(pacman, ghosts, PowerUp.State.OFF, tyle_board);
		this.tyle_board = tyle_board;
	}

	public void setCharacters() throws IOException {
		for (int i = 0; i < 4; i++) {
			Ghost ghost = ghosts[i];
			ghost.setImage();
			ghost.updateImage();
		}
		pacman.updateImage();
	}
	// *********************************************************************************//
	// *********************************************************************************//
	
	/**
	 * Run in the startGame() in a continuous while-loop. Works by updating image of pacMan based on his state and updating each of the ghosts individually
	 * @param delta Array of length 2, with x and y deltas for PacMan to move. PacMan will move by x=speed*delta[0] and by y=speed*delta[1]
	 * @throws IOException 
	 */
	public void postKeyPressEventHandler(int[] delta) throws IOException {
		time_at_start = (long) new Date().getTime();
		
		pacman_frames_passed++;
		pacmanHandler();
		// ghostStateHandler.switchTargetState();
		
		power_up.blinkPowerUps(cycle_frame, 10);
		power_up.powerupHandler(pacman);
		
		if (power_up.getState() != PowerUp.State.OFF) {
			// ghostStateHandler.ghostStateTimer.timer.
		}
		
		if (!pacman.isStalled(pacman_frames_passed)) {
			pacman.update(delta[0], delta[1], tyle_board); //Move pacman by dx and dy along tyle_board
		} else {
			pacman.changeFramesStalled(pacman.getFramesStalled() + 1);
		}
		
		if (pacman.getDeltaX() != 0 || pacman.getDeltaY() != 0) {		
			ghostHandler();
			cycleHandler();
		}
		
		PacManBoard.sleep();
		
		time_at_end = (long) new Date().getTime();
		double time_diff = time_at_end - time_at_start;
		if (time_diff > 17)
			time_diff = 16.666;
		total_time += (time_diff);
		
		ghostStateHandler.switchTargetState(total_time, power_up);
	}
	
	// *********************************************************************************//
	// *********************************************************************************//
	// HELPER FUNCTIONS FOR postKeyPressEventHandler()
	// *********************************************************************************//
	// Increment cycle_frame and reset it to 0 if it is equal to frames_per_cycle.
	public void cycleHandler() {
		cycle_frame++;
		if (cycle_frame == frames_per_cycle)
			cycle_frame = 0;
	}
	
	public void setGhostData(Ghost ghost) {
		ghost.setSpeed();
		ghost.setImage();
		ghost.updateImage();
	}
	
	private void pacmanHandler() throws IOException {
		pacman.updateImage();
		if (pacman.getSpeedPercent() != curPacMan_speed_percent) {
			pacman_frames_passed = 0;
			pacman.changeFramesStalled(0);
			curPacMan_speed_percent = pacman.getSpeedPercent();
		}
	}
	
	private void ghostHandler() throws IOException {
		if (!global_dot_counter) {
			incrementGhostDotCount();
		}
		else {
			globalCounterHandler();
		}
		

		if (pacman.updateDots(tyle_board)) {
			PacManBoard.totalScore += 10;
			dotTimer.cancelTimer();
			PacManBoard.TOTAL_DOTS--;
		}
		dotTimer.restartTimer();
		
		for (int i = 0; i < 4; i++) {
			Ghost ghost = ghosts[i];
			setGhostData(ghost);
			if (ghost.getSpeedPercent() != curGhost_speed_percent[i]) {
				ghost_frames_passed[i] = 0;
				ghost.changeFramesStalled(0);
				curGhost_speed_percent[i] = ghost.getSpeedPercent();
			}
			curGhost_speed_percent[i] = ghost.getSpeedPercent();
			ghost_frames_passed[i]++;
			doCollisionEvents(ghost);
			
			if (!ghost.isStalled(ghost_frames_passed[i])) {
				ghost.ghostStart(global_dot_counter);
				setDotCounterStates(ghosts);
				if (ghost.getHomeState() == HomeState.HAS_EXITED) {
					ghost.makeMove(pacman);
				}
			} else {
				ghost.changeFramesStalled(ghost.getFramesStalled() + 1);
			}
				
			doCollisionEvents(ghost);
		}
		
		
	}
	
	private void doCollisionEvents(Ghost ghost) throws IOException {
		if (ghost.checkCollision(power_up, pacman)) {
			global_dot_counter = true;
			global_dots_captured = 0;
			PacManBoard.lives--;
			for (int i = 0; i < 30; i++) {
				PacManBoard.sleep();
			}
			
			for (int i = 0; i < 4; i++) {
				ghosts[i].changeVisibility(Visibility.NOT_VISIBLE);
			}
			pacman.setState(PacMan.State.DEAD);
			Audio audio = new Audio();
			audio.dieSound();
			for (int i = 0; i < 33; i++) {
				pacman.updateImage();
				PacManBoard.frame.repaint();
				PacManBoard.sleep();
			}
			pacman.setState(PacMan.State.DEFAULT);
			pacman.changeVisibility(PacMan.Visibility.NOT_VISIBLE);
			for (int i = 0; i < 50; i++) {
				pacman.updateImage();
				PacManBoard.frame.repaint();
				PacManBoard.sleep();
			}
			
			for (int i = 0; i < 4; i++) {
				ghosts[i].changeVisibility(Visibility.VISIBLE);
			}
			pacman.changeVisibility(PacMan.Visibility.VISIBLE);
			pacman.setState(PacMan.State.DEFAULT);
			pacman.resetPacMan();
			PacManBoard.frame.repaint();
			
			dotTimer.updateTimer();
			
			for (int j = 0; j < 4; j++) {
				ghosts[j].resetGhost();
			}
			handleStart();
		}
	}
	
	public void incrementGhostDotCount() {
		for (int i = 0; i < ghosts.length; i++) {
			Ghost ghost = ghosts[i];
			ghost.incrementDotsCaptured(pacman);
		}
	}
	
	public void globalCounterHandler() {
		if (tyle_board[pacman.getY() / PacManBoard.dimension][pacman.getX() / PacManBoard.dimension].type == TyleType.DOT)
			global_dots_captured++;
		if (global_dots_captured >= globat_dot_limit[1] && ghosts[1].getHomeState() == HomeState.IS_HOME) {
			ghosts[1].setHomeState(HomeState.IS_EXITING);
			global_dots_captured = 0;
		} else if (global_dots_captured >= globat_dot_limit[2] && ghosts[2].getHomeState() == HomeState.IS_HOME) {
			ghosts[2].setHomeState(HomeState.IS_EXITING);
			global_dots_captured = 0;
		} else if (global_dots_captured >= globat_dot_limit[3] && ghosts[3].getHomeState() == HomeState.IS_HOME) {
			ghosts[3].setHomeState(HomeState.IS_EXITING);
			global_dots_captured = 0;
		}
	}
	
	public void setDotCounterStates(Ghost[] ghosts) {
		for (int i = 0; i < ghosts.length - 1; i++) {
			if (ghosts[i].getHomeState() == HomeState.HAS_EXITED) {
				if (ghosts[i + 1].getHomeState() == HomeState.IS_HOME) {
					ghosts[i + 1].setDotCounterState(DotCounterState.ACTIVE);
				}
			}
		}
	}

	public void handleStart() throws IOException {
		pacman.updateImage();
		while (pacman.getStartCount() != -1) {
			pacman.pacmanStart();
			PacManBoard.sleep();
		}
	}
	
}
