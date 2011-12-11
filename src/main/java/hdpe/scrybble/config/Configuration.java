package hdpe.scrybble.config;

import hdpe.scrybble.dict.SOWPODSDictionary;
import hdpe.scrybble.game.BoardLayout;
import hdpe.scrybble.game.Dictionary;
import hdpe.scrybble.game.LetterDistributions;

/**
 * @author Ryan Pickett
 *
 */
public class Configuration {

	private BoardLayout boardLayout;
	private Dictionary dictionary;
	private LetterDistributions letterDistributions;
	private String definitionsUrl;
	private int timePerPlayerSeconds;

	/**
	 * @return
	 */
	public static Configuration getDefaultConfiguration() {
		Configuration c = new Configuration();
		c.setBoardLayout(new DefaultBoardLayout());
		c.setDictionary(new SOWPODSDictionary());
		c.setLetterDistributions(new DefaultLetterDistributions());
		c.setDefinitionsUrl("http://en.wiktionary.org/wiki/%l");
		c.setTimePerPlayerSeconds(1500);
		return c;
	}

	/**
	 * @return
	 */
	public BoardLayout getBoardLayout() {
		return boardLayout;
	}

	/**
	 * @param boardLayout
	 */
	public void setBoardLayout(BoardLayout boardLayout) {
		this.boardLayout = boardLayout;
	}

	/**
	 * @return
	 */
	public Dictionary getDictionary() {
		return dictionary;
	}

	/**
	 * @param dictionary
	 */
	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	/**
	 * @return
	 */
	public LetterDistributions getLetterDistributions() {
		return letterDistributions;
	}

	/**
	 * @param letterDistributions
	 */
	public void setLetterDistributions(LetterDistributions letterDistributions) {
		this.letterDistributions = letterDistributions;
	}

	/**
	 * @return
	 */
	public String getDefinitionsUrl() {
		return definitionsUrl;
	}

	/**
	 * @param definitionsUrl
	 */
	public void setDefinitionsUrl(String definitionsUrl) {
		this.definitionsUrl = definitionsUrl;
	}

	/**
	 * @return
	 */
	public int getTimePerPlayerSeconds() {
		return timePerPlayerSeconds;
	}

	/**
	 * @param timePerPlayerSeconds
	 */
	public void setTimePerPlayerSeconds(int timePerPlayerSeconds) {
		this.timePerPlayerSeconds = timePerPlayerSeconds;
	}	
}