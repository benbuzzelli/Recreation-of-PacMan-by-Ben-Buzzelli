package pacMan;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pacMan.PacManBoard.Tyle;
import pacMan.PacManBoard.TyleType;

public abstract class Ghost {
	public enum State {
		DEFAULT, BLUE
	}

	public enum GhostName {
		INKY("inky"), BLINKY("blinky"), PINKY("pinky"), CLYDE("clyde");

		public String filename_prefix;

		GhostName(String filename) {
			this.filename_prefix = filename;
		}
	}

	public Ghost(GhostName ghost, State state, int x, int y) {
		this.ghost = ghost;
		this.state = state;
		this.x = x;
		this.y = y;
	}

	private State state;
	private GhostName ghost;
	private int x;
	private int y;
	private Random rand = new Random();

	public static String[][] filename_appendix = { { "_up.png", "_up1.png", }, { "_down.png", "_down1.png", },
			{ "_left.png", "_left1.png", }, { "_right.png", "_right1.png", } };
	public String curName;
	public Image character;
	public int blueFrame = 0;
	private final int dimension = 16;
	private int curDeltaX = 0;
	private int curDeltaY = 0;
	private int speed = 2;
	private int start_count = 0;
	public int image_frame = 0;

	public abstract void resetGhost();

	public abstract void ghostStart();

	public abstract void rotateCharacter();

	public abstract void rotateCharacterBlue();
	
	public void setImage() {
		character = Toolkit.getDefaultToolkit().getImage(ghost.filename_prefix + filename_appendix[0][0]);
	}
	
	public void updateImage() {
		
		if (image_frame == 10)
			image_frame = 0;
		
		if (getState() == State.DEFAULT)
			rotateCharacter();
		else
			rotateCharacterBlue();
		
		image_frame++;
	}

	public void updateMove(PacMan pacman, PacManBoard.Tyle[][] tyle_board) {
		if (getX() % getDimension() == 0 && getY() % getDimension() == 0)
			getChaseMove(pacman, tyle_board);
		updateX(getDeltaX());
		updateY(getDeltaY());
	}

	public void getChaseMove(PacMan pacman, PacManBoard.Tyle[][] tyle_board) {
		if (tyle_board[getY() / getDimension()][getX() / getDimension()].type == TyleType.TELEPORT) {
			teleport(tyle_board[getY() / getDimension()][getX() / getDimension()], tyle_board);
		}

		List<int[]> move = new ArrayList<int[]>();
		int[][] delta = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };

		for (int i = 0; i < 4; i++) {
			if (isValid(delta[i][0], delta[i][1], tyle_board)) {
				move.add(delta[i]);
			}
		}

		int[] chosen_move = findClosestMove(pacman, move);
		updateSpeed(speed);

		updateDeltaX(chosen_move[0]);
		updateDeltaY(chosen_move[1]);

	}

	private int[] findClosestMove(PacMan pacman, List<int[]> move) {
		int distance = 100000;

		for (int i = 0; i < move.size(); i++) {
			int tempDistance = getDistance(pacman, move.get(i));
			if (tempDistance < distance)
				distance = tempDistance;
		}

		for (int i = 0; i < move.size(); i++) {
			if (getDistance(pacman, move.get(i)) > distance)
				move.remove(i);
		}

		int move_index = rand.nextInt(move.size());
		return move.get(move_index);
	}

	private int getDistance(PacMan pacman, int[] move) {

		int row1 = getY() + move[1], row2 = pacman.getY();
		int col1 = getX() + move[0], col2 = pacman.getX();

		int distance = Math.abs(row2 - row1) + Math.abs(col2 - col1);
		return distance;
	}

	public boolean isValid(int dx, int dy, PacManBoard.Tyle[][] tyle_board) {
		if ((dx != 0 && dx == getDeltaX() * -1) || (dy != 0 && dy == getDeltaY() * -1))
			return false;
		int column = getX() / getDimension() + dx, row = getY() / getDimension() + dy;
		int num_columns = tyle_board[0].length, num_rows = tyle_board.length;
		if (column < 0 || column >= num_columns || row < 0 || row >= num_rows)
			return false;
		if (tyle_board[row][column].type == TyleType.UNREACHABLE || tyle_board[row][column].type == TyleType.WALL)
			return false;
		if (tyle_board[row][column].type == TyleType.GHOSTGATE && dy == 1)
			return false;
		if (tyle_board[row][column] == Tyle.DOWN_ONLY_SQUARE && dy == -1)
			return false;
		return true;
	}

	public void teleport(PacManBoard.Tyle type, PacManBoard.Tyle[][] tyle_board) {
		if (type == PacManBoard.Tyle.TELEPORT_SQUARE_A && curDeltaX == -1) {
			for (int i = 0; i < tyle_board.length; i++) {
				for (int j = 0; j < tyle_board[0].length; j++) {
					if (tyle_board[i][j] == PacManBoard.Tyle.TELEPORT_SQUARE_B) {
						this.x = j * dimension;
						this.y = i * dimension;
						return;
					}
				}
			}
		}
		if (type == PacManBoard.Tyle.TELEPORT_SQUARE_B && curDeltaX == 1) {
			for (int i = 0; i < tyle_board.length; i++) {
				for (int j = 0; j < tyle_board[0].length; j++) {
					if (tyle_board[i][j] == PacManBoard.Tyle.TELEPORT_SQUARE_A) {
						this.x = j * dimension;
						this.y = i * dimension;
						return;
					}
				}
			}
		}
	}

	public boolean checkCollision(PacMan pacman) {
		int x = pacman.getX();
		int y = pacman.getY();
		int dimension = getDimension();

		if (pacman.isBlueGhost)
			state = State.BLUE;
		if (this.getX() / dimension == x / dimension && this.getY() / dimension == y / dimension) {
			if (state == State.BLUE) {
				resetGhost();
			} else
				pacman.resetPacMan();
			return true;
		}
		return false;
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

	public int getDimension() {
		return dimension;
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

}
