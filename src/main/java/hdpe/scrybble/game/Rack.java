package hdpe.scrybble.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Ryan Pickett
 *
 */
public class Rack extends ArrayList<Tile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RackState state = new RackStateImpl();

	/**
	 * 
	 */
	public Rack() {
		super();
	}

	/**
	 * @param tile
	 */
	public Rack(Tile... tile) {
		super();
		addAll(Arrays.asList(tile));
	}
	
	/**
	 * @return
	 */
	public RackState getState() {
		return state;
	}

	/**
	 * @return
	 */
	public int getValue() {
		int value = 0;
		for (Tile t : this) {
			 value += t.getValue();
		}
		return value;
	}
	
	/**
	 * @return
	 */
	public List<Tile> getTiles() {
		return this;
	}

	private class RackStateImpl implements RackState {

		public Collection<TileState> getTiles() {
			List<TileState> tiles = new ArrayList<TileState>();
			for (Tile tile : Rack.this) {
				tiles.add(tile.getState());
			}
			return tiles;
		}

		public int size() {
			return Rack.this.size();
		}
		
	}
	
}