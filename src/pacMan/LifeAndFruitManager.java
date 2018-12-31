package pacMan;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LifeAndFruitManager {

	private Image life = Toolkit.getDefaultToolkit().getImage("images/pacman_right.png");;
	private Image fruit;

	private int lifeColumn;
	private int fruitColumn;

	private ArrayList<String> input = new ArrayList<>();

	private int panelRow;
	private String life_fruit_panel;

	public void setValues() throws FileNotFoundException {
		getInput();
		setRowAndPanel();
		getLifeFruitLocation();
	}

	public void drawLifeAndFruit(Graphics g, PacManBoard pacman_board) {
		int dim = PacManBoard.dimension;
		int width = life.getWidth(pacman_board);
		int height = life.getHeight(pacman_board);
		int xplus = -(width / 2 - PacManBoard.dimension) / 2;
		int yplus = -(height / 2 - PacManBoard.dimension) / 2;

		for (int i = 0; i < PacManBoard.lives; i++) {
			g.drawImage(life, (lifeColumn * dim + xplus + dim) + 2*i * dim,
					panelRow * dim + yplus + dim, width / 2, height / 2,
					pacman_board);
		}
	}

	private void getLifeFruitLocation() {
		int length = life_fruit_panel.length();
		for (int i = 0; i < length; i++) {
			if (life_fruit_panel.charAt(i) == 'L')
				lifeColumn = i;
			if (life_fruit_panel.charAt(i) == 'F')
				fruitColumn = i;
		}
	}

	private void setRowAndPanel() {
		panelRow = Integer.parseInt(input.get(0));
		life_fruit_panel = input.get(1);
	}

	private void getInput() throws FileNotFoundException {
		File file = new File("life_fruit_panel.txt");
		Scanner in = new Scanner(file);

		while (in.hasNextLine()) {
			input.add(in.nextLine());
		}

		in.close();
	}

}
