package pacMan;

import java.util.Timer;
import java.util.TimerTask;

import pacMan.DotTimer.FreeGhost;
import pacMan.Ghost.TargetingState;

public class GhostStateTimer {
	private Timer timer = new Timer();
	private boolean isScatter;
	private boolean isAttack;
	private boolean isFrightened;
	private TargetingState targetingState = TargetingState.SCATTER;
	
	private int scatter_sec = 7;
	private int attack_sec = 20;
	
	public boolean getIsScatter() {
		return isScatter;
	}
	
	public boolean getIsAttack() {
		return isAttack;
	}
	
	public TargetingState getTargetState() {
		return targetingState;
	}
	
	public void scheduleAttack() {
		timer = new Timer();
		timer.schedule(new SetToAttack(), scatter_sec * 1000);
	}
	
	public void scheduleScatter() {
		timer = new Timer();
		timer.schedule(new SetToScatter(), attack_sec * 1000);
	}
	
	class SetToAttack extends TimerTask {
		
		public void run() {
			isScatter = false;
			isAttack = true;
			targetingState = TargetingState.ATTACK;
			timer.cancel();
			scheduleScatter();
			System.out.println("set to 20");
		}
	}
	
	class SetToScatter extends TimerTask {
		
		public void run() {
			isAttack = false;
			isScatter = true;
			targetingState = TargetingState.SCATTER;
			timer.cancel();
			scheduleAttack();
			System.out.println("set to 7");
		}
	}
}
