package pacMan;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import pacMan.TyleContainer.Tyle;
import pacMan.TyleContainer.TyleType;

/*
 * This class contains the methods and abstract methods to be used by each of the four
 * ghosts in PacMan.
 */
public abstract class Ghost {

	//*********************************************************************************//
	//								ENUM DECLARATION 		
	//*********************************************************************************//
	// HomeState lets other classes know when a particular ghost has left its spawn
	// location or is leaving. This is necessary for the program to know when the
	// ghost can begin using its normal method for finding and making its next move.
	// This method is called makeMove.
	public enum HomeState {
		IS_HOME, HAS_EXITED, IS_EXITING
	}

	// DotCounterState is used to trigger each ghost's personal dot counter. This is
	// ultimately used to let the ghost know when he can leave his spawn location.
	public enum DotCounterState {
		ACTIVE, INACTIVE
	}

	// TargetingState represents the three states each ghost can have when choosing
	// which square to travel to.
	public enum TargetingState {
		ATTACK, SCATTER, GO_HOME
	}

	// State represents the four main states of each ghost. These states are not
	// used for ghost maneuvering logic, but do trigger or effect certain graphic
	// changes and other ghost states.
	public enum State {
		DEFAULT, BLUE, BLINKING, HEAD_HOME
	}

	// GhostName is the most broad enum. Each ghost name is an enum. These store the
	// filenames for the graphics, specific to each ghost.
	public enum GhostName {
		INKY(new String[][] { { "images/inky_up.png", "images/inky_up1.png" },
				{ "images/inky_down.png", "images/inky_down1.png" },
				{ "images/inky_left.png", "images/inky_left1.png" },
				{ "images/inky_right.png", "images/inky_right1.png" } }),
		BLINKY(new String[][] { { "images/blinky_up.png", "images/blinky_up1.png" },
				{ "images/blinky_down.png", "images/blinky_down1.png" },
				{ "images/blinky_left.png", "images/blinky_left1.png" },
				{ "images/blinky_right.png", "images/blinky_right1.png" } }),
		PINKY(new String[][] { { "images/pinky_up.png", "images/pinky_up1.png" },
				{ "images/pinky_down.png", "images/pinky_down1.png" },
				{ "images/pinky_left.png", "images/pinky_left1.png" },
				{ "images/pinky_right.png", "images/pinky_right1.png" } }),
		CLYDE(new String[][] { { "images/clyde_up.png", "images/clyde_up1.png" },
				{ "images/clyde_down.png", "images/clyde_down1.png" },
				{ "images/clyde_left.png", "images/clyde_left1.png" },
				{ "images/clyde_right.png", "images/clyde_right1.png" } });

		public String[][] filename;

		GhostName(String[][] filename) {
			this.filename = filename;
		}
	}
	
	// Creates a new Tyle[][] to be initialized by this class's constructor.
	private Tyle[][] tyle_board;

	//*********************************************************************************//
	//*********************************************************************************//
	//					VARIABLES ASSOCIATED WITH IMAGES OR GRAPHICS 		
	//*********************************************************************************//
	// Eyes and blink store the graphics to be painted when a ghost is not in its
	// default state.
	private String[] eyes = { "images/eyes_up.png", "images/eyes_down.png", "images/eyes_left.png",
			"images/eyes_right.png" };
	private String[] blink = { "images/blue_blink1.png", "images/blue_blink2.png" };

	// This holds the ghosts current image, which will be used by the PacManBoard
	// class
	public Image character;

	// Creates a new Animator array for each ghost direction in their default state.
	// Also creates a new Animator for the ghosts, to be used in their blue state.
	private Animator[] animator = { new Animator(), new Animator(), new Animator(), new Animator() };
	private Animator blue_animator = new Animator();
	//*********************************************************************************//
	//*********************************************************************************//
	//									ENUM VARIABLES 		
	//*********************************************************************************//

	// Create each enum variables and initialize dot_counter_state and home_state
	// because these states are shared between each ghost upon starting a new game
	// of PacMan.
	private DotCounterState dot_counter_state = DotCounterState.INACTIVE;
	private HomeState home_state = HomeState.IS_HOME;
	private TargetingState targeting_state;
	private State state;
	private GhostName ghost;
	/***********************************************************************************/
	/***********************************************************************************/
	//							STATE DETERMINATION VARIABLES		
	/***********************************************************************************/

