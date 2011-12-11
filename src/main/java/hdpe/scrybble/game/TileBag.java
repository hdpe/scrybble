package hdpe.scrybble.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Ryan Pickett
 *
 */
public class TileBag {

	private List<Tile> tiles = new ArrayList<Tile>();
	
	/**
	 * @param tiles
	 */
	public TileBag(List<? extends Tile> tiles) {
		this.tiles.addAll(tiles);
	}
	

	/**
	 * @return
	 */
	public Collection<Tile> getTiles() {
		return tiles;
	}
	
	/**
	 * @param number
	 * @return
	 */
	public Collection<Tile> getTiles(int number) {
		Collections.shuffle(tiles);
		
		List<Tile> result = new ArrayList<Tile>();
		while (hasTiles() && result.size() < number) {
			result.add(tiles.remove(0));
		}
		return result;
	}
	
	/**
	 * @param tiles
	 */
	public void putTiles(Collection<Tile> tiles) {
		this.tiles.addAll(tiles);
	}
	
	/**
	 * @return
	 */
	public boolean hasTiles() {
		return !tiles.isEmpty();
	}

	/**
	 * @return
	 */
	public int size() {
		return tiles.size();
	}
}