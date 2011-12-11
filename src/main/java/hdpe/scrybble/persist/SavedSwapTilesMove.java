package hdpe.scrybble.persist;

import hdpe.scrybble.game.FutureMove;
import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerMove;
import hdpe.scrybble.game.SwapTilesMove;
import hdpe.scrybble.game.Tile;
import hdpe.scrybble.game.TileState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Ryan Pickett
 *
 */
@XmlRootElement(name = "swap")
public class SavedSwapTilesMove extends SavedPlayerMove {

	private List<SavedTile> swap = new ArrayList<SavedTile>();
	
	public SavedSwapTilesMove() {
		
	}
	
	public SavedSwapTilesMove(SwapTilesMove move) {
		for (TileState tile : move.getSwap()) {
			swap.add(tile.isBlank() ? new SavedBlankTile() : new SavedLetterTile(
					String.valueOf(tile.getLetter())));
		}
	}

	@XmlElementWrapper(name = "tiles") @XmlElementRef public List<SavedTile> getSwap() {
		return swap;
	}

	public void setSwap(List<SavedTile> swap) {
		this.swap = swap;
	}

	public FutureMove toStartMove(Game game, final Player player) {
		return new FutureMove() {
			
			public PlayerMove getPlayerMove(Game game) {
				Collection<Tile> tiles = SavedTile.getTiles(swap, player.getRack().getTiles());
				Collection<TileState> states = new ArrayList<TileState>();
				for (Tile tile : tiles) {
					states.add(tile.getState());
				}
				return new SwapTilesMove(states);
			}
		};
	}

}
