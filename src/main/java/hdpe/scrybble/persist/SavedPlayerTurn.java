package hdpe.scrybble.persist;

import hdpe.scrybble.game.HistoryTurn;
import hdpe.scrybble.game.PassGoMove;
import hdpe.scrybble.game.PlaceTilesMove;
import hdpe.scrybble.game.PlayerMoveVisitor;
import hdpe.scrybble.game.PlayerResignationMove;
import hdpe.scrybble.game.SwapTilesMove;
import hdpe.scrybble.game.Tile;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;


/**
 * @author Ryan Pickett
 *
 */
public class SavedPlayerTurn implements PlayerMoveVisitor {

	private String playerName;
	private SavedPlayerMove move;
	private List<SavedTile> pickup = new ArrayList<SavedTile>();
	private long elapsed;

	public SavedPlayerTurn() {
		
	}
	
	public SavedPlayerTurn(HistoryTurn playerTurn) {
		playerName = playerTurn.getPlayer().getName();
		playerTurn.getMove().accept(this);
		for (Tile t : playerTurn.getPickup()) {
			pickup.add(!t.isBlank() ? new SavedLetterTile(String.valueOf(t.getLetter())) : new SavedBlankTile());
		}
		elapsed = playerTurn.getElapsed();
	}

	public void visitPassGoMove(PassGoMove move) {
		this.move = new SavedPassGoMove(move);
	}

	public void visitPlaceTilesMove(PlaceTilesMove move) {
		this.move = new SavedPlaceTilesMove(move);
	}

	public void visitPlayerResignationMove(PlayerResignationMove move) {
		this.move = new SavedPlayerResignationMove(move);
	}

	public void visitSwapTilesMove(SwapTilesMove move) {
		this.move = new SavedSwapTilesMove(move);
	}
	
	@XmlAttribute(name = "player") public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@XmlElementRef public SavedPlayerMove getMove() {
		return move;
	}

	public void setMove(SavedPlayerMove move) {
		this.move = move;
	}

	@XmlElementWrapper(name = "pickup")	@XmlElementRef public List<SavedTile> getPickup() {
		return pickup;
	}

	public void setPickup(List<SavedTile> pickup) {
		this.pickup = pickup;
	}

	@XmlAttribute(name = "elapsed") public long getElapsed() {
		return elapsed;
	}

	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}
}