package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public class Square {

	private Board board;
	private int x;
	private int y;
	private int letterMultiplier;
	private int wordMultiplier;	
	private Tile tile;
	
	private SquareStateImpl state = new SquareStateImpl(this);
	
	/**
	 * @param board
	 * @param x
	 * @param y
	 * @param letterMultiplier
	 * @param wordMultiplier
	 */
	public Square(Board board, int x, int y, int letterMultiplier, 
			int wordMultiplier) {
			
		this.board = board;
		this.x = x;
		this.y = y;
		this.letterMultiplier = letterMultiplier;
		this.wordMultiplier = wordMultiplier;
	}

	/**
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * @return
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * @return
	 */
	public int getLetterMultiplier() {
		return letterMultiplier;
	}

	/**
	 * @return
	 */
	public int getWordMultiplier() {
		return wordMultiplier;
	}
	
	/**
	 * @return
	 */
	public SquareState getState() {
		return state;
	}

	/**
	 * @param tile
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
	}
	
	/**
	 * 
	 */
	public void clear() {
		this.tile = null;
	}
	
	/**
	 * @return
	 */
	public boolean isClearAndAdjacentToTile() {
		return !hasTile() && (
				(x > 0 && board.getSquare(x - 1, y).hasTile()) ||
				(x < 14 && board.getSquare(x + 1, y).hasTile()) ||
				(y > 0 && board.getSquare(x, y - 1).hasTile()) ||
				(y < 14 && board.getSquare(x, y + 1).hasTile()));
	}

	/**
	 * @return
	 */
	public Square left() {
		return x > 0 ? board.getSquare(x - 1, y) : null;
	}
	
	/**
	 * @return
	 */
	public Square right() {
		return x < board.getWidth() - 1 ? board.getSquare(x + 1, y) : null;
	}
	
	/**
	 * @return
	 */
	public Square up() {
		return y > 0 ? board.getSquare(x, y - 1) : null;
	}
	
	/**
	 * @return
	 */
	public Square down() {
		return y < board.getHeight() - 1 ? board.getSquare(x, y + 1) : null;
	}
	
	/**
	 * @return
	 */
	public boolean hasTile() {
		return tile != null;
	}
	
	/**
	 * @param direction
	 * @return
	 */
	public Square next(WordDirection direction) {
		return direction == WordDirection.RIGHT ? right() : down();
	}	
	
	/**
	 * @param direction
	 * @return
	 */
	public Square previous(WordDirection direction) {
		return direction == WordDirection.RIGHT ? left() : up();
	}
	
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Square other = (Square) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}


	private class SquareStateImpl implements SquareState {

		Square square;
		
		SquareStateImpl(Square square) {
			this.square = square;
		}
	
		public SquareState next(WordDirection direction) {
			return getSquareState(Square.this.next(direction));
		}	
		
		public SquareState previous(WordDirection direction) {
			return getSquareState(Square.this.previous(direction));
		}		
		
		protected SquareState getSquareState(Square square) {
			return square == null ? null : square.getState();
		}

		public boolean isClearAndAdjacentToTile() {
			return Square.this.isClearAndAdjacentToTile();
		}

		public boolean hasTile() {
			return Square.this.hasTile();
		}
	
		public int getX() {
			return Square.this.getX();
		}
	
		public int getY() {
			return Square.this.getY();
		}
	
		public int getLetterMultiplier() {
			return Square.this.getLetterMultiplier();
		}
	
		public int getWordMultiplier() {
			return Square.this.getWordMultiplier();
		}
	
		public TileState getTile() {
			return !hasTile() ? null : Square.this.getTile().getState();
		}
		
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((square == null) ? 0 : square.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SquareStateImpl other = (SquareStateImpl) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (square == null) {
				if (other.square != null)
					return false;
			} else if (!square.equals(other.square))
				return false;
			return true;
		}

		public String toString() {
			return Square.this.toString();
		}

		private Square getOuterType() {
			return Square.this;
		}
	}
}