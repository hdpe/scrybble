package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public class IllegalTileLocationException extends IllegalMoveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7562635164285685067L;

	IllegalTileLocationException(String msg, PlayerMove move) {
		super(msg, move);
	}

}
