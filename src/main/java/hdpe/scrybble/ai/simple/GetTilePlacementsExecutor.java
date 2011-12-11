package hdpe.scrybble.ai.simple;

import hdpe.scrybble.game.BoardState;
import hdpe.scrybble.game.Dictionary;
import hdpe.scrybble.game.GameState;
import hdpe.scrybble.game.RackState;
import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.game.WordDirection;
import hdpe.scrybble.util.BoardUtil;
import hdpe.scrybble.util.TilePlacements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Ryan Pickett
 *
 */
class GetTilePlacementsExecutor {
	
	private TilePlacements best;
	
	private Object lock = new Object();
	private ExecutorService executor = Executors.newFixedThreadPool(10);
	
	TilePlacements getBestTilePlacement(final GameState game, 
			final RackState rack, final Dictionary dictionary) {
		
		Collection<TargetSquare> targets = getTargetSquares(game);
		
		for (final TargetSquare target : targets) {			
			executor.execute(new Runnable() {
				
				public void run() {
					TilePlacements val = target.getBestTilePlacement(rack, 
							dictionary);
					synchronized (lock) {
						if (val != null && (best == null || 
								val.getScore() > best.getScore())) {
							best = val;
						}	
					}							
				}
			});				
		}
		
		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}

		return best;
	}

	protected List<TargetSquare> getTargetSquares(GameState game) {
		
		BoardState board = game.getBoard();
		
		if (!board.getCentreSquare().hasTile()) {
			
			return Arrays.asList(
					new TargetSquare(game, board.getCentreSquare(), WordDirection.RIGHT), 
					new TargetSquare(game, board.getCentreSquare(), WordDirection.DOWN)
					);	
		} else {
			
			List<TargetSquare> ss = new ArrayList<TargetSquare>();
			for (SquareState s : BoardUtil.getSquares(board)) {
				if (s.isClearAndAdjacentToTile()) {
					ss.add(new TargetSquare(game, s, WordDirection.RIGHT));
					ss.add(new TargetSquare(game, s, WordDirection.DOWN));
				}
			}
			
			return ss;
		}
	}
}