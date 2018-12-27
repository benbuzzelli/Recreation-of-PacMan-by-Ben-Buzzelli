package pacMan;

public class TyleContainer {

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

		GHOST_WALL_RIGHT("images/gw_r.png", 'r', TyleType.WALL), GHOST_WALL_TOP_RIGHT("images/gw_tr.png", 's', TyleType.WALL),
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
		
		// Ghost spawn locations
		INKY_SPAWN_TYLE("images/black_ghost_square.png", 'I', TyleType.GHOSTSPACE),
		PINKY_SPAWN_TYLE("images/black_ghost_square.png", 'P', TyleType.GHOSTSPACE),
		BLINKY_SPAWN_TYLE("images/black_square.png", 'L', TyleType.EMPTY),
		CLYDE_SPAWN_TYLE("images/black_ghost_square.png", 'C', TyleType.GHOSTSPACE),
		PACMAN_SPAWN_TYLE("images/dot_square.png", 'N', TyleType.DOT),
		
		GHOST_HOME_TYLE("images/black_ghost_square.png", 'H', TyleType.GHOSTSPACE),
		
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
}
