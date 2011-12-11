package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public interface BoardLayout {

	/**
	 * @return
	 */
	int getWidth();
	
	/**
	 * @return
	 */
	int getHeight();
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	int getLetterMultiplier(int x, int y);
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	int getWordMultiplier(int x, int y);
}
