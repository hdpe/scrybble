package hdpe.scrybble.game.event;

import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.Tile;

/**
 * @author Ryan Pickett
 *
 */
public class GameDrawStartTileEvent extends GameEvent {

	private Player player;
	private Tile tile;
	
	/**
	 * @param game
	 * @param player
	 * @param tile
	 */
	public GameDrawStartTileEvent(Game game, Player player, Tile tile) {
		super(game);
		this.player = player;
		this.tile = tile;
	}

	/**
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return
	 */
	public Tile getTile() {
		return tile;
	}
}