	// This boolean variable indicates when a ghost has changed a certain state. //
	// This is necessary to allow for ghost's anti-backtracking movement to // be
	// temporarily disabled.
	private boolean back_tracking;

	// target_clock triggers a change in a ghost's targeting_state.
	private int target_clock = 0;

	// This is the limit of dots needed to be collected for this ghost
	// to leave its spawn location.
	private int dot_trigger_count;
	private int dots_captured = 0;
	/***********************************************************************************/
	/***********************************************************************************/
	//						VARIABLES ASSOCIATED WITH MOVEMENT 		
	/***********************************************************************************/
	private int x;
	private int y;
	private int curDeltaX = 0;
	private int curDeltaY = 0;
	private int speed = 2;
	private int speed_percent = 75;
	private int start_count = 0;
	
	// These integer arrays store the target for a ghost to reach in each targeting
	// state.
	private int[] attack_target = new int[2];
	private int[] scatter_target = new int[2];
	private int[] home_target = new int[] { 240, 224 };
	/***********************************************************************************/
	/***********************************************************************************/

	// This variable is unique to all others of this class. It is strictly used
	// for when a ghost is traveling home. When density is zero, a ghost may pass
	// this PacMan and enter into the ghost box.
	private int density = 1;

	// This constructor is used to initialize variables that are certain or known on
	// the start of each game.
	public Ghost(GhostName ghost, State state, int x, int y, Tyle[][] tyle_board, TargetingState targeting_state,
			int dot_trigger_count) {
		this.ghost = ghost;
		this.state = state;
		this.x = x;
		this.y = y;
		this.tyle_board = tyle_board;
		this.targeting_state = targeting_state;
		this.dot_trigger_count = dot_trigger_count;
	}

	public abstract void updateAttackTarget(PacMan pacman);
	public abstract void updateScatterTarget();
	public abstract void setHomeTarget();

	public abstract void resetGhost();
	public abstract void ghostStart(boolean global_counter);
	public abstract void ghostStartExit();

	public void setImage() {
		character = Toolkit.getDefaultToolkit().getImage(ghost.filename[0][0]);
	}

	public void updateImage() {

		if (getState() == State.DEFAULT) {
			rotateCharacter();
		} else if (getState() == State.HEAD_HOME) {
			rotateEyes();
		} else {
			blueTimer();
		}

	}

	public void makeMove(PacMan pacman) {
		if (x % PacManBoard.dimension == 0 && y % PacManBoard.dimension == 0) {
			int[] target = new int[2];

			updateAttackTarget(pacman);
			updateScatterTarget();

			if (targeting_state == TargetingState.ATTACK) {
				target = attack_target;
			} else if (targeting_state == TargetingState.SCATTER) {
				target = scatter_target;
			} else if (targeting_state == TargetingState.GO_HOME) {
				target = home_target;
				goHome(target);
				updateX(getDeltaX());
				updateY(getDeltaY());
				return;
			}

			getGhostMove(target[0], target[1]);
		}

		updateX(getDeltaX());
		updateY(getDeltaY());
	}

	public void setSpeed() {
		if (getState() == State.HEAD_HOME) {
			speed_percent = 75;
			if (x % PacManBoard.dimension == 0 && y % PacManBoard.dimension == 0)
				speed = 4;
		} else if (getState() != State.DEFAULT) {
			speed_percent = 50;
			speed = 2;
		} else {
			if (tyle_board[y / PacManBoard.dimension][x / PacManBoard.dimension] == Tyle.TELEPORT_PATH)
				speed_percent = 50;
			else
				speed_percent = 75;
			speed = 2;
		}
	}

