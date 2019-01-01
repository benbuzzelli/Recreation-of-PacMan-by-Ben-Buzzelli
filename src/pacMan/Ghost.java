package pacMan;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import pacMan.Ghost.DotCounterState;
import pacMan.Ghost.HomeState;
import pacMan.PacMan.Visibility;
import pacMan.TyleContainer.Tyle;
import pacMan.TyleContainer.TyleType;

/*
 * This class contains the methods and abstract methods to be used by each of the four
 * ghosts in PacMan.
 */
public abstract class Ghost {

	// *********************************************************************************//
	// ENUM DECLARATION
	// *********************************************************************************//
	// HomeState lets other classes know when a particular ghost has left its spawn
	// location or is leaving. This is necessary for the program to know when the
	// ghost can begin using its normal method for finding and making its next move.
	// This method is called makeMove.
	public enum HomeState {
		IS_HOME, HAS_EXITED, IS_EXITING, IS_ENTERING
	}

	// DotCounterState is used to trigger each ghost's personal dot counter. This is
	// ultimately used to let the ghost know when he can leave his spawn location.
	public enum DotCounterState {
		ACTIVE, INACTIVE
	}

	// TargetingState represents the three states each ghost can have when choosing
	// which square to travel to.
	public enum TargetingState {
		ATTACK, SCATTER, FRIGHTENED, GO_HOME
	}

	// State represents the four main states of each ghost. These states are not
	// used for ghost maneuvering logic, but do trigger or effect certain graphic
	// changes and other ghost states.
	public enum State {
		DEFAULT, BLUE, BLINKING, HEAD_HOME
	}
	
	public enum Visibility {
		VISIBLE, NOT_VISIBLE
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

	// *********************************************************************************//
	// *********************************************************************************//
	// VARIABLES ASSOCIATED WITH IMAGES OR GRAPHICS
	// *********************************************************************************//
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
	// *********************************************************************************//
	// *********************************************************************************//
	// ENUM VARIABLES
	// *********************************************************************************//

	// Create each enum variables and initialize dot_counter_state and home_state
	// because these states are shared between each ghost upon starting a new game
	// of PacMan.
	private DotCounterState dot_counter_state = DotCounterState.INACTIVE;
	private HomeState home_state;
	private TargetingState targeting_state;
	private State state;
	private GhostName ghost;
	private Visibility visibility = Visibility.VISIBLE;
	// *********************************************************************************//
	// *********************************************************************************//
	// STATE DETERMINATION VARIABLES
	// *********************************************************************************//

	// This boolean variable indicates when a ghost has changed a certain state. //
	// This is necessary to allow for ghost's anti-backtracking movement to // be
	// temporarily disabled.
	private boolean back_tracking;
	
	// This is the limit of dots needed to be collected for this ghost
	// to leave its spawn location.
	private int dot_trigger_count;
	private int dots_captured = 0;
	// *********************************************************************************//
	// *********************************************************************************//
	// VARIABLES ASSOCIATED WITH MOVEMENT
	// *********************************************************************************//
	public int spawnX;
	public int spawnY;
	
	private int exitX;
	private int exitY;
	
	private int x;
	private int y;
	private int curDeltaX = 0;
	private int curDeltaY = 0;
	private int speed = 2;
	private int speed_percent = 75;
	private int frames_stalled = 0;
	private int start_count = 0;

	// These integer arrays store the target for a ghost to reach in each targeting
	// state.
	private int[] attack_target = new int[2];
	private int[] scatter_target = new int[2];
	private int[] home_target = new int[2];
	/***********************************************************************************/
	/***********************************************************************************/

	// This variable is unique to all others of this class. It is strictly used
	// for when a ghost is traveling home. When density is zero, a ghost may pass
	// this PacMan and enter into the ghost box.
	private int density = 1;

