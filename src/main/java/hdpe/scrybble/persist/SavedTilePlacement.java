package hdpe.scrybble.persist;

import hdpe.scrybble.util.TilePlacement;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;


/**
 * @author Ryan Pickett
 *
 */
public class SavedTilePlacement {

	private int x;
	private int y;
	private SavedTile tile;
	
	public SavedTilePlacement() {
		
	}
	
	public SavedTilePlacement(TilePlacement tp) {
		x = tp.getSquare().getX();
		y = tp.getSquare().getY();
		String letter = String.valueOf(tp.getLetter());
		tile = !tp.getTile().isBlank() ? new SavedLetterTile(letter) : new SavedBlankTile(letter);
	}

	@XmlAttribute public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@XmlAttribute public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@XmlElementRef public SavedTile getTile() {
		return tile;
	}

	public void setTile(SavedTile tile) {
		this.tile = tile;
	}
}