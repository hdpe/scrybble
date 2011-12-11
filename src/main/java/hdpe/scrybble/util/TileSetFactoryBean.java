package hdpe.scrybble.util;

import hdpe.scrybble.config.DefaultLetterDistributions;
import hdpe.scrybble.game.LetterDistributions;
import hdpe.scrybble.game.Rack;
import hdpe.scrybble.game.Tile;

/**
 * @author Ryan Pickett
 *
 */
public class TileSetFactoryBean {
	
	private LetterDistributions letterDistributions = 
		new DefaultLetterDistributions();
	
	/**
	 * @param tileString
	 * @return
	 */
	public Rack newTileSet(String tileString) {
		Rack result = new Rack();
		for (char c : tileString.toCharArray()) {
			if (c != '*') {
				result.add(new Tile(c, letterDistributions.getScore(c)));
			} else {
				result.add(new Tile(Tile.BLANK, 0));
			}
		}
		return result;
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
}