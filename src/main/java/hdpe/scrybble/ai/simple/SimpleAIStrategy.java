package hdpe.scrybble.ai.simple;

import hdpe.scrybble.game.Dictionary;
import hdpe.scrybble.game.GameState;
import hdpe.scrybble.game.PassGoMove;
import hdpe.scrybble.game.PlaceTilesMove;
import hdpe.scrybble.game.PlayerMove;
import hdpe.scrybble.game.PlayerResignationMove;
import hdpe.scrybble.game.PlayerState;
import hdpe.scrybble.game.PlayerStrategy;
import hdpe.scrybble.game.RackState;
import hdpe.scrybble.game.SwapTilesMove;
import hdpe.scrybble.util.TilePlacements;

/**
 * @author Ryan Pickett
 *
 */
public class SimpleAIStrategy implements PlayerStrategy {

	private boolean passedLastGo;

	public void initGame(GameState game, PlayerState player) {
		passedLastGo = false;
	}

	public PlayerMove getNextMove(GameState game, RackState rack, 
			Dictionary dictionary) {
	
		TilePlacements tilePlacements = new GetTilePlacementsExecutor()
				.getBestTilePlacement(game, rack, dictionary);
		
		if (tilePlacements != null) {	
			passedLastGo = false;
			return new PlaceTilesMove(tilePlacements.getTiles());
			
		}
		
		if (game.getTilesRemaining() >= game.getRackSize()) {
			passedLastGo = false;
			return new SwapTilesMove(rack.getTiles());
		}
		
		if (!passedLastGo) {
			passedLastGo = true;
			return new PassGoMove();	
		}
		
		return new PlayerResignationMove();		
	}

	public void cancel() {
		// just leave it be
	}
}