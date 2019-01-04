package pacMan;

public class HighScoreEntry {

	private int score;
	private String name;
	private long epochTime;
	
	public HighScoreEntry(int score, String name, long epochTime){
		this.score = score;
		this.name = name;
		this.epochTime = epochTime;
	}
	
	public int getScore(){
		return score;
	}
	
	public String getName(){
		return name;
	}
	
	public long getEpochTime(){
		return epochTime;
	}
}
