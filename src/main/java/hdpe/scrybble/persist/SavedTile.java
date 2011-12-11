package hdpe.scrybble.persist;

import hdpe.scrybble.game.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * @author Ryan Pickett
 *
 */
@XmlSeeAlso(value = {
		SavedLetterTile.class,
		SavedBlankTile.class
})
public abstract class SavedTile {
	private String letter;
	
	public SavedTile() {
		
	}
	
	public SavedTile(String letter) {
		this.letter = letter;
	}
	
	public abstract boolean isBlank();
	
	public String toString() {
		return String.valueOf(letter);
	}
	
	@XmlAttribute(name = "l") public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	
	public static Collection<Tile> getTiles(List<SavedTile> tiles,
			Collection<Tile> from) {
		return removeTiles(tiles, new ArrayList<Tile>(from));
	}

	
	public static Collection<Tile> removeTiles(List<SavedTile> tiles,
			Collection<Tile> from) {
		List<Tile> result = new ArrayList<Tile>();
		for (SavedTile tile : tiles) {
			result.add(removeTile(tile, from));
		}
		return result;
	}

	public static Tile removeTile(SavedTile savedTile, Collection<Tile> from) {
		for (Iterator<Tile> i = from.iterator(); i.hasNext(); ) {
			Tile tile = i.next();
			if ((!savedTile.isBlank() && tile.getLetter() == savedTile.getLetter().charAt(0))
				|| (savedTile.isBlank() && tile.isBlank())) {
				i.remove();
				return tile;
			}	
		}
		throw new IllegalArgumentException("No such tile " + savedTile + " in " + from);
	}	
}