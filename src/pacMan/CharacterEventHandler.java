package pacMan;
import pacMan.Ghost.DotCounterState;
import pacMan.Ghost.HomeState;
import pacMan.TyleContainer.Tyle;
import pacMan.TyleContainer.TyleType;

public class CharacterEventHandler {

	private int cycle_frame = 0;
	private final int frames_per_cycle;

	private PacMan pacman;
	private Ghost[] ghosts;
	
	private DotState dotstate;
	private int global_dots_captured = 0;
	private boolean global_dot_counter;
	private int[] globat_dot_limit = {0, 7, 17, 32};
	
	private Tyle[][] tyle_board;

	public CharacterEventHandler(int frames_per_cycle, PacMan pacman, Ghost[] ghosts, Tyle[][] tyle_board) {
		this.frames_per_cycle = frames_per_cycle;
		this.pacman = pacman;
		this.ghosts = ghosts;
		this.dotstate = new DotState(pacman, DotState.State.OFF, tyle_board);
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
	
	public void postKeyPressEventHandler(int[] delta) {
		pacmanHandler();
		
		dotstate.blinkPowerUps(cycle_frame, 10);
		dotstate.powerupHandler(pacman);
		
		if (isNotStalled(pacman.getSpeedPercent())) {
			pacman.update(delta[0], delta[1], tyle_board);
		}
		
		if (pacman.getDeltaX() != 0 || pacman.getDeltaY() != 0) {		
			ghostHandler();
			cycleHandler();
		}
	}
	
	public void cycleHandler() {
		cycle_frame++;
		if (cycle_frame == frames_per_cycle)
			cycle_frame = 0;
	}
	
	public void setGhostData(Ghost ghost) {
		ghost.setSpeed();
		ghost.setImage();
		ghost.updateImage();
		ghost.stateHandler(dotstate);
	}
	
	private void pacmanHandler() {
		pacman.updateImage();
		pacman.setSpeed(tyle_board);
		pacman.updateDots(tyle_board);
	}
	
	private void ghostHandler() {
		for (int i = 0; i < 4; i++) {
			Ghost ghost = ghosts[i];
			setGhostData(ghost);
			
			doCollisionEvents(ghost);
			
			if (isNotStalled(ghost.getSpeedPercent())) {
				ghost.ghostStart(global_dot_counter);
				setDotCounterStates(ghosts);
				if (ghost.getHomeState() == HomeState.HAS_EXITED)
					ghost.makeMove(pacman);
				
			}
			
			doCollisionEvents(ghost);
			
		}
		
		if (!global_dot_counter) {
			incrementGhostDotCount();
		}
		else {
			globalCounterHandler();
		}
	}
	
	private void doCollisionEvents(Ghost ghost) {
		if (ghost.checkCollision(dotstate, pacman)) {
			global_dot_counter = true;
			global_dots_captured = 0;
			pacman.resetPacMan();
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
		} else if (global_dots_captured >= globat_dot_limit[2] && ghosts[2].getHomeState() == HomeState.IS_HOME) {
			ghosts[2].setHomeState(HomeState.IS_EXITING);
		} else if (global_dots_captured >= globat_dot_limit[3] && ghosts[3].getHomeState() == HomeState.IS_HOME) {
			ghosts[3].setHomeState(HomeState.IS_EXITING);
		}
	}
	
	public void setDotCounterStates(Ghost[] ghosts) {
		for (int i = 0; i < ghosts.length - 1; i++) {
			if (ghosts[i].getHomeState() == HomeState.HAS_EXITED) {
				if (ghosts[i + 1].getHomeState() == HomeState.IS_HOME)
					ghosts[i + 1].setDotCounterState(DotCounterState.ACTIVE);
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
