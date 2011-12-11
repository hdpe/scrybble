package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public interface FutureMove {

	/**
	 * @param game
	 * @return
	 */
	PlayerMove getPlayerMove(final Game game);
}