	// This constructor is used to initialize variables that are certain or known on
	// the start of each game.
	public Ghost(GhostName ghost, State state, Tyle[][] tyle_board, TargetingState targeting_state,
			HomeState home_state, int dot_trigger_count) {
		this.ghost = ghost;
		this.state = state;
		this.tyle_board = tyle_board;
		this.targeting_state = targeting_state;
		this.home_state = home_state;
		this.dot_trigger_count = dot_trigger_count;
		setSpawnLocation();

		this.x = spawnX;
		this.y = spawnY;
	}

	// Each ghost has a different way of choosing their targets for each state.
	// These need to be abstract because no ghosts share the same targeting logic.
	public abstract void updateAttackTarget(PacMan pacman);

	public abstract void updateScatterTarget();

	// This will reset a ghost to its unique spawn location. It will also reset
	// any variables that have changed and need to be initialized.
	public abstract void resetGhost();

	// Initialize the starting image of a ghost.
	public void setImage() {
		character = Toolkit.getDefaultToolkit().getImage(ghost.filename[0][0]);
	}

	public abstract void setSpawnLocation();

	// Update a ghost's image based on which State its in.
	public void updateImage() {

		if (getState() == State.DEFAULT) {
			rotateCharacter();
		} else if (getState() == State.HEAD_HOME) {
			rotateEyes();
		} else {
			rotateBlue();
		}

	}

	// There are three conditions that effect a ghost's speed during a level.
	// These are:
	// 1. After a ghost has been eaten by PacMan when PacMan is powered up.
	// 2. When a ghost transitions back into its default state.
	// 3. If a ghost is traveling through a teleport path.
	public void setSpeed() {
		if (getState() == State.HEAD_HOME) {
			speed_percent = 75; // Set speed_percent to 75 after a ghost has been eaten.
			if (x % PacManBoard.dimension == 0 && y % PacManBoard.dimension == 0)
				speed = 4; // Also set speed to 4.
		} else if (getState() != State.DEFAULT) {
			speed_percent = 50; // Set speed_percent to 50 if a ghost is blue.
			speed = 2;
		} else {
			if (tyle_board[y / PacManBoard.dimension][x / PacManBoard.dimension] == Tyle.TELEPORT_PATH)
				speed_percent = 40; // Set speed_percent to 50 when a ghost is traveling on a TELEPORT_PATH.
			else
				speed_percent = 75; // Set speed_percent to 75 when a ghost is in its Default state.
			speed = 2;
		}
	}

	public void ghostStart(boolean global_counter) {
		if (getHomeState() == HomeState.IS_HOME) {
			updateSpeed(getSpeed());
			if (ghost != GhostName.PINKY && getDeltaY() == 0) {
				updateDeltaY(-1);
			}
			else if (ghost == GhostName.PINKY && getDeltaY() == 0) {
				updateDeltaY(1);
			}
			
			if (getY() == spawnY + PacManBoard.dimension / 2)
				updateDeltaY(-1);
			else if (getY() == spawnY - PacManBoard.dimension / 2) {
				updateDeltaY(1);
			}
			updateX(getDeltaX());
			updateY(getDeltaY());
			if (getNumDotsCaptured() >= getDotTriggerCount() && !global_counter) {
				setDotCounterState(DotCounterState.INACTIVE);
				setHomeState(HomeState.IS_EXITING);
			}
		} else if (getHomeState() == HomeState.IS_EXITING) {
			ghostStartExit();
		}
	}

	public void ghostStartExit() {
		updateSpeed(1);
		if (getX() != exitX) {
			if (getX() < exitX)
				updateDeltaX(1);
			else
				updateDeltaX(-1);
			updateDeltaY(0);
		} else if (getY() != exitY) {
			updateDeltaX(0);
			updateDeltaY(-1);
		} else {
			updateDeltaX(-1);
			updateDeltaY(0);
			setHomeState(HomeState.HAS_EXITED);
			updateSpeed(2);
			return;
		}
		updateX(getDeltaX());
		updateY(getDeltaY());
	}

