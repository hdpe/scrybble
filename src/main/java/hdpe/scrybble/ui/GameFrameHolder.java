package hdpe.scrybble.ui;

/**
 * @author Ryan Pickett
 *
 */
public class GameFrameHolder {
	
	private static GameFrame INSTANCE;
	
	static void register(GameFrame rack) {
		INSTANCE = rack;
	}
	
	public static GameFrame getGameFrame() {
		return INSTANCE;
	}
}
