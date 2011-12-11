package hdpe.scrybble.game;

import static org.junit.Assert.assertEquals;

import hdpe.scrybble.game.Tile;
import hdpe.scrybble.game.TileBag;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TileBagTest {

	static TileBag bag;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bag = new TileBag(Arrays.asList(new Tile('O', 1), new Tile('E', 1)));
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetTiles() {
		int bagSize = bag.size();
		
		assertEquals(2, bagSize);
		
		Collection<Tile> tile = bag.getTiles(1);
		
		bagSize = bag.size();
		
		assertEquals(1, bagSize);
		assertEquals(1, tile.size());
	}
}