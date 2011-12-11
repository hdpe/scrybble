package hdpe.scrybble.game;


/**
 * @author Ryan Pickett
 *
 */
public interface PlayerStrategy {

	/**
	 * @param game
	 */
	void initGame(GameState game, PlayerState player);
	
	/**
	 * @param game
	 * @param rack
	 * @param dictionary
	 * @return
	 */
	PlayerMove getNextMove(GameState game, RackState rack, Dictionary dictionary);

	/**
	 * 
	 */
	void cancel();
}
