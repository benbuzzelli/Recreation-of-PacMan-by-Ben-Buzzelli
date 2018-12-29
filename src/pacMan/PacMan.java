package pacMan;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Timer;

import pacMan.TyleContainer.Tyle;
import pacMan.TyleContainer.TyleType;


public class PacMan {

	public enum Visibility {
		VISIBLE, NOT_VISIBLE
	}
	
	public enum State {
		DEFAULT, POWERED, DEAD
	}

	public enum Name {
		PACMAN;

		String filename_prefix = "images/pacman";

	}

	private static final PacManBoard pcBoard = null;

	public Name name = Name.PACMAN;
	public State state = State.DEFAULT;
	private Visibility visibility = Visibility.VISIBLE;

	private String dyingSpriteSheet = "images/PacManDyingSprite.png";
	public static String[][] filename_appendix = { { "_closed.png" }, { "_up.png", "_up1.png", },
			{ "_down.png", "_down1.png", }, { "_left.png", "_left1.png", }, { "_right.png", "_right1.png", } };
	public Image character = Toolkit.getDefaultToolkit().getImage(name.filename_prefix + filename_appendix[0][0]);
	public int image_frame = 0;
	
	private Animator spriteAnimator = new Animator();

	private Tyle[][] tyle_board;
	
	public boolean isBlueGhost;
	
	private char spawn_char = 'N';
	private int spawnX;
	private int spawnY;
	private int y;
	private int x;
	private final int dimension = 16;
	private int curDeltaX = 0;
	private int curDeltaY = 0;
	private int speed = 2;
	private int curSpeed = 0;
	private int start_count = 0;
	private int speed_percent = 80;
		
	public PacMan(Tyle[][] tyle_board) {
		this.tyle_board = tyle_board;
		setSpawnLocation();
		this.x = spawnX;
		this.y = spawnY;
	}
	
	public void setSpawnLocation() {
		int rows = tyle_board.length;
		int columns = tyle_board[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (tyle_board[i][j].c == spawn_char) {
					spawnX = j * PacManBoard.dimension + PacManBoard.dimension / 2;
					spawnY = i * PacManBoard.dimension;
				}
			}
		}
		
	}

	public void resetPacMan() {
		start_count = 0;
		speed_percent = 80;
    
		resetX(spawnX);
		resetY(spawnY);
	}
	
	public void pacmanStart() {
		if (start_count < 30) {
			curDeltaX = 0;
			curDeltaY = 0;
			curSpeed = 0;
			start_count++;
		}
		else {
			curDeltaX = -1;
			curDeltaY = 0;
			curSpeed = 2;
			start_count = -1;
		}
	}
	
	public void setSpeed(Tyle[][] tyle_board) {
		if (tyle_board[y / PacManBoard.dimension][x / PacManBoard.dimension] == Tyle.DOT_SQUARE)
			speed_percent = 70;
		else
			speed_percent = 80;
	}

	public boolean updateDots(Tyle[][] tyle_board) {
		if (tyle_board[y / dimension][x / dimension].type == TyleType.DOT){
			tyle_board[y / dimension][x / dimension] = Tyle.BLACK_SQUARE;
			return true;
		}
		return false;
	}

	public void teleport(Tyle type, Tyle[][] tyle_board) {
		if (type == Tyle.TELEPORT_SQUARE_A && curDeltaX == -1) {
			for (int i = 0; i < tyle_board.length; i++) {
				for (int j = 0; j < tyle_board[0].length; j++) {
					if (tyle_board[i][j] == Tyle.TELEPORT_SQUARE_B) {
						this.x = j * dimension;
						this.y = i * dimension;
						return;
					}
				}
			}
		}
		if (type == Tyle.TELEPORT_SQUARE_B && curDeltaX == 1) {
			for (int i = 0; i < tyle_board.length; i++) {
				for (int j = 0; j < tyle_board[0].length; j++) {
					if (tyle_board[i][j] == Tyle.TELEPORT_SQUARE_A) {
						this.x = j * dimension;
						this.y = i * dimension;
						return;
					}
				}
			}
		}
	}
	
	public void updateImage() throws IOException {
		if (image_frame == 12)
			image_frame = 0;
		
		rotateCharacter();
		
		image_frame++;
	}

	public void rotateCharacter() throws IOException {

		if (state != State.DEAD) {
			int[][] delta = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };
	
			for (int i = 1; i < 5; i++) {
				if (curDeltaX == delta[i-1][0] && curDeltaY == delta[i-1][1]) {
					String filename = new String();
					if (curSpeed != 0) {
						if (image_frame / 4 == 2)
							filename = name.filename_prefix + filename_appendix[0][0];
						else
							filename = name.filename_prefix + filename_appendix[i][image_frame / 4];
					}
					else
						filename = name.filename_prefix + filename_appendix[i][0];
					character = Toolkit.getDefaultToolkit().getImage(filename);
				}
				else if (curSpeed == 0 && curDeltaX == 0 && curDeltaY == 0)
					character = Toolkit.getDefaultToolkit().getImage(name.filename_prefix + filename_appendix[0][0]);
			}
		} else {
			character = spriteAnimator.generateSpriteAnimation(5, 11, dyingSpriteSheet, pcBoard);
		}
	}

	public void update(int dx, int dy, Tyle[][] tyle_board) {
		if (x % dimension == 0 && y % dimension == 0) {
			if (getIfValid(dx, dy, tyle_board)) {
				curDeltaX = dx;
				curDeltaY = dy;
				updateSpeed(speed);
			}
			else if (!getIfValid(curDeltaX, curDeltaY, tyle_board)) {
				updateSpeed(0);
			}
		}
		updateX(curDeltaX);
		updateY(curDeltaY);
	}
	
	public boolean getIfValid(int dx, int dy, Tyle[][] tyle_board) {
		if (tyle_board[getY() / getDimension()][getX() / getDimension()].type == TyleType.TELEPORT) {
			teleport(tyle_board[getY() / getDimension()][getX() / getDimension()], tyle_board);
		}

		if (isValid(dx, dy, tyle_board)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isValid(int dx, int dy, Tyle[][] tyle_board) {
		int column = getX() / dimension + dx;
		int row = getY() / dimension + dy;
		
		int num_columns = tyle_board[0].length, num_rows = tyle_board.length;
		if (column < 0 || column >= num_columns || row < 0 || row >= num_rows)
			return false;
		if (tyle_board[row][column].type == TyleType.UNREACHABLE || tyle_board[row][column].type == TyleType.WALL)
			return false;
		if (tyle_board[row][column].type == TyleType.GHOSTGATE)
			return false;
		return true;
	}

	
	public void updateDeltaX(int deltaX) {
		curDeltaX = deltaX;
	}

	public void updateDeltaY(int deltaY) {
		curDeltaY = deltaY;
	}

	public void updateX(int deltaX) {
		x += curDeltaX * curSpeed;
	}

	public void updateY(int deltaY) {
		y += curDeltaY * curSpeed;
	}

	public void resetX(int x) {
		this.x = x;
	}

	public void resetY(int y) {
		this.y = y;
	}

	public void updateSpeed(int speed) {
		curSpeed = speed;
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

	public double getSpeed() {
		return curSpeed;
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

	public int getDimension() {
		return dimension;
	}

	public int getStartCount() {
		return start_count;
	}
	
	public int getSpeedPercent() {
		return speed_percent;
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	
	public void changeVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
	
	public void setState(State state) {
		this.state = state;
	}

}
