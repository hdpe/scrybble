package hdpe.scrybble.persist;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Ryan Pickett
 *
 */
@XmlRootElement(name = "tile")
public class SavedLetterTile extends SavedTile {
	
	public SavedLetterTile() {
		
	}
	
	public SavedLetterTile(String letter) {
		super(letter);
	}
	
	@XmlTransient public boolean isBlank() {
		return false;
	}
}