	// Method for making a ghost's move and updating its position based on its
	// target square.
	public void makeMove(PacMan pacman) {
		if (x % PacManBoard.dimension == 0 && y % PacManBoard.dimension == 0) {
			int[] target = new int[2];

			updateAttackTarget(pacman); // Update this each time makeMove is called
			updateScatterTarget(); // Update this each time makeMove is called

			if (targeting_state == TargetingState.ATTACK) {
				target = attack_target; // Set the ghost's target equal to attack target when in its attack state.
			} else if (targeting_state == TargetingState.SCATTER) {
				target = scatter_target; // Set the ghost's target equal to its appropriate state.
			} else if (targeting_state == TargetingState.GO_HOME) {
				goHome(); // Call goHome method during a Ghost's GO_HOME state because there are more
								// specific instructions.
				updateX(curDeltaX); // Update x and y for each move.
				updateY(curDeltaY);
				return; // Return to skip the next instructions.
			}

			getGhostMove(target[0], target[1]); // Call getGhostMove which will set curDeltaX and curDeltaY to their
												// appropriate values.
		}

		updateX(getDeltaX()); // Update x and y for each move.
		updateY(getDeltaY());
	}

	// This method takes in a dx and dy and updates a ghost's curdelta values.
	public void getGhostMove(int targetX, int targetY) {
		if (tyle_board[y / PacManBoard.dimension][x / PacManBoard.dimension].type == TyleType.TELEPORT)
			teleport(tyle_board[y / PacManBoard.dimension][x / PacManBoard.dimension]); // Call this if a Ghost is on a
																						// TELEPORT square.

		List<int[]> move = new ArrayList<int[]>(); // Create an list to store the possible moves a ghost could make.
		int[][] delta = { { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 } }; // These are the four delta values in ordered this
																		// way for desired preference of selection.

		for (int i = 0; i < 4; i++) {
			if (isValid(delta[i][0], delta[i][1])) { // Call isValid to check is a move is valid.
				move.add(delta[i]); // Add a move to the list if it is valid.
			}
		}
		
		int[] chosen_move = new int[2];
		if (targeting_state != TargetingState.FRIGHTENED)
			chosen_move = findClosestMove(targetX, targetY, move); // Call findClosestMove to get the desired move.
		else {
			chosen_move = findLongestMove(targetX, targetY, move);
		}
		updateSpeed(speed);

		updateDeltaX(chosen_move[0]); // Update each delta with the new move.
		updateDeltaY(chosen_move[1]);

	}

	// This method takes in a target x and y and a moves list, and returns the move
	// which will put the ghost closest to its target.
	private int[] findClosestMove(int targetX, int targetY, List<int[]> move) {
		double distance = 100000; // Set distance to an unreachable, high value.

		for (int i = 0; i < move.size(); i++) {
			double tempDistance = getDistance(targetX, targetY, move.get(i)); // Use getDistance to find move's diagonal
																				// distance to its target.
			if (tempDistance < distance)
				distance = tempDistance; // Set distance equal to the new tempDistance if tempDistance is less than
											// distance.
		}

		for (int i = 0; i < move.size(); i++) {
			if (getDistance(targetX, targetY, move.get(i)) > distance) {
				move.remove(i); // Remove any moves that result in a greater distance than the shortest
								// distance.
				i--;
			}
		}

		return move.get(0);
	}
	
	private int[] findLongestMove(int targetX, int targetY, List<int[]> move) {
		double distance = -100;

		for (int i = 0; i < move.size(); i++) {
			double tempDistance = getDistance(targetX, targetY, move.get(i)); // Use getDistance to find move's diagonal
																				// distance to its target.
			if (tempDistance > distance)
				distance = tempDistance;
		}

		for (int i = 0; i < move.size(); i++) {
			if (getDistance(targetX, targetY, move.get(i)) < distance) {
				move.remove(i); // Remove any moves that result in a greater distance than the shortest
								// distance.
				i--;
			}
		}

		return move.get(0);
	}

