package pacMan;

import java.awt.Image;
import java.awt.Toolkit;

import pacMan.PacManBoard.Tyle;
import pacMan.PacManBoard.TyleType;

public abstract class Character {
	public enum State {
		DEFAULT, POWERED
	}

	public enum Name {
		PACMAN;
		
		String filename_prefix = "pacman";

	}

	public Name name = Name.PACMAN;
	
	public static String[][] filename_appendix = {{"_closed.png"}, { "_up.png", "_up1.png", }, { "_down.png", "_down1.png", },
			{ "_left.png", "_left1.png", }, { "_right.png", "_right1.png", } };
	public Image character = Toolkit.getDefaultToolkit().getImage(name.filename_prefix + filename_appendix[0]);
	public boolean isBlueGhost;
	public int blueFrame = 0;
	private int y;
	private int x;
	private final int dimension = 16;
	private int curDeltaX = 0;
	private int curDeltaY = 0;
	private double speed = 0;
	private int start_count = 0;
	public int frame = 0;

	public abstract void updateImage();

	public abstract void rotateCharacter();

	public abstract void updatePacMan(int dx, int dy, PacManBoard.Tyle[][] tyle_board);

	public Character(Name name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.curDeltaX = 0;
		this.curDeltaY = 0;
	}

	public void resetPacMan() {
		resetX(240);
		resetY(384);
	}

	public void updateDots(PacManBoard.Tyle[][] tyle_board) {
		if (name == Name.PACMAN && tyle_board[y / dimension][x / dimension].type == TyleType.DOT)
			tyle_board[y / dimension][x / dimension] = Tyle.BLACK_SQUARE;
	}

	public boolean updateBlueState(boolean isBlue, PacManBoard.Tyle[][] tyle_board) {
		if (isBlue) {
			if (name != Name.PACMAN) {
				isBlueGhost = true;
			}
		} else
			isBlueGhost = false;
		if (name == Name.PACMAN && tyle_board[y / dimension][x / dimension].type == TyleType.POWERUP) {
			tyle_board[y / dimension][x / dimension] = Tyle.POWERUP_USED;
			isBlueGhost = true;
			isBlue = true;
		}

		return isBlue;
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

	public void updateSpeed(double speed) {
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

	public double getSpeed() {
		return speed;
	}

	public Image getImage() {
		return character;
	}

	public int getDimension() {
		return dimension;
	}

	public Name getName() {
		return name;
	}

	public int getStartCount() {
		return start_count;
	}

}
