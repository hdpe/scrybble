package hdpe.scrybble.ai.simple;

import hdpe.scrybble.ai.util.AIUtil;
import hdpe.scrybble.game.Dictionary;
import hdpe.scrybble.game.GameState;
import hdpe.scrybble.game.RackState;
import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.game.TileState;
import hdpe.scrybble.game.WordDirection;
import hdpe.scrybble.util.TilePlacements;
import hdpe.scrybble.util.TileRun;
import hdpe.scrybble.util.Trie.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ryan Pickett
 *
 */
class TargetSquare {

	private GameState game;
	private SquareState square;
	private WordDirection direction;

	TargetSquare(GameState game, SquareState squareState, WordDirection direction) {
		this.game = game;
		this.square = squareState;
		this.direction = direction;
	}

	SquareState getSquare() {
		return square;
	}

	WordDirection getDirection() {
		return direction;
	}

	public String toString() {
		return String.valueOf(square);
	}

	TilePlacements getBestTilePlacement(RackState rack, Dictionary dictionary) {
		
		int bestScore = 0;
		TileRun bestRun = null;
		
		for (TileRun run : getPotentialTileRuns(rack, dictionary)) {
			int score = run.getScore(); 
			if (score > bestScore) {
				bestRun = run;
				bestScore = score;
			}
		}
		
		return bestRun == null ? null : bestRun.toTilePlacements();
	}
	
	Collection<TileRun> getPotentialTileRuns(RackState rack,
			Dictionary dictionary) {

		List<TileRun> words = new ArrayList<TileRun>();
		
		for (SquareState sq = getIterationStartSquare(rack); sq != null && 
				!AIUtil.greaterThan(sq, square, direction); sq = sq.next(direction)) {
			
			SquareState previous = sq.previous(direction);
			
			if (previous == null || !previous.hasTile()) {
		
				getWords(sq, rack, dictionary, words, square);
			}
		}
		
		return words;
	}

	protected void getWords(SquareState sq, RackState rack, Dictionary dictionary, 
			Collection<TileRun> words, SquareState target) {
		
		getWords(new TileRun(game, direction, sq), new ArrayList<TileState>(rack.getTiles()), 
				dictionary, sq, 0, words, target);
		
	}
	
	protected void getWords(TileRun tail, List<TileState> tiles, Dictionary dictionary, 
			SquareState sq, int idx, Collection<TileRun> words, SquareState target) {
		
		if (sq.hasTile()) {	
			getWords(tail, tiles, dictionary, sq.getTile(), sq.getTile().getLetter(), 
					words, idx, sq, target);			
		} else {
		
			for (int n = 0; n < tiles.size(); n ++) {
		
				TileState tile = tiles.get(n);
				
				if (tile.isBlank()) {
					
					for (char letter = 'A'; letter <= 'Z'; letter ++) {
						getWords(tail, tiles, dictionary, 
								tile, letter, words, idx, sq, target);
					}
					
				} else {
					
					getWords(tail, tiles, dictionary, 
							tile, tile.getLetter(), words, idx, sq, target);
				}
				
			}
		}
	}

	protected void getWords(TileRun tail, List<TileState> tiles, Dictionary dictionary, 
			TileState intendedTile, char intendedLetter, Collection<TileRun> words, 
			int idx, SquareState square, SquareState target) {


		TileRun newWord = tail.appendAndValidate(intendedTile, intendedLetter,
				!square.hasTile(), square, dictionary);
		
		if (newWord != null) {
			
			Node node = dictionary.getTrie().getNode(newWord.toWordString());
			
			if (node != null) {
				
				SquareState nextSquare = square.next(direction);
				
				if (!AIUtil.lessThan(square, target, direction) && node.isTerminal()
						&& (nextSquare == null || !nextSquare.hasTile())) {
					words.add(newWord);
				}
				
				if (!square.hasTile()) {
					tiles = new ArrayList<TileState>(tiles);
					tiles.remove(intendedTile);
				}
				
				if (nextSquare != null) {
					getWords(newWord, tiles, dictionary, nextSquare, 
							idx + 1, words, target);
				}
			}
		}
	}

	protected SquareState getIterationStartSquare(RackState rack) {
		
		SquareState previous = square.previous(direction);
		
		if (previous == null) {
			return square;
		} else if (previous.hasTile()) {
			SquareState last = previous;
			while ((previous = previous.previous(direction)) != null
					&& previous.hasTile()) {
				last = previous;
			}
			return last;
		} else {
			int length = 1;
			SquareState last = previous;
			while ((previous = previous.previous(direction)) != null
					&& !previous.hasTile()
					&& length <= rack.size()) {
				length ++;
				last = previous;
			}
			return last.next(direction);
		}
	}
}