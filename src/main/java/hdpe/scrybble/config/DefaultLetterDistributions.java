package hdpe.scrybble.config;

import hdpe.scrybble.game.LetterDistribution;
import hdpe.scrybble.game.LetterDistributions;
import hdpe.scrybble.game.Tile;
import hdpe.scrybble.game.TileBag;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Ryan Pickett
 *
 */
public class DefaultLetterDistributions implements LetterDistributions {
	
	private LetterDistribution[] distributions;
	
	/**
	 * 
	 */
	public DefaultLetterDistributions() {
		distributions = new LetterDistribution[] {
				new LetterDistribution('A', 1, 9),
				new LetterDistribution('B', 3, 2),
				new LetterDistribution('C', 3, 2),
				new LetterDistribution('D', 2, 4),
				new LetterDistribution('E', 1, 12),
				new LetterDistribution('F', 4, 2),
				new LetterDistribution('G', 2, 3),
				new LetterDistribution('H', 4, 2),
				new LetterDistribution('I', 1, 9),
				new LetterDistribution('J', 8, 1),
				new LetterDistribution('K', 5, 1),
				new LetterDistribution('L', 1, 4),
				new LetterDistribution('M', 3, 2),
				new LetterDistribution('N', 1, 6),
				new LetterDistribution('O', 1, 10),
				new LetterDistribution('P', 3, 2),
				new LetterDistribution('Q', 10, 1),
				new LetterDistribution('R', 1, 6),
				new LetterDistribution('S', 1, 4),
				new LetterDistribution('T', 1, 6),
				new LetterDistribution('U', 1, 4),
				new LetterDistribution('V', 4, 2),
				new LetterDistribution('W', 4, 2),
				new LetterDistribution('X', 8, 1),
				new LetterDistribution('Y', 4, 2),
				new LetterDistribution('Z', 10, 1),
				new LetterDistribution(Tile.BLANK, 0, 2)
		};
	}

	public int getScore(char character) {
		return distributions[character - 65].getScore();
	}

	public TileBag newTileBag() {
		List<Tile> tiles = new ArrayList<Tile>();
		for (LetterDistribution letter : distributions) {
			for (int i = 0; i < letter.getNumber(); i ++) {
				tiles.add(new Tile(letter.getLetter(), letter.getScore()));
			}
		}
		return new TileBag(tiles);	
	}

}