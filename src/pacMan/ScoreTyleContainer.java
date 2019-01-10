package pacMan;

public class ScoreTyleContainer {

	public enum ScoreTyle {
		BLACK_SPACE("images_score_panel/black_square.png", '#'),
		LETTER_H("images_score_panel/H.png", 'H'),
		LETTER_I("images_score_panel/I.png", 'I'),
		LETTER_G("images_score_panel/G.png", 'G'),
		LETTER_S("images_score_panel/S.png", 'S'),
		LETTER_C("images_score_panel/C.png", 'C'),
		LETTER_O("images_score_panel/O.png", 'O'),
		LETTER_R("images_score_panel/R.png", 'R'),
		LETTER_E("images_score_panel/E.png", 'E'),
		LETTER_U("images_score_panel/U.png", 'U'),		
		LETTER_P("images_score_panel/P.png", 'P'),
		LETTER_ONE("images_score_panel/1.png", '1');

		public String filename;
		public char c;
		
		ScoreTyle(String filename, char c) {
			this.filename = filename;
			this.c = c;
		}
	}
	
}
