package hdpe.scrybble.game.event;

import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.GameResult;

/**
 * @author Ryan Pickett
 *
 */
public class GameResultEvent extends GameEvent {
	
	private GameResult result;

	/**
	 * @param game
	 * @param result
	 */
	public GameResultEvent(Game game, GameResult result) {
		super(game);
		this.result = result;
	}

	/**
	 * @return
	 */
	public GameResult getResult() {
		return result;
	}
	
	
}
