package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public interface TileState {

	/**
	 * @return
	 */
	boolean isBlank();

	/**
	 * @return
	 */
	boolean isPlaced();
	
	/**
	 * @return
	 */
	char getLetter();

	/**
	 * @return
	 */
	int getValue();
}
