package hdpe.scrybble.game;

import java.util.Collection;

/**
 * @author Ryan Pickett
 *
 */
public class HistoryTurn {

	private Player player;
	private PlayerMove move;
	private Collection<Tile> pickup;
	private long elapsed;
	
	HistoryTurn(Player player, PlayerMove move, Collection<Tile> pickup,
			long elapsed) {
		this.player = player;
		this.move = move;
		this.pickup = pickup;
		this.elapsed = elapsed;
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
	public PlayerMove getMove() {
		return move;
	}

	/**
	 * @return
	 */
	public Collection<Tile> getPickup() {
		return pickup;
	}

	/**
	 * @return
	 */
	public long getElapsed() {
		return elapsed;
	}
}