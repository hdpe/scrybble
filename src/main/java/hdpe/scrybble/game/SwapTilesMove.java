package hdpe.scrybble.game;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @author Ryan Pickett
 *
 */
public final class SwapTilesMove extends PlayerMove {

	private Collection<TileState> swap;
	
	/**
	 * @param tiles
	 */
	public SwapTilesMove(Collection<TileState> tiles) {
		this.swap = new ArrayList<TileState>(tiles);
	}

	public void apply(Player player, Game game) throws IllegalMoveException {
		TileBag bag = game.getTileBag();
		Rack rack = player.getRack();

		apply(bag, rack);
	}
	
	/**
	 * @param bag
	 * @param rack
	 * @throws IllegalMoveException
	 */
	public void apply(TileBag bag, Rack rack) throws IllegalMoveException {
		
		if (swap.size() > bag.size()) {
			throw new IllegalMoveException("Not enough tiles, bag only contains " + 
					bag.size(), this);
		}
		
		Collection<Tile> tiles = getTilesFromRack(rack);
		rack.removeAll(tiles);
		bag.putTiles(tiles);
	}

	public void accept(PlayerMoveVisitor visitor) {
		visitor.visitSwapTilesMove(this);
	}
	
	/**
	 * @return
	 */
	public Collection<TileState> getSwap() {
		return swap;
	}

	protected Collection<Tile> getTilesFromRack(Rack rack) throws IllegalMoveException {
		Collection<Tile> tiles = new ArrayList<Tile>();
		
		SWAP_LOOP: for (TileState swapTile : swap) {
			for (Tile tile : rack) {
				if (tile.getState().equals(swapTile)) {
					tiles.add(tile);
					continue SWAP_LOOP;
				}
			}
			throw new IllegalMoveException("No such tile in rack: " + swapTile, this);
		}

		return tiles;
	}

	public String toString() {
		return "Swap " + swap.size() + " tiles";
	}
}