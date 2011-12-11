package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public enum GameMode {

	/**
	 * 
	 */
	PLAY("Play"),
	
	/**
	 * 
	 */
	RECORD("Record");
	
	private String label;
	
	GameMode(String label) {
		this.label = label;
	}
	
	public String toString() {
		return label;
	}
}
