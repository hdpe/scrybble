package hdpe.scrybble.game;

import java.util.Collection;


/**
 * @author Ryan Pickett
 *
 */
public interface Player {
	
	/**
	 * @return
	 */
	Clock getClock();
	
	/**
	 * @return
	 */
	String getName();
	
	/**
	 * @param game
	 * @param dictionary
	 * @return
	 */
	PlayerMove getNextMove(GameState game, Dictionary dictionary);

	/**
	 * @return
	 */
	Rack getRack();
	
	/**
	 * @return
	 */
	int getScore();
	
	/**
	 * @return
	 */
	PlayerState getState();

	/**
	 * @return
	 */
	PlayerStrategy getStrategy();

	/**
	 * @return
	 */
	boolean hasResigned();
	
	/**
	 * @param score
	 */
	void incrementScore(int score);
	
	/**
	 * 
	 */
	void resign();
	
	/**
	 * @param playerName
	 */
	void setName(String playerName);
	
	/**
	 * @param strategy
	 */
	void setStrategy(PlayerStrategy strategy);
	
	/**
	 * @param game
	 * @param initialTiles
	 */
	void startGame(Game game, Collection<Tile> initialTiles);
}
