package hdpe.scrybble.persist;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Ryan Pickett
 *
 */
@XmlRootElement(name = "blank")
public class SavedBlankTile extends SavedTile {

	public SavedBlankTile() {
		
	}
	
	public SavedBlankTile(String letter) {
		super(letter);
	}
	
	@XmlTransient public boolean isBlank() {
		return true;
	}
}
