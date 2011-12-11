package hdpe.scrybble.game.event;

import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.Player;

/**
 * @author Ryan Pickett
 *
 */
public class GameFirstPlayerDeterminedEvent extends GameEvent {

	private Player player;
	
	/**
	 * @param game
	 * @param player
	 */
	public GameFirstPlayerDeterminedEvent(Game game, Player player) {
		super(game);
		this.player = player;
	}

	/**
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}
}