package hdpe.scrybble.game;

import static org.junit.Assert.assertEquals;
import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.GameContext;
import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.game.Tile;
import hdpe.scrybble.game.WordDirection;
import hdpe.scrybble.util.TileRun;

import org.junit.BeforeClass;
import org.junit.Test;

public class TilePlacementsTest {

	static Game game;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		GameContext gc = new GameContext();
		game = gc.newGame();
	}

	@Test
	public void testPlaceFirstMove() {
		
		SquareState s = game.getBoard().getSquare(3, 7).getState();		
		TileRun run = new TileRun(game.getState(), WordDirection.RIGHT, s);
		
		run = run.append(new Tile('F', 4).getState(), 'F', true, s);
		s = s.next(WordDirection.RIGHT);
		run = run.append(new Tile('E', 1).getState(), 'E', true, s);
		s = s.next(WordDirection.RIGHT);
		run = run.append(new Tile('V', 4).getState(), 'V', true, s);
		s = s.next(WordDirection.RIGHT);
		run = run.append(new Tile('E', 1).getState(), 'E', true, s);
		s = s.next(WordDirection.RIGHT);
		run = run.append(new Tile('R', 1).getState(), 'R', true, s);		
		
		assertEquals(30, run.getScore());
		assertEquals(30, run.toTilePlacements().getScore());
	}
}