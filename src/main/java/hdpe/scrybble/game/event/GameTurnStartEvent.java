package hdpe.scrybble.game.event;

import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.Player;

/**
 * @author Ryan Pickett
 *
 */
public class GameTurnStartEvent extends GameTurnEvent {

	/**
	 * @param game
	 * @param turn
	 * @param player
	 */
	public GameTurnStartEvent(Game game, int turn, Player player) {
		super(game, turn, player);
	}

}
