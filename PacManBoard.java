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
		INSIDE_WALL_RIGHT("iw_r.png", '0', TyleType.WALL), INSIDE_WALL_TOP_RIGHT("iw_tr.png", '1', TyleType.WALL),
		INSIDE_WALL_TOP("iw_t.png", '2', TyleType.WALL), INSIDE_WALL_TOP_LEFT("iw_tl.png", '3', TyleType.WALL),
		INSIDE_WALL_LEFT("iw_l.png", '4', TyleType.WALL), INSIDE_WALL_BOTTOM_LEFT("iw_bl.png", '5', TyleType.WALL),
		INSIDE_WALL_BOTTOM("iw_b.png", '6', TyleType.WALL), INSIDE_WALL_BOTTOM_RIGHT("iw_br.png", '7', TyleType.WALL),

		CONCAVE_WALL_TOP_RIGHT("cw_tr.png", '}', TyleType.WALL), CONCAVE_WALL_TOP_LEFT("cw_tl.png", '{', TyleType.WALL),
		CONCAVE_WALL_BOTTOM_LEFT("cw_bl.png", '(', TyleType.WALL),
		CONCAVE_WALL_BOTTOM_RIGHT("cw_br.png", ')', TyleType.WALL),

		OUTSIDE_WALL_RIGHT("ow_r.png", '8', TyleType.WALL), OUTSIDE_WALL_TOP_RIGHT("ow_tr.png", '9', TyleType.WALL),
		OUTSIDE_WALL_TOP("ow_t.png", 'a', TyleType.WALL), OUTSIDE_WALL_TOP_LEFT("ow_tl.png", 'b', TyleType.WALL),
		OUTSIDE_WALL_LEFT("ow_l.png", 'c', TyleType.WALL), OUTSIDE_WALL_BOTTOM_LEFT("ow_bl.png", 'd', TyleType.WALL),
		OUTSIDE_WALL_BOTTOM("ow_b.png", 'e', TyleType.WALL), OUTSIDE_WALL_BOTTOM_RIGHT("ow_br.png", 'f', TyleType.WALL),

		OUTSIDE_CONCAVE_WALL_TOP_TOP_RIGHT("ocw_t_tr.png", 'k', TyleType.WALL),
		OUTSIDE_CONCAVE_WALL_TOP_TOP_LEFT("ocw_t_tl.png", 'l', TyleType.WALL),
		OUTSIDE_CONCAVE_WALL_LEFT_TOP_LEFT("ocw_l_tl.png", 'n', TyleType.WALL),
		OUTSIDE_CONCAVE_WALL_LEFT_BOTTOM_LEFT("ocw_l_bl.png", 'm', TyleType.WALL),
		OUTSIDE_CONCAVE_WALL_RIGHT_BOTTOM_RIGHT("ocw_r_br.png", 'p', TyleType.WALL),
		OUTSIDE_CONCAVE_WALL_RIGHT_TOP_RIGHT("ocw_r_tr.png", 'q', TyleType.WALL),

		GHOST_WALL_RIGHT("gw_r.png", 'r', TyleType.WALL), GHOST_WALL_TOP_RIGHT("gw_tr.png", 's', TyleType.WALL),
		GHOST_WALL_TOP("gw_t.png", 't', TyleType.WALL), GHOST_WALL_TOP_LEFT("gw_tl.png", 'u', TyleType.WALL),
		GHOST_WALL_LEFT("gw_l.png", 'v', TyleType.WALL), GHOST_WALL_BOTTOM_LEFT("gw_bl.png", 'w', TyleType.WALL),
		GHOST_WALL_BOTTOM("gw_b.png", 'x', TyleType.WALL), GHOST_WALL_BOTTOM_RIGHT("gw_br.png", 'y', TyleType.WALL),
		GHOST_WALL_RIGHT_GATE("gw_r_gate.png", '[', TyleType.WALL),
		GHOST_WALL_LEFT_GATE("gw_l_gate.png", ']', TyleType.WALL),

		OUT_OF_BOUNDS("out_of_bounds.png", '#', TyleType.UNREACHABLE),
		BLACK_SQUARE("black_square.png", '+', TyleType.EMPTY),
		TELEPORT_SQUARE_A("teleport_square.png", 'A', TyleType.TELEPORT),
		TELEPORT_SQUARE_B("teleport_square.png", 'B', TyleType.TELEPORT),
		BLACK_GHOST_SQUARE("black_ghost_square.png", '_', TyleType.GHOSTSPACE),
		DOWN_ONLY_SQUARE("black_square.png", '^', TyleType.EMPTY), DOT_SQUARE("dot_square.png", 'o', TyleType.DOT),
		POWERUP_USED("black_square.png", '$', TyleType.EMPTY), POWERUP("powerup.png", '@', TyleType.POWERUP),
		POWERUP_BLINKED("black_square.png", '!', TyleType.POWERUP),
		GHOST_GATE("ghost_gate.png", '=', TyleType.GHOSTGATE);

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

	private final int dimension = 16;

	private int curFrame = -1;
	private boolean isPowered;

	private PacMan pacman = new PacMan(240, 384);
	private Ghost[] ghosts = {new Clyde(280,232),
							  new Inky(216, 232),
							  new Blinky(248, 192),
							  new Pinky(248, 248)
							 };
	private Ghost clyde = ghosts[0];
	private Ghost inky = ghosts[1];
	private Ghost blinky = ghosts[2];
	private Ghost pinky = ghosts[3];

	private ScoreBoard scoreboard = new ScoreBoard();
	private JFrame frame = new JFrame();
	private final ArrayList<String> board = new ArrayList<>();
	private Tyle[][] tyle_board;

	private List<int[]> powerup_pos = new ArrayList<int[]>();

	private int[] delta = new int[2];

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
		g.drawImage(clyde.getImage(), clyde.getX() + xplus, clyde.getY() + yplus, width / 2, height / 2, this);
	}
	
	private void drawGhosts(Graphics g){
		for(int i =0;i<ghosts.length;i++){
			Ghost ghost = ghosts[i];
			int width = ghost.getImage().getWidth(this);
			int height = ghost.getImage().getHeight(this);
			int xplus = -(width / 2 - dimension) / 2;
			int yplus = -(height / 2 - dimension) / 2;

			g.drawImage(ghost.getImage(), ghost.getX() + xplus, ghost.getY() + yplus, width / 2, height / 2, this);
		}
	}

	/*
	private void drawClyde(Graphics g) {
		int width = clyde.getImage().getWidth(this);
		int height = clyde.getImage().getHeight(this);
		int xplus = -(width / 2 - dimension) / 2;
		int yplus = -(height / 2 - dimension) / 2;

		g.drawImage(clyde.getImage(), clyde.getX() + xplus, clyde.getY() + yplus, width / 2, height / 2, this);
	}

	private void drawInky(Graphics g) {
		int width = inky.getImage().getWidth(this);
		int height = inky.getImage().getHeight(this);
		int xplus = -(width / 2 - dimension) / 2;
		int yplus = -(height / 2 - dimension) / 2;

		g.drawImage(inky.getImage(), inky.getX() + xplus, inky.getY() + yplus, width / 2, height / 2, this);
	}

	private void drawPinky(Graphics g) {
		int width = pinky.getImage().getWidth(this);
		int height = pinky.getImage().getHeight(this);
		int xplus = -(width / 2 - dimension) / 2;
		int yplus = -(height / 2 - dimension) / 2;

		g.drawImage(pinky.getImage(), pinky.getX() + xplus, pinky.getY() + yplus, width / 2, height / 2, this);
	}

	private void drawBlinky(Graphics g) {
		int width = blinky.getImage().getWidth(this);
		int height = blinky.getImage().getHeight(this);
		int xplus = -(width / 2 - dimension) / 2;
		int yplus = -(height / 2 - dimension) / 2;

		g.drawImage(blinky.getImage(), blinky.getX() + xplus, blinky.getY() + yplus, width / 2, height / 2, this);
	}
	*/

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

	private void setLayeredPane(JLayeredPane layeredPane) {
		Dimension layeredPaneSize = new Dimension(512, 576);
		layeredPane.setPreferredSize(layeredPaneSize);
		addText(layeredPane);
	}

	private void addText(JLayeredPane layeredPane) {

		JLabel score = new JLabel();
		score = scoreboard.score();

		layeredPane.add(score, 1000);
		layeredPane.moveToFront(score);
	}

	private void combinePanes(JFrame frame) {
		JLayeredPane layeredPane = new JLayeredPane();
		setLayeredPane(layeredPane);
		frame.add(layeredPane);
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

	public void blinkPowerUps() {
		if (curFrame % 200 == 0) {
			for (int i = 0; i < powerup_pos.size(); i++) {
				if (tyle_board[powerup_pos.get(i)[0]][powerup_pos.get(i)[1]] == Tyle.POWERUP) {
					tyle_board[powerup_pos.get(i)[0]][powerup_pos.get(i)[1]] = Tyle.POWERUP_BLINKED;
				} else if (tyle_board[powerup_pos.get(i)[0]][powerup_pos.get(i)[1]] == Tyle.POWERUP_BLINKED) {
					tyle_board[powerup_pos.get(i)[0]][powerup_pos.get(i)[1]] = Tyle.POWERUP;
				}
			}
		}
	}

	public void createBoard() throws FileNotFoundException {
		getBoard("textBoard.txt");
	}

	public void startGame() throws FileNotFoundException {
		createBoard();
		setTyleBoard(board.size(), board.get(0).length());
		getPowerUpLocations(board.size(), board.get(0).length());
		setFrame(frame);

		clyde.setImage();
		inky.setImage();
		pinky.setImage();
		blinky.setImage();
		clyde.updateImage();
		inky.updateImage();
		pinky.updateImage();
		blinky.updateImage();
		pacman.updateImage();

		while (true) {
			
			if (pacman.getStartCount() != -1)
				pacman.pacmanStart();
			else {
				//pacman.update(delta[0], delta[1], tyle_board);
				pacman.updatePacMan(delta[0], delta[1], tyle_board);
				pacman.updateImage();
			}

			if (pacman.getDeltaX() != 0 || pacman.getDeltaY() != 0) {
				if (clyde.getStartCount() != -1) {
					clyde.ghostStart();
					clyde.updateImage();
				}
				if (inky.getStartCount() != -1) {
					inky.ghostStart();
					inky.updateImage();
				}
				if (pinky.getStartCount() != -1) {
					pinky.ghostStart();
					pinky.updateImage();
				}
				if (blinky.getStartCount() != -1) {
					blinky.ghostStart();
					blinky.updateImage();
				}
				curFrame++;
				if (curFrame == 20)
					curFrame = 0;
				blinkPowerUps();

				if (clyde.getStartCount() == -1) {
					clyde.updateMove(pacman, tyle_board);
					clyde.updateImage();
				}
				if (inky.getStartCount() == -1) {
					inky.updateMove(pacman, tyle_board);
					inky.updateImage();
				}
				if (pinky.getStartCount() == -1) {
					pinky.updateMove(pacman, tyle_board);
					pinky.updateImage();
				}
				if (blinky.getStartCount() == -1) {
					blinky.updateMove(pacman, tyle_board);
					blinky.updateImage();
				}
			}

			pacman.updateBlueState(isPowered, tyle_board);
			clyde.checkCollision(pacman);
			inky.checkCollision(pacman);
			pinky.checkCollision(pacman);
			blinky.checkCollision(pacman);
			pacman.updateDots(tyle_board);

			frame.repaint();
			pacman.frame++;
			if (pacman.frame == 30)
				pacman.frame = 0;
			try {
				Thread.sleep(16, 666);
				// Thread.sleep(100, 666);
			} catch (InterruptedException e) {

			}

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