	public void getGhostMove(int newX, int newY) {
		if (tyle_board[y / PacManBoard.dimension][x / PacManBoard.dimension].type == TyleType.TELEPORT) {
			teleport(tyle_board[y / PacManBoard.dimension][x / PacManBoard.dimension]);
		}

		List<int[]> move = new ArrayList<int[]>();
		int[][] delta = { { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 } };

		for (int i = 0; i < 4; i++) {
			if (isValid(delta[i][0], delta[i][1])) {
				move.add(delta[i]);
			}
		}

		int[] chosen_move = findClosestMove(newX, newY, move);
		updateSpeed(speed);

		updateDeltaX(chosen_move[0]);
		updateDeltaY(chosen_move[1]);

	}

	private int[] findClosestMove(int newX, int newY, List<int[]> move) {
		double distance = 100000;

		for (int i = 0; i < move.size(); i++) {
			double tempDistance = getDistance(newX, newY, move.get(i));
			if (tempDistance < distance)
				distance = tempDistance;
		}

		for (int i = 0; i < move.size(); i++) {
			if (getDistance(newX, newY, move.get(i)) > distance)
				move.remove(i);
		}

		return move.get(0);
	}

	private double getDistance(int newX, int newY, int[] move) {

		int row1 = y + move[1] * PacManBoard.dimension, row2 = newY;
		int col1 = x + move[0] * PacManBoard.dimension, col2 = newX;

		double distance = Math.sqrt((row2 - row1) * (row2 - row1) + (col2 - col1) * (col2 - col1));
		return distance;
	}

	public boolean isValid(int dx, int dy) {
		if (back_tracking == false && (dx != 0 && dx == getDeltaX() * -1) || (dy != 0 && dy == getDeltaY() * -1)) {
			return false;
		}
		back_tracking = false;
		int column = getX() / PacManBoard.dimension + dx, row = getY() / PacManBoard.dimension + dy;

		if (tyle_board[row][column].type == TyleType.UNREACHABLE || tyle_board[row][column].type == TyleType.WALL)
			return false;
		if (tyle_board[row][column].type == TyleType.GHOSTGATE && dy == 1 && density == 1)
			return false;
		if (tyle_board[row][column] == Tyle.DOWN_ONLY_SQUARE && dy == -1 && density == 1)
			return false;
		return true;
	}

	public void teleport(Tyle type) {
		if (type == Tyle.TELEPORT_SQUARE_A && curDeltaX == -1) {
			for (int i = 0; i < tyle_board.length; i++) {
				for (int j = 0; j < tyle_board[0].length; j++) {
					if (tyle_board[i][j] == Tyle.TELEPORT_SQUARE_B) {
						this.x = j * PacManBoard.dimension;
						this.y = i * PacManBoard.dimension;
						return;
					}
				}
			}
		}
		if (type == Tyle.TELEPORT_SQUARE_B && curDeltaX == 1) {
			for (int i = 0; i < tyle_board.length; i++) {
				for (int j = 0; j < tyle_board[0].length; j++) {
					if (tyle_board[i][j] == Tyle.TELEPORT_SQUARE_A) {
						this.x = j * PacManBoard.dimension;
						this.y = i * PacManBoard.dimension;
						return;
					}
				}
			}
		}
	}

	public void stateHandler(DotState dotstate) {

		if (target_clock == 1200)
			target_clock = 0;

		if (target_clock / 600 == 0 && targeting_state != TargetingState.GO_HOME)
			targeting_state = TargetingState.ATTACK;
		else if (target_clock / 600 == 1 && targeting_state != TargetingState.GO_HOME)
			targeting_state = TargetingState.SCATTER;

		target_clock++;

		if (dotstate.blueTimer == 0) {
			if (dotstate.getState() != DotState.State.OFF && !dotstate.blinking)
				state = State.BLUE;
		} else if (state == State.BLUE && dotstate.getState() != DotState.State.OFF && dotstate.blinking == true
				&& state != State.HEAD_HOME)
			state = State.BLINKING;
		if (dotstate.getState() == DotState.State.OFF && state != State.HEAD_HOME)
			state = State.DEFAULT;
	}

