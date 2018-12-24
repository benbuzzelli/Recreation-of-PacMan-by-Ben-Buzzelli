package pacMan;

import java.awt.Image;
import java.awt.Toolkit;

import pacMan.Ghost.State;
import pacMan.PacManBoard.Tyle;
import pacMan.PacManBoard.TyleType;

public class PacMan {

	public enum State {
		DEFAULT, POWERED
	}

	public enum Name {
		PACMAN;

		String filename_prefix = "pacman";

	}

	public Name name = Name.PACMAN;

	public static String[][] filename_appendix = { { "_closed.png" }, { "_up.png", "_up1.png", },
			{ "_down.png", "_down1.png", }, { "_left.png", "_left1.png", }, { "_right.png", "_right1.png", } };
	public Image character = Toolkit.getDefaultToolkit().getImage(name.filename_prefix + filename_appendix[0][0]);
	public int image_frame = 0;

	public boolean isBlueGhost;
	public int blueFrame = 0;
	private int y;
	private int x;
	private final int dimension = 16;
	private int curDeltaX = 0;
	private int curDeltaY = 0;
	private double speed = 2;
	private int start_count = 0;
	public int frame = 0;
	
	public PacMan(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void resetPacMan() {
		resetX(240);
		resetY(384);
	}
	
	public void ghostStart() {
		
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
	
	public void updateImage() {
		if (image_frame == 10)
			image_frame = 0;
		
		rotateCharacter();
		
		image_frame++;
	}

	public void rotateCharacter() {

		int[][] delta = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };

		for (int i = 1; i < 5; i++) {
			if (getDeltaX() == delta[i-1][0] && getDeltaY() == delta[i-1][1]) {
				String filename = new String();
				if (speed != 0)
					filename = name.filename_prefix + filename_appendix[i][image_frame / 5];
				else
					filename = name.filename_prefix + filename_appendix[i][0];
				character = Toolkit.getDefaultToolkit().getImage(filename);
			}
			else
				character = Toolkit.getDefaultToolkit().getImage(name.filename_prefix + filename_appendix[0][0]);
		}
	}

	public void updatePacMan(int deltaX, int deltaY, PacManBoard.Tyle[][] tyle_board) {
		int[] delta = new int[2];
		delta = getMove(deltaX, deltaY, tyle_board);
		System.out.println("dx = " + delta[0] + " dy = " + delta[1]);
		updateDeltaX(delta[0]);
		updateDeltaY(delta[1]);
		updateX(curDeltaX);
		updateY(curDeltaY);
		System.out.println("x = " + x + " y = " + y + " SPEED: " + speed);
	}

	public int[] getMove(int deltaX, int deltaY, PacManBoard.Tyle[][] tyle_board) {
		System.out.println(" SPEED: " + speed);
		int[] delta = new int[2];
		PacManBoard.Tyle typeCurrent = getNextTyle(getDeltaX(), getDeltaY(), tyle_board);
		PacManBoard.Tyle type = getNextTyle(deltaX, deltaY, tyle_board);
		if (type != null && type.type != PacManBoard.TyleType.WALL && type.type != PacManBoard.TyleType.GHOSTGATE
				&& type.type != PacManBoard.TyleType.UNREACHABLE) {
			teleport(type, tyle_board);
			delta[0] = deltaX;
			delta[1] = deltaY;
			updateSpeed(speed);
			return delta;
		}
		System.out.println(" SPEED: " + speed);
		if (typeCurrent != null && typeCurrent.type != PacManBoard.TyleType.WALL
				&& typeCurrent.type != PacManBoard.TyleType.GHOSTGATE
				&& typeCurrent.type != PacManBoard.TyleType.UNREACHABLE) {
			teleport(typeCurrent, tyle_board);
			delta[0] = getDeltaX();
			delta[1] = getDeltaY();
			updateSpeed(speed);
			return delta;
		}
		System.out.println(" SPEED: " + speed);
		delta[0] = getDeltaX();
		delta[1] = getDeltaY();
		updateSpeed(0);
		System.out.println(" SPEED: " + speed);
		return delta;
	}

	public PacManBoard.Tyle getNextTyle(int deltaX, int deltaY, PacManBoard.Tyle[][] tyle_board) {
		int tempX = getX();
		int tempY = getY();
		int dimension = getDimension();

		if (deltaX == 0 && deltaY == 0)
			return null;
		if (deltaX == 0 && getX() % dimension == 0) {
			while (tempY == getY() || tempY % dimension != 0) {
				tempY += deltaY;
			}
		} else if (deltaY == 0 && getY() % dimension == 0) {
			while (tempX == getX() || tempX % dimension != 0) {
				tempX += deltaX;
			}
		} else
			return null;
		return tyle_board[tempY / dimension][tempX / dimension];
	}

	public void updateDeltaX(int deltaX) {
		this.curDeltaX = deltaX;
	}

	public void updateDeltaY(int deltaY) {
		this.curDeltaY = deltaY;
	}

	public void updateX(int deltaX) {
		x += curDeltaX * speed;
	}

	public void updateY(int deltaY) {
		y += curDeltaY * speed;
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

	public int getStartCount() {
		return start_count;
	}

}
