package hdpe.scrybble.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import hdpe.scrybble.game.IllegalMoveException;
import hdpe.scrybble.game.Rack;
import hdpe.scrybble.game.SwapTilesMove;
import hdpe.scrybble.game.Tile;
import hdpe.scrybble.game.TileBag;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SwapTilesMoveTest {

	static SwapTilesMove move;
	static TileBag bag;
	static Rack ts;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		bag = new TileBag(Arrays.asList(new Tile('O', 1), new Tile('E', 1), new Tile('C', 1)));
		ts = new Rack(new Tile('X', 8), new Tile('E', 1));
		
		move = new SwapTilesMove(ts.getState().getTiles());
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testApplyTileBagTileSet() {
		assertEquals(3, bag.size());
		assertEquals(2, ts.size());
		
		try {
			move.apply(bag, ts);
		} catch (IllegalMoveException e) {
			fail(e.getMessage());
		}
		
		assertEquals(5, bag.size());
		assertEquals(0, ts.size());		
	}
}