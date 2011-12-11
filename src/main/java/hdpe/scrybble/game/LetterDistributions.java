package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public interface LetterDistributions {
	/**
	 * @return
	 */
	TileBag newTileBag();

	/**
	 * @param character
	 * @return
	 */
	int getScore(char character);
}
