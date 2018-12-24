package pacMan;

import java.awt.Toolkit;

public class Pinky extends Ghost {

	public Pinky(int x, int y) {
		super(GhostName.PINKY, State.DEFAULT, x, y);
	}

	public void resetGhost() {
		resetX(208);
		resetY(240);
	}

	public void ghostStart() {
		if (getStartCount() < 10) {
			updateSpeed(getSpeed());
			if (getY() == 16 * getDimension() - getDimension() / 2)
				updateDeltaY(-1);
			else if (getY() == 14 * getDimension() + getDimension() / 2) {
				updateDeltaY(1);
				updateStartCount(getStartCount() + 1);
			}
		} else {
			if (getY() != 12 * getDimension()) {
				updateDeltaX(0);
				updateDeltaY(-1);
			} else {
				updateDeltaX(-1);
				updateDeltaY(0);
				updateStartCount(-1);
				return;
			}
		}
		updateX(getDeltaX());
		updateY(getDeltaY());
	}

	public void rotateCharacter() {
		int[][] delta = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };

		for (int i = 0; i < 4; i++) {
			if (getDeltaX() == delta[i][0] && getDeltaY() == delta[i][1]) {
				String filename = getGhostName().filename_prefix + filename_appendix[i][image_frame / 5];
				character = Toolkit.getDefaultToolkit().getImage(filename);
			}
		}
	}

	public void rotateCharacterBlue() {
		blueTimer();
	}

	public void blueTimer() {
		blueFrame++;
		if (blueFrame < 800)
			this.character = Toolkit.getDefaultToolkit().getImage("blue.png");
		if (blueFrame >= 800 && blueFrame < 1200 && blueFrame % 60 >= 0 && blueFrame % 60 < 35)
			this.character = Toolkit.getDefaultToolkit().getImage("blue_blink.png");
		if (blueFrame >= 800 && blueFrame < 1200 && blueFrame % 60 >= 35 && blueFrame % 60 < 60)
			this.character = Toolkit.getDefaultToolkit().getImage("blue_blink182.png");
		if (blueFrame == 1200) {
			updateState(State.DEFAULT);
			blueFrame = 0;
		}
	}

}
