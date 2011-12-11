package hdpe.scrybble.game.event;

import hdpe.scrybble.game.Game;

/**
 * @author Ryan Pickett
 *
 */
public class GameStoppedEvent extends GameEvent {

	/**
	 * @param game
	 */
	public GameStoppedEvent(Game game) {
		super(game);
	}
}