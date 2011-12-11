package hdpe.scrybble.game.event;

import hdpe.scrybble.game.Game;

/**
 * @author Ryan Pickett
 *
 */
public class GameStartEvent extends GameEvent {

	/**
	 * @param game
	 */
	public GameStartEvent(Game game) {
		super(game);
	}

}
