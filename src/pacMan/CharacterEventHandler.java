package pacMan;
import java.util.Timer;

import pacMan.Ghost.DotCounterState;
import pacMan.Ghost.HomeState;
import pacMan.Ghost.TargetingState;
import pacMan.TyleContainer.Tyle;
import pacMan.TyleContainer.TyleType;


public class CharacterEventHandler {

	// *********************************************************************************//
	// VARIABLE DECLARATION
	// *********************************************************************************//
	private int cycle_frame = 0;
	private final int frames_per_cycle;

	private PacMan pacman;
	private Ghost[] ghosts;
	
	private PowerUp power_up; // This class keeps track of things to do with power-ups
	
	private int global_dots_captured = 0;
	private boolean global_dot_counter;
	
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
		ghostStateHandler.ghostStateTimer.scheduleAttack();
		this.power_up = new PowerUp(pacman, ghosts, PowerUp.State.OFF, tyle_board);
		this.tyle_board = tyle_board;
	}

	public void setCharacters() {
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
	 */
	public void postKeyPressEventHandler(int[] delta) {
			
		pacmanHandler();
		ghostStateHandler.switchTargetState();
		
		power_up.blinkPowerUps(cycle_frame, 10);
		power_up.powerupHandler(pacman);
		
		if (isNotStalled(pacman.getSpeedPercent())) {
			pacman.update(delta[0], delta[1], tyle_board); //Move pacman by dx and dy along tyle_board
		}
		
		if (pacman.getDeltaX() != 0 || pacman.getDeltaY() != 0) {		
			ghostHandler();
			cycleHandler();
		}
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
	
	private void pacmanHandler() {
		pacman.updateImage();
		pacman.setSpeed(tyle_board);
	}
	
	private void ghostHandler() {
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
			
			doCollisionEvents(ghost);
			
			if (isNotStalled(ghost.getSpeedPercent())) {
				ghost.ghostStart(global_dot_counter);
				setDotCounterStates(ghosts);
				if (ghost.getHomeState() == HomeState.HAS_EXITED) {
					ghost.makeMove(pacman);
				}
			}
			doCollisionEvents(ghost);
		}
		
		
	}
	
	private void doCollisionEvents(Ghost ghost) {
		if (ghost.checkCollision(power_up, pacman)) {
			global_dot_counter = true;
			global_dots_captured = 0;
			pacman.resetPacMan();
			
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

	public void handleStart() {
		while (pacman.getStartCount() != -1) {
			pacman.pacmanStart();
			PacManBoard.sleep();
		}
	}
	
	private boolean isNotStalled(int speed_percent) {
		double ratio = 1 - speed_percent / 100.0;
		int stall_frame = (int) Math.floor(1.0 / ratio);
		if (cycle_frame % stall_frame == 0)
			return false;
		return true;
	}
	
}
