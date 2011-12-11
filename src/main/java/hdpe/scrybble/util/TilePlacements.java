package hdpe.scrybble.util;

import hdpe.scrybble.game.GameState;
import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.game.WordDirection;

import java.util.Collection;
import java.util.List;


/**
 * @author Ryan Pickett
 *
 */
public class TilePlacements {
	
	private SquareState startSquare;
	private WordDirection direction;
	
	private List<TilePlacement> tiles;
	private Collection<TileRun> words;
	private int score;
	private boolean bonus;
	
	public static final int ALL_TILES_BONUS = 50;
	
	TilePlacements(GameState game, SquareState startSquare, WordDirection direction,
			List<TilePlacement> tiles, Collection<TileRun> words) {
		
		this.startSquare = startSquare;
		this.direction = direction;
		this.tiles = tiles;
		this.words = words;
		if (words.iterator().next().getPlacedByPlayer() == game.getRackSize()) {
			bonus = true;
			score += ALL_TILES_BONUS;
		}
		for (TileRun word : words) {
			score += word.getScore();
		}
	}
	
	/**
	 * @return
	 */
	public SquareState getStartSquare() {
		return startSquare;
	}

	/**
	 * @return
	 */
	public WordDirection getDirection() {
		return direction;
	}

	/**
	 * @return
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @return
	 */
	public List<TilePlacement> getTiles() {
		return tiles;
	}

	/**
	 * @return
	 */
	public Collection<TileRun> getWords() {
		return words;
	}	
	
	/**
	 * @return
	 */
	public boolean isBonus() {
		return bonus;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (TileRun word : words) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(word);
		}
		if (bonus) {
			sb.append(" +").append(ALL_TILES_BONUS);
		}
		if (words.size() > 1 || bonus) {
			sb.append(" = ").append(score);
		}
		return sb.toString();
	}
}