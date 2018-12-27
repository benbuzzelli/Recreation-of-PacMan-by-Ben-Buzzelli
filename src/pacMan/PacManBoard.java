package pacMan;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import pacMan.TyleContainer.Tyle;

public class PacManBoard extends JPanel implements KeyListener {


	public static Scanner in = new Scanner(System.in);
	public static final int dimension = 16;

	private PacMan pacman = new PacMan(240, 384);
	private Ghost[] ghosts = new Ghost[4];
	
	private JFrame frame = new JFrame();
	private final ArrayList<String> board = new ArrayList<>();
	private Tyle[][] tyle_board;

	private List<int[]> powerup_pos = new ArrayList<int[]>();

	private int[] delta = {-1, 0};

	public void getBoard(String file_name) throws FileNotFoundException {
		File file = new File(file_name);
		Scanner in = new Scanner(file);

		while (in.hasNextLine()) {
			board.add(in.nextLine());
		}

		in.close();
	}

	public void paint(Graphics g) {

		drawGameBoard(g);

		drawPacMan(g);
		drawGhosts(g);
		drawGameBorder(g);
	}

	private void drawGameBoard(Graphics g) {
		int rows = board.size();
		int columns = board.get(0).length();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				Image piece = Toolkit.getDefaultToolkit().getImage(tyle_board[i][j].filename);
				g.drawImage(piece, dimension * j, dimension * i, dimension, dimension, this);
			}
		}
	}

	private void drawGameBorder(Graphics g) {
		int rows = board.size();
		int columns = board.get(0).length();
		for (int i = 0; i < rows; i++) {
			Image piece = Toolkit.getDefaultToolkit().getImage(tyle_board[i][0].filename);
			g.drawImage(piece, 0, dimension * i, dimension, dimension, this);
			Image piece2 = Toolkit.getDefaultToolkit().getImage(tyle_board[i][1].filename);
			g.drawImage(piece2, dimension, dimension * i, dimension, dimension, this);
			Image piece3 = Toolkit.getDefaultToolkit().getImage(tyle_board[i][columns - 1].filename);
			g.drawImage(piece3, (columns - 1) * dimension, dimension * i, dimension, dimension, this);
			Image piece4 = Toolkit.getDefaultToolkit().getImage(tyle_board[i][columns - 2].filename);
			g.drawImage(piece4, (columns - 2) * dimension, dimension * i, dimension, dimension, this);
		}
	}

	private void drawPacMan(Graphics g) {
		int width = pacman.getImage().getWidth(this);
		int height = pacman.getImage().getHeight(this);
		int xplus = -(width / 2 - dimension) / 2;
		int yplus = -(height / 2 - dimension) / 2;

		g.drawImage(pacman.getImage(), pacman.getX() + xplus, pacman.getY() + yplus, width / 2, height / 2, this);
	}

	private void drawGhosts(Graphics g) {
		for (int i = 0; i < ghosts.length; i++) {
			Ghost ghost = ghosts[i];
			int width = ghost.getImage().getWidth(this);
			int height = ghost.getImage().getHeight(this);
			int xplus = -(width / 2 - dimension) / 2;
			int yplus = -(height / 2 - dimension) / 2;

			g.drawImage(ghost.getImage(), ghost.getX() + xplus, ghost.getY() + yplus, width / 2, height / 2, this);
		}
	}

	private void setFrame(JFrame frame) {
		frame.setSize(512, 576);
		frame.getContentPane().add(this);
		frame.setLocationRelativeTo(null);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addKeyListener(this);
	}


	public void setTyleBoard(int boardRows, int boardColumns) {
		tyle_board = new Tyle[boardRows][boardColumns];
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardColumns; j++) {
				for (Tyle tyle : Tyle.values()) {
					if (tyle.c == board.get(i).charAt(j)) {
						tyle_board[i][j] = tyle;
					}
				}
			}
		}
	}

	public void getPowerUpLocations(int boardRows, int boardColumns) {
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardColumns; j++) {
				if (tyle_board[i][j] == Tyle.POWERUP) {
					int[] position = new int[2];
					position[0] = i;
					position[1] = j;
					powerup_pos.add(position);
				}
			}
		}
	}

	public void createBoard() throws FileNotFoundException {
		getBoard("textBoard.txt");
	}
	
	private void setGhosts(Tyle[][] tyleBoard) {
		ghosts[0] = new Blinky(248, 192, tyleBoard);
		ghosts[1] = new Pinky(248, 248, tyleBoard);
		ghosts[2] = new Inky(216, 232, tyleBoard);
		ghosts[3] = new Clyde(280, 232, tyleBoard);
	}

	public void startGame() throws FileNotFoundException {
		createBoard();
		setTyleBoard(board.size(), board.get(0).length());
		getPowerUpLocations(board.size(), board.get(0).length());
		setGhosts(tyle_board);
		setFrame(frame);
		
		CharacterEventHandler characterHandler = new CharacterEventHandler(60, pacman, ghosts, tyle_board);

		characterHandler.setCharacters();
		
		characterHandler.handleStart();

		while (true) {

			characterHandler.postKeyPressEventHandler(delta);

			frame.repaint();
			
			sleep();

		}

	}
	
	public static void sleep() {
		try {
			Thread.sleep(16, 666);
			// Thread.sleep(100, 666);
		} catch (InterruptedException e) {

		}
	}

	public void keyTyped(KeyEvent event) {

	}

	public void keyPressed(KeyEvent event) {
		delta[0] = 0;
		delta[1] = 0;

		if (event.getKeyCode() == KeyEvent.VK_UP) {
			delta[0] = 0;
			delta[1] = -1;
		} else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
			delta[0] = 0;
			delta[1] = 1;
		} else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
			delta[0] = -1;
			delta[1] = 0;
		} else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
			delta[0] = 1;
			delta[1] = 0;
		}
	}

	public void keyReleased(KeyEvent event) {

	}

}