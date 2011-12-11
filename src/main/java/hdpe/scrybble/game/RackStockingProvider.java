package hdpe.scrybble.game;

import java.util.Collection;

/**
 * @author Ryan Pickett
 *
 */
public interface RackStockingProvider {

	/**
	 * @param player
	 * @param number
	 * @param tileBag
	 * @return
	 */
	Collection<Tile> getTiles(PlayerState player, int number, TileBag tileBag);
}
