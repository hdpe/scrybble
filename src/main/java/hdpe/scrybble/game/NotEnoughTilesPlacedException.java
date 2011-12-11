package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public class NotEnoughTilesPlacedException extends IllegalMoveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -30929847848796499L;

	NotEnoughTilesPlacedException(PlayerMove move) {
		super("Must place at least 1 tile (2 on first go)", move);
	}

}
