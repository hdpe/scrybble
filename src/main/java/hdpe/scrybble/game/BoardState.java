package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public interface BoardState {
	
	/**
	 * @return
	 */
	SquareState getCentreSquare();
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	SquareState getSquare(int x, int y);
	
	/**
	 * @return
	 */
	int getHeight();
	
	/**
	 * @return
	 */
	int getWidth();
}