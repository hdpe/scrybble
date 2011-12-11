package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public class Clock {

	private long timeRemaining;
	
	private long startTimeMs;
	private boolean running;
	
	void start() {
		running = true;
		startTimeMs = System.currentTimeMillis();
	}
	
	long stop() {
		running = false;
		long elapsed = (System.currentTimeMillis() - startTimeMs);
		timeRemaining -= elapsed;
		return elapsed;
	}
	
	void decrement(long ms) {
		timeRemaining -= ms;
	}
	
	/**
	 * @return
	 */
	public long getTimeRemaining() {
		long time = timeRemaining;
		
		if (running) {
			time -= (System.currentTimeMillis() - startTimeMs);
		}
		
		return time;	
	}
	
	/**
	 * @param timeRemaining
	 */
	public void setTimeRemaining(int timeRemaining) {
		this.timeRemaining = timeRemaining;
	}
}