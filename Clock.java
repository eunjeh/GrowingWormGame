
public class Clock {
	//하나의 주기를 만드는 milliseconds의 수
	private float millisPerCycle;
	
	private long lastUpdate;
	
	private int elapsedCycles;
	
	private float excessCycles;
	
	private boolean isPaused;
	
	public Clock(float cyclesPerSecond) {
		setCyclesPerSecond(cyclesPerSecond);
		reset();
	}
	
	public void setCyclesPerSecond(float cyclesPerSecond) {
		this.millisPerCycle=(1.0f/cyclesPerSecond)*1000;
		//System.out.println("millisPerCycle: "+millisPerCycle);
	}
	
	public void reset() {
		this.elapsedCycles=0;
		this.excessCycles=0.0f;
		this.lastUpdate=getCurrentTime();
		this.isPaused=false;
	}
	
	public void update() {
		long currUpdate=getCurrentTime();
		//System.out.println("currUpdate: "+currUpdate+" lastUpdate: "+lastUpdate+" excessCycles: "+excessCycles);
		float delta=(float)(currUpdate-lastUpdate)+excessCycles;
		//System.out.println("delta=(currUpdate-lastUpdate)+excessCycles");
		//System.out.println("delta: "+delta);
		
		if(!isPaused) {
			this.elapsedCycles+=(int)Math.floor(delta/millisPerCycle);
			this.excessCycles=delta%millisPerCycle;
		}
		
		//현재의 update 시간을 last update 시간으로 전환
		this.lastUpdate=currUpdate;
	}
	
	public void setPaused(boolean paused) {
		this.isPaused=paused;
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public boolean hasElapsedCycle() {
		if(elapsedCycles>0) {
			this.elapsedCycles--;
			return true;
		}
		return false;
	}
	
	public boolean peekElapsedCycle() {
		return (elapsedCycles>0);
	}
	
	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L);
	}

}
