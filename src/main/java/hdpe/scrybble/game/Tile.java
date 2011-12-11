package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public class Tile {
	
	public static final char BLANK = '*';

	private char letter;
	private int value;
	private Square square;
	
	private TileStateImpl state = new TileStateImpl();

	
	/**
	 * @param letter
	 * @param value
	 */
	public Tile(char letter, int value) {
		this.letter = letter;
		this.value = value;
	}
	
	/**
	 * @return
	 */
	public boolean isBlank() {
		return value == 0;
	}
	
	/**
	 * @return
	 */
	public char getLetter() {
		return letter;
	}

	/**
	 * @return
	 */
	public boolean isPlaced() {
		return square != null;
	}

	/**
	 * @return
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param letter
	 */
	public void setLetter(char letter) {
		this.letter = letter;
	}

	/**
	 * @param square
	 */
	public void setSquare(Square square) {
		this.square = square;
	}

	/**
	 * @return
	 */
	public TileState getState() {
		return state;
	}
	
	
	public String toString() {
		return String.valueOf(state.getLetter());
	}

	
	private class TileStateImpl implements TileState {

		public boolean isBlank() {
			return Tile.this.isBlank();
		}
		
		public char getLetter() {
			return Tile.this.getLetter();
		}
	
		public boolean isPlaced() {
			return Tile.this.isPlaced();
		}
	
		public int getValue() {
			return Tile.this.getValue();
		}
		
		public String toString() {
			return Tile.this.toString();
		}
	}	
}