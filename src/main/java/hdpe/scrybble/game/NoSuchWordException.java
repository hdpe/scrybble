package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public class NoSuchWordException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2542328653496720648L;

	/**
	 * @param word
	 */
	public NoSuchWordException(String word) {
		super("No such word: " + word);
	}
}
