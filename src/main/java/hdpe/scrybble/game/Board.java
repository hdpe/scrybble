package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public class Board {
	
	private Square[][] squares;
	private int width;
	private int height;
	
	private BoardStateImpl state = new BoardStateImpl();
	
	/**
	 * @param layout
	 */
	public Board(BoardLayout layout) {
		width = layout.getWidth();
		height = layout.getHeight();
		squares = new Square[width][height];
		for (int y = 0; y < height; y ++) {
			for (int x = 0; x < width; x ++) {
				squares[x][y] = new Square(this, x, y, 
						layout.getLetterMultiplier(x, y), 
						layout.getWordMultiplier(x, y));
			}
		}
	}

	/**
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return
	 */
	public Square getCentreSquare() {
		return squares[7][7];
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public Square getSquare(int x, int y) {
		return squares[x][y];
	}
	
	/**
	 * 
	 */
	public void clear() {
		for (int y = 0; y < getHeight(); y ++) {
			for (int x = 0; x < getWidth(); x ++) {
				getSquare(x, y).clear();
			}
		}
	}
	
	/**
	 * @param tile
	 * @param square
	 * @param letter
	 */
	public void placeTile(Tile tile, Square square, char letter) {
		square.setTile(tile);
		tile.setLetter(letter);
		tile.setSquare(square);
	}
	
	/**
	 * @return
	 */
	public BoardState getState() {
		return state;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		String mod;
		for (int y = 0; y < height; y ++) {
			for (int linechar = 0; linechar < (width*5); linechar ++) {
				s.append("-");
			}
			s.append("\n");
			
			for (int x = 0; x < width; x ++) {
				Square sq = getSquare(x, y);
				mod = null;
				switch (sq.getWordMultiplier()) {
					case 3: mod = "TW"; break;
					case 2: mod = "DW"; break;
					default: switch (sq.getLetterMultiplier()) {
						case 3: mod = "TL"; break;
						case 2: mod = "DL"; break;
						default: mod = "  ";
					}
				}
				s.append(mod).append(" ");
				Tile t = sq.getTile();
				if (t != null) {
					s.append(t.getLetter());
				} else {
					s.append(" ");
				}
				s.append("|");
			}
			s.append("\n");			
		}
		
		for (int linechar = 0; linechar < (width*5); linechar ++) {
			s.append("-");
		}
		s.append("\n");
		
		return s.toString();
	}

	private class BoardStateImpl implements BoardState {
	
		public int getHeight() {
			return Board.this.getHeight();
		}
	
		public int getWidth() {
			return Board.this.getWidth();
		}

		public SquareState getCentreSquare() {
			return Board.this.getCentreSquare().getState();
		}
		
		public SquareState getSquare(int x, int y) {
			return Board.this.getSquare(x, y).getState();
		}
		
		public String toString() {
			return Board.this.toString();
		}
	}
}