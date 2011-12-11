package hdpe.scrybble.game.event;

import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.Player;

/**
 * @author Ryan Pickett
 *
 */
public abstract class GameTurnEvent extends GameEvent {

	private int turn;
	private Player player;
	
	/**
	 * @param game
	 * @param turn
	 * @param player
	 */
	protected GameTurnEvent(Game game, int turn, Player player) {
		super(game);
		this.turn = turn;
		this.player = player;
	}

	public int getTurn() {
		return turn;
	}

	public Player getPlayer() {
		return player;
	}
	
	
	
	
}
