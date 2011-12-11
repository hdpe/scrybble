package hdpe.scrybble.config;

import hdpe.scrybble.game.BoardLayout;

/**
 * @author Ryan Pickett
 *
 */
public class DefaultBoardLayout implements BoardLayout {

	public int getWidth() {
		return 15;
	}

	public int getHeight() {
		return 15;
	}

	public int getLetterMultiplier(int x, int y) {
		if (getWordMultiplier(x, y) > 1) {
			return 1;
		} else if (x > 0 && y > 0 && x < 14 && y < 14 && (x % 4 == 1) && (y % 4 == 1)) {
			return 3;
		} else if ((x == y || x == 14 - y)
				|| ((x == 0 || x == 7 || x == 14) && (y == 3 || y == 11))
				|| ((y == 0 || y == 7 || y == 14) && (x == 3 || x == 11))
				|| ((x == 2 || x == 12) && (y == 6 || y == 8))
				|| ((y == 2 || y == 12) && (x == 6 || x == 8))) {
			return 2;
		} else {
			return 1;
		}
	}

	public int getWordMultiplier(int x, int y) {
		if (y % 7 == 0 && x % 7 == 0 && !(x == 7 && y == 7)) {
			return 3;
		} else if ((x == y || x == 14 - y) && (x < 5 || x > 9 || x == 7)) {
			return 2;
		} else { 
			return 1;
		}
	}
}