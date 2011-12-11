package hdpe.scrybble.persist;

import hdpe.scrybble.config.Configuration;
import hdpe.scrybble.game.BoardLayout;
import hdpe.scrybble.game.Dictionary;
import hdpe.scrybble.game.LetterDistributions;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Ryan Pickett
 *
 */
public class SavedConfiguration {
	
	private String boardLayout;
	private String dictionary;
	private String letterDistributions;
	private String definitionsUrl;
	private int timePerPlayerSeconds;
	
	public SavedConfiguration() {
		
	}
	
	public SavedConfiguration(Configuration configuration) {
		boardLayout = configuration.getBoardLayout().getClass().getName();
		dictionary = configuration.getDictionary().getClass().getName();
		letterDistributions = configuration.getLetterDistributions().getClass().getName();
		definitionsUrl = configuration.getDefinitionsUrl();
		timePerPlayerSeconds = configuration.getTimePerPlayerSeconds();
	}
	
	public Configuration toConfiguration() throws InstantiationException, 
			IllegalAccessException, ClassNotFoundException {
		Configuration c = Configuration.getDefaultConfiguration();
		c.setBoardLayout((BoardLayout)Class.forName(boardLayout).newInstance());
		c.setDictionary((Dictionary)Class.forName(dictionary).newInstance());
		c.setLetterDistributions((LetterDistributions)Class.forName(letterDistributions).newInstance());
		c.setDefinitionsUrl(definitionsUrl);
		c.setTimePerPlayerSeconds(timePerPlayerSeconds);
		return c;
	}

	@XmlAttribute public String getBoardLayout() {
		return boardLayout;
	}

	public void setBoardLayout(String boardLayout) {
		this.boardLayout = boardLayout;
	}

	@XmlAttribute public String getDictionary() {
		return dictionary;
	}

	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}

	@XmlAttribute public String getLetterDistributions() {
		return letterDistributions;
	}

	public void setLetterDistributions(String letterDistributions) {
		this.letterDistributions = letterDistributions;
	}

	@XmlAttribute public String getDefinitionsUrl() {
		return definitionsUrl;
	}

	public void setDefinitionsUrl(String definitionsUrl) {
		this.definitionsUrl = definitionsUrl;
	}

	@XmlAttribute public int getTimePerPlayerSeconds() {
		return timePerPlayerSeconds;
	}

	public void setTimePerPlayerSeconds(int timePerPlayerSeconds) {
		this.timePerPlayerSeconds = timePerPlayerSeconds;
	}
}