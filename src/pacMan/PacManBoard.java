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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import pacMan.TyleContainer.Tyle;

public class PacManBoard extends JPanel implements KeyListener {


	public static int FPS = 16;//TEMP VARIABLE!!!!!!!!!!
	public static int TOTAL_DOTS = 0;
	public static int totalScore = 0;
	
	public static Scanner in = new Scanner(System.in);
	public static final int dimension = 16;

	private PacMan pacman;
	private Ghost[] ghosts = new Ghost[4];
	
	public static JFrame frame = new JFrame();
	private final ArrayList<String> board = new ArrayList<>();
	private Tyle[][] tyle_board;
	
	private InPlayScoreBoard inPlayScoreBoard = new InPlayScoreBoard();

	private List<int[]> powerup_pos = new ArrayList<int[]>();

	private int[] delta = {-1, 0};
	GridLayout bigBoard = new GridLayout(2,1);
	
	public void createBoard() throws FileNotFoundException {
		getBoard("textBoard.txt");
	}
	
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
		
		inPlayScoreBoard.drawScorePanel(g, this);
		inPlayScoreBoard.drawScore(g, this);
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
		
		if (pacman.getVisibility() == PacMan.Visibility.VISIBLE)
			g.drawImage(pacman.getImage(), pacman.getX() + xplus, pacman.getY() + yplus, width / 2, height / 2, this);
	}

	private void drawGhosts(Graphics g) {
		for (int i = 0; i < ghosts.length; i++) {
			Ghost ghost = ghosts[i];
			if (ghost.getVisibility() == Ghost.Visibility.NOT_VISIBLE)
				continue;
			int width = ghost.getImage().getWidth(this);
			int height = ghost.getImage().getHeight(this);
			int xplus = -(width / 2 - dimension) / 2;
			int yplus = -(height / 2 - dimension) / 2;

			g.drawImage(ghost.getImage(), ghost.getX() + xplus, ghost.getY() + yplus, width / 2, height / 2, this);
		}
	}

	private void setFrame(JFrame frame) {
		int xDimension = board.get(0).length() * dimension;
		int yDimension = board.size() * dimension + 32;

		frame.setSize(xDimension, yDimension);
		frame.getContentPane().add(this);
		frame.setLocationRelativeTo(null);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addKeyListener(this);
	}
	
	public void setTyleBoard() {
		int boardRows = board.size();
		int boardColumns = board.get(0).length();
		tyle_board = new Tyle[boardRows][boardColumns];
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardColumns; j++) {
				for (Tyle tyle : Tyle.values()) {
					if (tyle.c == board.get(i).charAt(j)) {
						tyle_board[i][j] = tyle;
						if (board.get(i).charAt(j) == 'o' || board.get(i).charAt(j) == '@' || board.get(i).charAt(j) == 'N')
							TOTAL_DOTS++;
					}
				}
			}
		}
	}
	
	private void setScorePanel() throws FileNotFoundException {
		inPlayScoreBoard.createScorePanel();
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
	
	private void setGhosts(Tyle[][] tyleBoard) {
		ghosts[0] = new Blinky(tyleBoard);
		ghosts[1] = new Pinky(tyleBoard);
		ghosts[2] = new Inky(tyleBoard);
		ghosts[3] = new Clyde(tyleBoard);
	}
	
	public void gameStartUp() throws FileNotFoundException {
		createBoard();
		setTyleBoard();
		setScorePanel();
		getPowerUpLocations(board.size(), board.get(0).length());
	}

	public void startGame() throws FileNotFoundException {
		pacman = new PacMan(tyle_board);
		setGhosts(tyle_board);
		setFrame(frame);
		
		CharacterEventHandler characterHandler = new CharacterEventHandler(60, pacman, ghosts, tyle_board);

		characterHandler.setCharacters();
		
		characterHandler.handleStart();

		while (PacManBoard.TOTAL_DOTS > 0) {

			characterHandler.postKeyPressEventHandler(delta);
			frame.repaint();
			
			sleep();

		}

	}
	
	public void resetGame() throws FileNotFoundException {
		TOTAL_DOTS = 0;
		totalScore = 0;
		inPlayScoreBoard = null;
		// frame.removeAll();
		
		inPlayScoreBoard = new InPlayScoreBoard();
		setScorePanel();
		setTyleBoard();
	}
	
	public static void sleep() {
		try {
			Thread.sleep(FPS, 666);
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