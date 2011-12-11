package hdpe.scrybble.persist;

import hdpe.scrybble.game.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;


/**
 * @author Ryan Pickett
 *
 */
public class SavedPlayerRack {

	private String playerName;
	private List<SavedTile> tiles = new ArrayList<SavedTile>();
	private String playerStrategy;
	
	public SavedPlayerRack() {
		
	}
	
	public SavedPlayerRack(String playerName, String playerStrategy, Collection<Tile> tiles) {
		this.playerName = playerName;
		this.playerStrategy = playerStrategy;
		for (Tile tile : tiles) {
			this.tiles.add(!tile.isBlank() ? new SavedLetterTile(String.valueOf(tile.getLetter())) : new SavedBlankTile());
		}
	}

	@XmlAttribute public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@XmlAttribute public String getPlayerStrategy() {
		return playerStrategy;
	}

	public void setPlayerStrategy(String playerStrategy) {
		this.playerStrategy = playerStrategy;
	}

	@XmlElementWrapper(name = "tiles") @XmlElementRef public List<SavedTile> getTiles() {
		return tiles;
	}

	public void setTiles(List<SavedTile> tiles) {
		this.tiles = tiles;
	}
}