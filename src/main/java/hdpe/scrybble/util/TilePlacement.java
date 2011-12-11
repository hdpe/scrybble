/**
 * 
 */
package hdpe.scrybble.util;

import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.game.TileState;

/**
 * @author Ryan Pickett
 *
 */
public class TilePlacement {
	private TileState tile;
	private SquareState square;
	private char letter;
	
	/**
	 * @param tileState
	 * @param squareState
	 * @param letter
	 */
	public TilePlacement(TileState tileState, SquareState squareState, char letter) {
		
		this.tile = tileState;
		this.square = squareState;
		this.letter = letter;
	}
	
	/**
	 * @return
	 */
	public TileState getTile() {
		return tile;
	}
	
	/**
	 * @return
	 */
	public SquareState getSquare() {
		return square;
	}
	
	/**
	 * @return
	 */
	public char getLetter() {
		return letter;
	}

	public String toString() {
		String s = "" + letter;
		
		if (tile.isBlank()) {
			s += "*";
		}
		
		return s;
	}
}