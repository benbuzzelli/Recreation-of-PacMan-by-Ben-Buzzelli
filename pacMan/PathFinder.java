package pacMan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacMan.PacManBoard.Tyle;
import pacMan.PacManBoard.TyleType;

public class PathFinder {
	
	private Random rand = new Random();
		
	public int[] getGhostDelta(int dimension, Character pacman, Character ghost, PacManBoard.Tyle[][] tyle_board) {
		List<int[]> move = new ArrayList<int[]>();
		int min_distance = 100000;
		int index = 0;
		move = validMoves(dimension, ghost, tyle_board);
		
		
		if (move.size() == 0)
			return null;
		if (tyle_board[ghost.getY()/dimension][ghost.getX()/dimension].type != TyleType.GHOSTSPACE) {
			for (int i = 0; i < move.size(); i++) {
				//System.out.println("dx = " + move.get(i)[0] + " dy = " + move.get(i)[1]);
				if (getDistance(pacman, ghost, move.get(i)) < min_distance) {
					index = i;
					min_distance = getDistance(pacman, ghost, move.get(i));
				}
			}
		}
		else {
			index = rand.nextInt(move.size());
		}
		
		
		return move.get(index);
	}
	
	private int getDistance(Character pacman, Character ghost, int[] move) {
		
		int row1 = ghost.getY() + move[1], row2 = pacman.getY();
		int col1 = ghost.getX() + move[0], col2 = pacman.getX();
		
		int distance = Math.abs(row2 - row1) + Math.abs(col2 - col1);
		return distance;
	}
	
	public boolean validMove(int dx, int dy, PacManBoard.Tyle[][] tyle_board) {
		return false;
	}
	
	private List<int[]> validMoves(int dimension, Character ghost, PacManBoard.Tyle[][] tyle_board) {
		List<int[]> move = new ArrayList<int[]>();
		int row = ghost.getY();
		int column = ghost.getX();
		
		if (ghost.getDeltaY() == 0) {
			if (isFree(0, ghost.getDeltaX(), row, column + dimension*ghost.getDeltaX(), tyle_board, dimension)) {
				int[] delta = new int[2];
				delta[0] = ghost.getDeltaX();
				delta[1] = 0;
				move.add(delta);
			}
			if (isFree(1, 0, row+dimension, column, tyle_board, dimension)) {
				int[] delta = new int[2];
				delta[0] = 0;
				delta[1] = 1;
				move.add(delta);
			}
			if (isFree(-1, 0, row-dimension, column, tyle_board, dimension)) {
				int[] delta = new int[2];
				delta[0] = 0;
				delta[1] = -1;
				move.add(delta);
			}
		}
		
		if (ghost.getDeltaX() == 0) {
			if (isFree(ghost.getDeltaY(), 0, row + dimension*ghost.getDeltaY(), column, tyle_board, dimension)) {
				int[] delta = new int[2];
				delta[0] = 0;
				delta[1] = ghost.getDeltaY();
				move.add(delta);
			}
			if (isFree(0, 1, row, column+dimension, tyle_board, dimension)) {
				int[] delta = new int[2];
				delta[0] = 1;
				delta[1] = 0;
				move.add(delta);
			}
			if (isFree(0, -1, row, column-dimension, tyle_board, dimension)) {
				int[] delta = new int[2];
				delta[0] = -1;
				delta[1] = 0;
				move.add(delta);
			}
		}
		
		for (int i = 0; i < move.size(); i++) {
			if (move.get(i)[0] == 0 && move.get(i)[1] == 0)
				move.remove(i);
		}
		
		return move;
	}
	
	private boolean isFree(int dy, int dx, int y, int x, PacManBoard.Tyle[][] tyle_board, int dimension) {
		// System.out.println("x: " + x + " y: " + y);
		if (x%dimension != 0 && dy != 0)
			return false;
		if (y%dimension != 0 && dx != 0)
			return false;
		
		int row = y/dimension + dy;
		int col = x/dimension + dx;
		
		if (dy == -1)
			row = y/dimension;
		if (dx == -1)
			col = x/dimension;
		if (row < 0 || row >= tyle_board.length || col < 0 || col >= tyle_board[0].length)
			return false;
		
		// System.out.println("printing dx and dy :   " + dx + " " + dy);
		
		if (tyle_board[row][col].type == TyleType.UNREACHABLE)
			return false;
		if (tyle_board[row][col].type == TyleType.WALL)
			return false;
		if (tyle_board[row][col].type == TyleType.GHOSTGATE)
			System.out.println(tyle_board[row][col] + "  " + " row: " + row + " col: " + col + " DY = " + dy);
		if (tyle_board[row][col].type == TyleType.GHOSTGATE && dy == 1) {
			System.out.println(tyle_board[row][col] + "  " + " row: " + row + " col: " + col + " DY = " + dy);
			return false;
		}
		if (tyle_board[row][col] == Tyle.DOWN_ONLY_SQUARE && dy == -1) {
			System.out.println(tyle_board[row][col] + "  " + " row: " + row + " col: " + col + " DY = " + dy);
			return false;
		}
		return true;
	}
	
}
