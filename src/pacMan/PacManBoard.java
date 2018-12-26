package pacMan;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PacManBoard extends JPanel implements KeyListener {

	public enum TyleType {
		WALL, UNREACHABLE, EMPTY, TELEPORT, DOT, POWERUP, GHOSTSPACE, GHOSTGATE;
	}

	public enum Tyle {
		INSIDE_WALL_RIGHT("images/iw_r.png", '0', TyleType.WALL), INSIDE_WALL_TOP_RIGHT("images/iw_tr.png", '1', TyleType.WALL),
		INSIDE_WALL_TOP("images/iw_t.png", '2', TyleType.WALL), INSIDE_WALL_TOP_LEFT("images/iw_tl.png", '3', TyleType.WALL),
		INSIDE_WALL_LEFT("images/iw_l.png", '4', TyleType.WALL), INSIDE_WALL_BOTTOM_LEFT("images/iw_bl.png", '5', TyleType.WALL),
		INSIDE_WALL_BOTTOM("images/iw_b.png", '6', TyleType.WALL), INSIDE_WALL_BOTTOM_RIGHT("images/iw_br.png", '7', TyleType.WALL),

		CONCAVE_WALL_TOP_RIGHT("images/cw_tr.png", '}', TyleType.WALL), CONCAVE_WALL_TOP_LEFT("images/cw_tl.png", '{', TyleType.WALL),
		CONCAVE_WALL_BOTTOM_LEFT("images/cw_bl.png", '(', TyleType.WALL),
		CONCAVE_WALL_BOTTOM_RIGHT("images/cw_br.png", ')', TyleType.WALL),

		OUTSIDE_WALL_RIGHT("images/ow_r.png", '8', TyleType.WALL), OUTSIDE_WALL_TOP_RIGHT("images/ow_tr.png", '9', TyleType.WALL),
		OUTSIDE_WALL_TOP("images/ow_t.png", 'a', TyleType.WALL), OUTSIDE_WALL_TOP_LEFT("images/ow_tl.png", 'b', TyleType.WALL),
		OUTSIDE_WALL_LEFT("images/ow_l.png", 'c', TyleType.WALL), OUTSIDE_WALL_BOTTOM_LEFT("images/ow_bl.png", 'd', TyleType.WALL),
		OUTSIDE_WALL_BOTTOM("images/ow_b.png", 'e', TyleType.WALL), OUTSIDE_WALL_BOTTOM_RIGHT("images/ow_br.png", 'f', TyleType.WALL),

		OUTSIDE_CONCAVE_WALL_TOP_TOP_RIGHT("images/ocw_t_tr.png", 'k', TyleType.WALL),
		OUTSIDE_CONCAVE_WALL_TOP_TOP_LEFT("images/ocw_t_tl.png", 'l', TyleType.WALL),
		OUTSIDE_CONCAVE_WALL_LEFT_TOP_LEFT("images/ocw_l_tl.png", 'n', TyleType.WALL),
		OUTSIDE_CONCAVE_WALL_LEFT_BOTTOM_LEFT("images/ocw_l_bl.png", 'm', TyleType.WALL),
		OUTSIDE_CONCAVE_WALL_RIGHT_BOTTOM_RIGHT("images/ocw_r_br.png", 'p', TyleType.WALL),
		OUTSIDE_CONCAVE_WALL_RIGHT_TOP_RIGHT("images/ocw_r_tr.png", 'q', TyleType.WALL),

		GHOST_WALL_RIGHT("images/w_r.png", 'r', TyleType.WALL), GHOST_WALL_TOP_RIGHT("images/gw_tr.png", 's', TyleType.WALL),
		GHOST_WALL_TOP("images/gw_t.png", 't', TyleType.WALL), GHOST_WALL_TOP_LEFT("images/gw_tl.png", 'u', TyleType.WALL),
		GHOST_WALL_LEFT("images/gw_l.png", 'v', TyleType.WALL), GHOST_WALL_BOTTOM_LEFT("images/gw_bl.png", 'w', TyleType.WALL),
		GHOST_WALL_BOTTOM("images/gw_b.png", 'x', TyleType.WALL), GHOST_WALL_BOTTOM_RIGHT("images/gw_br.png", 'y', TyleType.WALL),
		GHOST_WALL_RIGHT_GATE("images/gw_r_gate.png", '[', TyleType.WALL),
		GHOST_WALL_LEFT_GATE("images/gw_l_gate.png", ']', TyleType.WALL),

		OUT_OF_BOUNDS("images/out_of_bounds.png", '#', TyleType.UNREACHABLE),
		BLACK_SQUARE("images/black_square.png", '+', TyleType.EMPTY),
		TELEPORT_SQUARE_A("images/teleport_square.png", 'A', TyleType.TELEPORT),
		TELEPORT_SQUARE_B("images/teleport_square.png", 'B', TyleType.TELEPORT),
		TELEPORT_PATH("images/teleport_square.png", '?', TyleType.TELEPORT),
		BLACK_GHOST_SQUARE("images/black_ghost_square.png", '_', TyleType.GHOSTSPACE),
		DOWN_ONLY_SQUARE("images/black_square.png", '^', TyleType.EMPTY), DOT_SQUARE("images/dot_square.png", 'o', TyleType.DOT),
		POWERUP_USED("images/black_square.png", '$', TyleType.EMPTY), POWERUP("images/powerup.png", '@', TyleType.POWERUP),
		POWERUP_BLINKED("images/black_square.png", '!', TyleType.POWERUP),
		GHOST_GATE("images/ghost_gate.png", '=', TyleType.GHOSTGATE);

		public String filename;
		public char c;
		public TyleType type;

		Tyle(String filename, char c, TyleType type) {
			this.filename = filename;
			this.c = c;
			this.type = type;
		}
	}

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
	
	private void setGhosts(PacManBoard.Tyle[][] tyleBoard) {
		ghosts[0] = new Clyde(280, 232, tyleBoard);
		ghosts[1] = new Inky(216, 232, tyleBoard);
		ghosts[2] = new Blinky(248, 192, tyleBoard);
		ghosts[3] = new Pinky(248, 248, tyleBoard);
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

	public static void main(String[] args) throws FileNotFoundException {
		PacManBoard pacman_board = new PacManBoard();
		pacman_board.startGame();
	}

}