package hdpe.scrybble.util;

import hdpe.scrybble.game.Dictionary;
import hdpe.scrybble.game.GameState;
import hdpe.scrybble.game.Square;
import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.game.TileState;
import hdpe.scrybble.game.WordDirection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author Ryan Pickett
 *
 */
public class TileRun {
	
	private GameState game;
	private SquareState startSquare;
	private WordDirection direction;
	
	private List<TileRunTile> tiles = new ArrayList<TileRunTile>();
	private Collection<ParallelTileRun> parallels = new ArrayList<ParallelTileRun>();
	
	private int scoreBase;
	private int placedByPlayer;
	private int multiplier = 1;
	private int score;
	
	/**
	 * @param game
	 * @param direction
	 * @param startSquare
	 */
	public TileRun(GameState game, WordDirection direction, SquareState startSquare) {
		this.game = game;
		this.direction = direction;
		this.startSquare = startSquare;
	}

	/**
	 * @param game
	 * @param direction
	 * @param tiles
	 */
	public TileRun(GameState game, WordDirection direction, 
			List<TileRunTile> tiles) {
		this.game = game;
		this.direction = direction;
		this.startSquare = tiles.get(0).getSquare();
		this.tiles = tiles;
	}

	/**
	 * @param copy
	 */
	public TileRun(TileRun copy) {
		this(copy.game, copy.direction, copy.startSquare);
		this.score = copy.score;
		this.scoreBase = copy.scoreBase;
		this.multiplier = copy.multiplier;
		this.placedByPlayer = copy.placedByPlayer;
		this.tiles = new ArrayList<TileRunTile>(copy.tiles);
		this.parallels = new ArrayList<ParallelTileRun>(copy.parallels);
	}

	/**
	 * @return
	 */
	public int length() {
		return tiles.size();
	}

	/**
	 * @return
	 */
	public TilePlacements toTilePlacements() {
		List<TilePlacement> ps = new ArrayList<TilePlacement>();
		Collection<TileRun> runs = new ArrayList<TileRun>();
		runs.add(this);
		for (ParallelTileRun ptr : parallels) {
			if (ptr.getTileRun() != null) {
				runs.add(ptr.getTileRun());
			}
		}
		for (TileRunTile t : tiles) {
			if (t.isPlacedByPlayer()) {
				ps.add(new TilePlacement(t.getTile(), t.getSquare(), 
						t.getLetter()));
			}
		}
		return new TilePlacements(game, startSquare, direction, ps, runs);
	}

	
	/**
	 * @param s
	 * @return
	 */
	public boolean contains(Square s) {
		for (TileRunTile t : tiles) {
			if (s.equals(t.getSquare())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public String toWordString() {
		StringBuilder sb = new StringBuilder();
		for (TileRunTile t : tiles) {
			sb.append(t.getLetter());
		}
		return sb.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (TileRunTile t : tiles) {
			sb.append(t.getLetter());
		}
		sb.append(" (").append(score).append(")");
		return sb.toString();
	}
	
	/**
	 * @return
	 */
	public int getScore() {
		return score;
	}
	
	void addTileScores() {
		for (TileRunTile tile : tiles) {
			addScore(tile);
		}
	}
	
	void addScore(TileRunTile tile) {
		scoreBase += tile.getScore();
		multiplier *= tile.getWordMultiplier();
		placedByPlayer += tile.isPlacedByPlayer() ? 1 : 0;
		score = scoreBase * multiplier;
	}	

	

	/**
	 * @param intendedTile
	 * @param intendedLetter
	 * @param placedByPlayer
	 * @param square
	 * @param dictionary
	 * @return
	 */
	public TileRun appendAndValidate(TileState intendedTile, char intendedLetter, 
			boolean placedByPlayer, SquareState square, Dictionary dictionary) {
		
		TileRun run = new TileRun(this);
		
		TileRunTile placed = new TileRunTile(intendedTile, intendedLetter, 
				placedByPlayer, square);
		
		run.tiles.add(placed);

		run.addScore(placed);
		
		ParallelTileRun parallel = null;
		
		if (placedByPlayer) {
			parallel = new ParallelTileRun(game, square, direction, placed);
			
			if (parallel.length() > 1) {
				if (!parallel.isValid(dictionary)) {
					return null;
				}
				run.parallels.add(parallel);
			}		
		}
		
		return run;
	}

	
	/**
	 * @param intendedTile
	 * @param intendedLetter
	 * @param placedByPlayer
	 * @param square
	 * @return
	 */
	public TileRun append(TileState intendedTile, char intendedLetter, 
			boolean placedByPlayer, SquareState square) {

		TileRun run = new TileRun(this);
		
		TileRunTile placed = new TileRunTile(intendedTile, intendedLetter, 
				placedByPlayer, square);
		
		run.tiles.add(placed);

		run.addScore(placed);
		
		ParallelTileRun parallel = null;
		
		if (placedByPlayer) {
			parallel = new ParallelTileRun(game, square, direction, placed);
			if (parallel.length() > 1) {
				run.parallels.add(parallel);	
			}			
		}
				
		return run;
	}	
	
	/**
	 * @return
	 */
	public int getPlacedByPlayer() {
		return placedByPlayer;
	}


	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tiles == null) ? 0 : tiles.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileRun other = (TileRun) obj;
		if (tiles == null) {
			if (other.tiles != null)
				return false;
		} else if (!tiles.equals(other.tiles))
			return false;
		return true;
	}
}