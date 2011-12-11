package hdpe.scrybble.persist;

import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.GameContext;
import hdpe.scrybble.game.HistoryTurn;
import hdpe.scrybble.game.Player;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ryan Pickett
 *
 */
@XmlRootElement(name = "game")
public class SavedGame {

	private SavedConfiguration savedConfiguration;
	private List<SavedPlayerTurn> turns = new ArrayList<SavedPlayerTurn>();
	private List<SavedPlayerRack> racks = new ArrayList<SavedPlayerRack>();

	public SavedGame() {
		
	}
	
	public SavedGame(GameContext gameContext, Game game) {
		savedConfiguration = new SavedConfiguration(gameContext.getConfiguration());
		for (HistoryTurn playerTurn : game.getHistory().getTurns()) {
			turns.add(new SavedPlayerTurn(playerTurn));
		}
		for (Player player : game.getPlayers()) {
			racks.add(new SavedPlayerRack(player.getName(), 
					player.getStrategy().getClass().getName(), 
					game.getInitialRack(player)));
		}
	}
	
	@XmlElement(name = "config") public SavedConfiguration getSavedConfiguration() {
		return savedConfiguration;
	}

	public void setSavedConfiguration(SavedConfiguration savedConfiguration) {
		this.savedConfiguration = savedConfiguration;
	}

	@XmlElement(name = "rack") public List<SavedPlayerRack> getRacks() {
		return racks;
	}

	public void setRacks(List<SavedPlayerRack> racks) {
		this.racks = racks;
	}

	@XmlElement(name = "turn") public List<SavedPlayerTurn> getTurns() {
		return turns;
	}

	public void setTurns(List<SavedPlayerTurn> turns) {
		this.turns = turns;
	}
}