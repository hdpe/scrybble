package hdpe.scrybble.dict;

import hdpe.scrybble.game.Dictionary;
import hdpe.scrybble.util.Trie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * @author Ryan Pickett
 *
 */
public abstract class AbstractResourceLoadingDictionary implements Dictionary {

	private URL resource;
	private boolean skipFirstLine;
	private boolean loaded;
	private List<String> words = new ArrayList<String>();
	private Trie trie;
	
	/**
	 * @param resource
	 * @param skipFirstLine
	 */
	public AbstractResourceLoadingDictionary(URL resource, boolean skipFirstLine) {
		this.resource = resource;
		this.skipFirstLine = skipFirstLine;
	}
	
	public void init() {
		if (!loaded) {
			
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(
						resource.openStream()));
				String line = null;
				if (skipFirstLine) {
					reader.readLine();
				}
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line != null) {
						words.add(line);
					}
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Collections.sort(words);
			
			trie = new Trie(words);
			
			loaded = true;
		}
	}

	public Collection<String> getWords() {
		return words;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public boolean isValid(String word) {
		return trie.containsWord(word);
	}

	public Trie getTrie() {
		return trie;
	}
}