package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public class LetterDistribution {
	private char letter;
	private int score;
	private int number;
	
	/**
	 * @param letter
	 * @param score
	 * @param number
	 */
	public LetterDistribution(char letter, int score, int number) {
		this.letter = letter;
		this.score = score;
		this.number = number;
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
	public int getScore() {
		return score;
	}
	/**
	 * @return
	 */
	public int getNumber() {
		return number;
	}
}