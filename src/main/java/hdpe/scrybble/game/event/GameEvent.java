package hdpe.scrybble.game.event;

import hdpe.scrybble.game.Game;

/**
 * @author Ryan Pickett
 *
 */
public abstract class GameEvent {
	
	private Game game;
	
	/**
	 * @param game
	 */
	protected GameEvent(Game game) {
		this.game = game;
	}
	
	/**
	 * @return
	 */
	public Game getGame() {
		return game;
	}
}