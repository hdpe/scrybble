package hdpe.scrybble.util;

import hdpe.scrybble.game.BoardState;
import hdpe.scrybble.game.SquareState;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Ryan Pickett
 *
 */
public abstract class BoardUtil {
	private BoardUtil() {
		
	}
	
	/**
	 * @param board
	 * @return
	 */
	public static List<SquareState> getSquares(BoardState board) {
		List<SquareState> result = new ArrayList<SquareState>();
		for (int y = 0, x, maxY = board.getHeight(), maxX = board.getWidth(); 
				y < maxY; y ++) {
			for (x = 0; x < maxX; x ++) {
				result.add(board.getSquare(x, y));
			}
		}
		return result;
	}
}