	public boolean checkCollision(DotState dotstate, PacMan pacman) {
		int x = pacman.getX();
		int y = pacman.getY();

		if (density == 1 && this.x / PacManBoard.dimension == x / PacManBoard.dimension 
				&& this.y / PacManBoard.dimension == y / PacManBoard.dimension) {
			if (state != State.DEFAULT && state != State.HEAD_HOME) {
				state = State.HEAD_HOME;
				targeting_state = TargetingState.GO_HOME;
				dotstate.decrementGhosts();
				back_tracking = true;
			} else if (state != State.HEAD_HOME) {
				return true;
			}
		}
		return false;
	}

	public void goHome(int[] target) {
		if (x % PacManBoard.dimension == 0 && y % PacManBoard.dimension == 0) {
			getGhostMove(target[0], target[1]);
			density = 0;
		}
		if (x == 240 && y == 224) {
			state = State.DEFAULT;
			targeting_state = TargetingState.ATTACK;
			density = 1;
		}
	}

	public void blueTimer() {
		if (state == State.BLINKING) {
			character = blue_animator.generateAnimation(12, blink);
		} else {
			this.character = Toolkit.getDefaultToolkit().getImage("images/blue.png");
		}
	}

	public void rotateCharacter() {
		int[][] delta = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };

		for (int i = 0; i < 4; i++) {
			if (curDeltaX == delta[i][0] && curDeltaY == delta[i][1]) {
				character = animator[i].generateAnimation(5, ghost.filename[i]);
			}
		}
	}

	public void rotateEyes() {
		int[][] delta = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };

		for (int i = 0; i < 4; i++) {
			if (curDeltaX == delta[i][0] && curDeltaY == delta[i][1]) {
				character = Toolkit.getDefaultToolkit().getImage(eyes[i]);
			}
		}
	}

	public void rotate() {

	}

	public void updateDeltaX(int deltaX) {
		this.curDeltaX = deltaX;
	}

	public void updateDeltaY(int deltaY) {
		this.curDeltaY = deltaY;
	}

	public void updateX(int deltaX) {
		this.x += Math.floor(curDeltaX * speed);
	}

	public void updateY(int deltaY) {
		this.y += Math.floor(curDeltaY * speed);
	}

	public void resetX(int x) {
		this.x = x;
	}

	public void resetY(int y) {
		this.y = y;
	}

	public void updateName(GhostName ghost) {
		this.ghost = ghost;
	}

	public void updateState(State state) {
		this.state = state;
	}

	public void updateSpeed(int speed) {
		this.speed = speed;
	}

	public int updateStartCount(int num) {
		return start_count = num;
	}

	public int getDeltaX() {
		return curDeltaX;
	}

	public int getDeltaY() {
		return curDeltaY;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSpeed() {
		return speed;
	}

	public Image getImage() {
		return character;
	}

	public int getStartCount() {
		return start_count;
	}

	public GhostName getGhostName() {
		return ghost;
	}

	public State getState() {
		return state;
	}

	public int getSpeedPercent() {
		return speed_percent;
	}

	public void updateSpeedPercent(int percent) {
		speed_percent = percent;
	}

	public Tyle[][] getTyleBoard() {
		return tyle_board;
	}

	public void updateDensity(int density) {
		this.density = density;
	}

	public void setAttackTarget(int[] target) {
		attack_target = target;
	}

	public void setScatterTarget(int[] target) {
		scatter_target = target;
	}

	public void setHomeTarget(int[] target) {
		home_target = target;
	}

	public void setDotCounterState(DotCounterState state) {
		this.dot_counter_state = state;
	}

	public DotCounterState getDotCounterState() {
		return dot_counter_state;
	}

	public void setDotTriggerCount(int count) {
		dot_trigger_count = count;
	}

	public int getDotTriggerCount() {
		return dot_trigger_count;
	}

	public void setHomeState(HomeState home_state) {
		this.home_state = home_state;
	}

	public HomeState getHomeState() {
		return home_state;
	}

	public int getNumDotsCaptured() {
		return dots_captured;
	}

	public void incrementDotsCaptured(PacMan pacman) {
		if (dot_counter_state == DotCounterState.ACTIVE) {
			if (tyle_board[pacman.getY() / PacManBoard.dimension][pacman.getX()
					/ PacManBoard.dimension].type == TyleType.DOT) {
				dots_captured++;
			}
		}
	}

	public void resetDotsCaptured() {
		dots_captured = 0;
	}

}
