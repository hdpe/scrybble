package hdpe.scrybble.game.event;

import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.IllegalMoveException;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerMove;

/**
 * @author Ryan Pickett
 *
 */
public class GameIllegalMoveEvent extends GameMoveEvent {

	private IllegalMoveException exception;
	
	/**
	 * @param game
	 * @param turn
	 * @param player
	 * @param move
	 * @param exception
	 */
	public GameIllegalMoveEvent(Game game, int turn, Player player, PlayerMove move, 
			IllegalMoveException exception) {
		super(game, turn, player, move);
		this.exception = exception;
	}

	/**
	 * @return
	 */
	public IllegalMoveException getException() {
		return exception;
	}
}