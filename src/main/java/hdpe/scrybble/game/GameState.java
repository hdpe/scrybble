package hdpe.scrybble.game;

import java.util.List;

/**
 * @author Ryan Pickett
 *
 */
public interface GameState {

	/**
	 * @return
	 */
	BoardState getBoard();
	
	/**
	 * @return
	 */
	int getPlayersRemaining();
	
	/**
	 * @return
	 */
	int getTilesRemaining();
	
	/**
	 * @return
	 */
	boolean isFinished();

	/**
	 * @return
	 */
	int getTurn();
	
	/**
	 * @return
	 */
	int getRackSize();
	
	/**
	 * @return
	 */
	GameHistory getTurnHistory();

	/**
	 * @return
	 */
	List<PlayerState> getPlayers();
}