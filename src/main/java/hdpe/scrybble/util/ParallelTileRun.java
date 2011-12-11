package hdpe.scrybble.util;

import hdpe.scrybble.game.Dictionary;
import hdpe.scrybble.game.GameState;
import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.game.WordDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Pickett
 *
 */
class ParallelTileRun {
	
	private WordDirection direction;
	private TileRun tileRun;
	
	ParallelTileRun(GameState game, SquareState square, WordDirection direction, 
			TileRunTile placed) {

		this.direction = direction == WordDirection.RIGHT ? WordDirection.DOWN
				: WordDirection.RIGHT;
		
		List<TileRunTile> tiles = new ArrayList<TileRunTile>();
		SquareState search = square.previous(this.direction);
		while (search != null && search.getTile() != null) {
			tiles.add(0, new TileRunTile(search.getTile(), search.getTile()
					.getLetter(), false, search));
			search = search.previous(this.direction);
		}
		tiles.add(placed);
		search = square.next(this.direction);
		while (search != null && search.getTile() != null) {
			tiles.add(new TileRunTile(search.getTile(), search.getTile()
					.getLetter(), false, search));
			search = search.next(this.direction);
		}
		
		if (tiles.size() > 1) {
			tileRun = new TileRun(game, this.direction, tiles);	
			tileRun.addTileScores();
		}		
	}

	TileRun getTileRun() {
		return tileRun;
	}

	int length() {
		return tileRun == null ? 0 : tileRun.length();
	}

	boolean isValid(Dictionary dictionary) {
		return dictionary.isValid(tileRun.toWordString());
	}
}