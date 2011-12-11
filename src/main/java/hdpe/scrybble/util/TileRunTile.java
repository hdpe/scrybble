package hdpe.scrybble.util;

import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.game.TileState;

/**
 * @author Ryan Pickett
 *
 */
class TileRunTile {
	
	private TileState tile;
	private SquareState square;
	
	private char letter;
	private boolean placedByPlayer;

	TileRunTile(TileState tile, char letter, boolean placedByPlayer,
			SquareState square) {
		this.tile = tile;
		this.letter = letter;
		this.placedByPlayer = placedByPlayer;
		this.square = square;
	}
	
	int getScore() {
		return tile.getValue() * getLetterMultiplier();
	}	
	
	int getLetterMultiplier() {
		return !placedByPlayer ? 1 : square.getLetterMultiplier();
	}
	
	int getWordMultiplier() {
		return !placedByPlayer ? 1 : square.getWordMultiplier();
	}
	
	char getLetter() {
		return letter;
	}
	
	TileState getTile() {
		return tile;
	}
	
	SquareState getSquare() {
		return square;
	}
	
	boolean isPlacedByPlayer() {
		return placedByPlayer;
	}

	public String toString() {
		return String.valueOf(tile);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + letter;
		result = prime * result + (placedByPlayer ? 1231 : 1237);
		result = prime * result + ((square == null) ? 0 : square.hashCode());
		result = prime * result + ((tile == null) ? 0 : tile.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileRunTile other = (TileRunTile) obj;
		if (letter != other.letter)
			return false;
		if (placedByPlayer != other.placedByPlayer)
			return false;
		if (square == null) {
			if (other.square != null)
				return false;
		} else if (!square.equals(other.square))
			return false;
		if (tile == null) {
			if (other.tile != null)
				return false;
		} else if (!tile.equals(other.tile))
			return false;
		return true;
	}		
}