	// Returns a double, representing the diagonal distance between two points.
	private double getDistance(int targetX, int targetY, int[] move) {

		// Set row and column values by adding delta number of dimension to a ghosts
		// current location.
		int newY = y + move[1] * PacManBoard.dimension, newX = x + move[0] * PacManBoard.dimension;

		// Use Pathagorean's theorem to get the diagonal distance between the two
		// points.
		double distance = Math.sqrt((targetY - newY) * (targetY - newY) + (targetX - newX) * (targetX - newX));

		return distance;
	}

	public boolean isValid(int dx, int dy) {
		// Check if a new dx and dy will cause a ghost to turn around and return false
		// if this is the case.
		// Do not return false however, if back_tracking is true.
		if (back_tracking == false && (dx != 0 && dx == getDeltaX() * -1) || (dy != 0 && dy == getDeltaY() * -1)) {
			return false;
		}
		back_tracking = false;

		// Get the new column and row using the input delta values.
		int column = getX() / PacManBoard.dimension + dx, row = getY() / PacManBoard.dimension + dy;

		// Return false if a ghost would end up on a WALL or UNREACHABLE tyle
		if (tyle_board[row][column].type == TyleType.UNREACHABLE || tyle_board[row][column].type == TyleType.WALL)
			return false;
		// Return false if a ghost would travel down onto a GHOSTGATE tyle. A ghost
		// without a density of 1
		// may do this though.
		if (tyle_board[row][column].type == TyleType.GHOSTGATE && dy == 1 && density == 1)
			return false;
		// Return false if a ghost would travel up through a DOWN_ONLY_SQUARE, unless
		// density is not 1.
		if (tyle_board[row][column] == Tyle.DOWN_ONLY_SQUARE && dy == -1 && density == 1)
			return false;
		
		return true; // If not illegal conditions have been met, then this move is acceptable.
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

	public boolean checkCollision(PowerUp power_up, PacMan pacman) {
		int pRow = pacman.getY() / PacManBoard.dimension;
		int pCol = pacman.getX() / PacManBoard.dimension;
		int gRow = y / PacManBoard.dimension;
		int gCol = x / PacManBoard.dimension;

		if (density == 1 && pRow == gRow && pCol == gCol && state == State.DEFAULT) {
			return true;
		}
		return false;
	}

	public void goHome() {
		if (x != home_target[0] || y != home_target[1]) {
			getGhostMove(home_target[0], home_target[1]);
			targeting_state = TargetingState.GO_HOME;
			density = 0;
		} else {
			state = State.DEFAULT;
			home_state = HomeState.IS_EXITING;
			targeting_state = TargetingState.ATTACK;
			density = 1;
		}
	}

	public void rotateBlue() {
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
	
	public void setFrightenedTarget(PacMan pacman) {
		setAttackTarget(new int[] {pacman.getX(), pacman.getY()});
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
	
	public void changeImage(String filename, Image image) {
		if (image == null)
			character = Toolkit.getDefaultToolkit().getImage(filename);
		else
			character = image;
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
	
	public int getDensity() {
		return density;
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

	public TargetingState getTargetingState() {
		return targeting_state;
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
	
	public void setState(State state) {
		this.state = state;
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
	
	public void setTargetingState(TargetingState targeting_state) {
		this.targeting_state = targeting_state;
	}
	
	public void setExitX(int exitX) {
		this.exitX = exitX;
	}
	
	public void setExitY(int exitY) {
		this.exitY = exitY;
	}

	public void setBackTracking(boolean value) {
		back_tracking = value;
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	
	public void changeVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
	
	public int getFramesStalled() {
		return frames_stalled;
	}
	
	public void changeFramesStalled(int num) {
		frames_stalled = num;
	}
	
	public boolean isStalled(int frames_passed) {
		double ratio = (100 - speed_percent) / 100.0;
		if ((frames_stalled * 1.0) / frames_passed < ratio)
			return true;
		else
			return false;
	}

}
