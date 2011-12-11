package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public interface SquareState {

	/**
	 * @return
	 */
	boolean hasTile();

	/**
	 * @return
	 */
	int getWordMultiplier();

	/**
	 * @return
	 */
	int getLetterMultiplier();

	/**
	 * @return
	 */
	boolean isClearAndAdjacentToTile();

	/**
	 * @param direction
	 * @return
	 */
	SquareState previous(WordDirection direction);

	/**
	 * @param direction
	 * @return
	 */
	SquareState next(WordDirection direction);

	/**
	 * @return
	 */
	TileState getTile();

	/**
	 * @return
	 */
	int getX();

	/**
	 * @return
	 */
	int getY();

}