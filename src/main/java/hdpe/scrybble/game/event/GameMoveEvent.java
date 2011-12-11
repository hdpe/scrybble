package hdpe.scrybble.game.event;

import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerMove;

/**
 * @author Ryan Pickett
 *
 */
public class GameMoveEvent extends GameTurnEvent {

	private PlayerMove move;
	
	/**
	 * @param game
	 * @param turn
	 * @param player
	 * @param move
	 */
	public GameMoveEvent(Game game, int turn, Player player, PlayerMove move) {
		super(game, turn, player);
		this.move = move;
	}

	/**
	 * @return
	 */
	public PlayerMove getMove() {
		return move;
	}
}