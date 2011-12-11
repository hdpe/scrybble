package hdpe.scrybble.game;

import java.util.Collection;


/**
 * @author Ryan Pickett
 *
 */
public interface RackState {

	/**
	 * @return
	 */
	Collection<TileState> getTiles();

	/**
	 * @return
	 */
	int size();
}
