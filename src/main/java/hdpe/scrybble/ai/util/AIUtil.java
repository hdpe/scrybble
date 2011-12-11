package hdpe.scrybble.ai.util;

import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.game.WordDirection;

/**
 * @author Ryan Pickett
 *
 */
public abstract class AIUtil {

	private AIUtil() {
		
	}
	
	/**
	 * @param square1
	 * @param square2
	 * @param wrt
	 * @return
	 */
	public static boolean greaterThan(SquareState square1, SquareState square2, WordDirection wrt) {
		return wrt == WordDirection.RIGHT ? (square1.getX() > square2.getX()) :
			(square1.getY() > square2.getY());
	}

	/**
	 * @param square1
	 * @param square2
	 * @param wrt
	 * @return
	 */
	public static boolean lessThan(SquareState square1, SquareState square2, WordDirection wrt) {
		return wrt == WordDirection.RIGHT ? (square1.getX() < square2.getX()) :
			(square1.getY() < square2.getY());
	}
}
