package hdpe.scrybble.persist;

import hdpe.scrybble.game.FutureMove;
import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.PlaceTilesMove;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerMove;
import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.game.Tile;
import hdpe.scrybble.game.TileState;
import hdpe.scrybble.util.TilePlacement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Ryan Pickett
 *
 */
@XmlRootElement(name = "placeTiles")
public class SavedPlaceTilesMove extends SavedPlayerMove {

	private List<SavedTilePlacement> tilePlacements = new ArrayList<SavedTilePlacement>();
	
	public SavedPlaceTilesMove() {
		
	}
	
	public SavedPlaceTilesMove(PlaceTilesMove move) {
		for (TilePlacement tp : move.getPlacementsList()) {
			tilePlacements.add(new SavedTilePlacement(tp));
		}
	}

	@XmlElement(name = "placement") public List<SavedTilePlacement> getTilePlacements() {
		return tilePlacements;
	}

	public void setTilePlacements(List<SavedTilePlacement> tilePlacements) {
		this.tilePlacements = tilePlacements;
	}

	public FutureMove toStartMove(final Game game, final Player player) {
		return new FutureMove() {			
			public PlayerMove getPlayerMove(Game game) {
				
				List<TilePlacement> placements = new ArrayList<TilePlacement>();				
				List<Tile> rackTiles = new ArrayList<Tile>(player.getRack().getTiles());
				
				for (SavedTilePlacement tp : tilePlacements) {
					TileState t = SavedTile.removeTile(tp.getTile(), rackTiles).getState();
					SquareState s = game.getBoard().getSquare(tp.getX(), tp.getY()).getState();
					placements.add(new TilePlacement(t, s, tp.getTile().getLetter().charAt(0)));
				}

				return new PlaceTilesMove(placements);
			}
		};
		
	}
}