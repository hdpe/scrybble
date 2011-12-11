package hdpe.scrybble.game;

import hdpe.scrybble.util.Trie;

import java.util.Collection;


/**
 * @author Ryan Pickett
 *
 */
public interface Dictionary {
	
	/**
	 * 
	 */
	void init();

	/**
	 * @return
	 */
	boolean isLoaded();	
	
	/**
	 * @param word
	 * @return
	 */
	boolean isValid(String word);
	
	/**
	 * @return
	 */
	Collection<String> getWords();

	/**
	 * @return
	 */
	Trie getTrie();	
}