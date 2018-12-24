package pacMan;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class ScoreBoard {
	
	public JLabel score() {
		JLabel score = new JLabel("HELLO WORLD");
		Font font = new Font("Segoe Script", Font.BOLD, 20);
		score.setFont(font);
		score.setForeground(Color.BLUE);
		score.setAlignmentX(0);
		score.setAlignmentY(0);
		return score;
	}
}
