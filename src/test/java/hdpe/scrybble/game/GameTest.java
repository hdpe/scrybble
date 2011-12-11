package hdpe.scrybble.game;

import static org.junit.Assert.assertEquals;
import hdpe.scrybble.ai.simple.SimpleAIStrategy;
import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.GameContext;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerImpl;
import hdpe.scrybble.util.StandardOutLoggingGameListener;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GameTest {

	@Test
	public void testStartDetermineFirstPlayer() {
		GameContext gc = new GameContext();
		List<Player> players = Arrays.asList(
				(Player)new PlayerImpl("P1", new SimpleAIStrategy()),
				(Player)new PlayerImpl("P2", new SimpleAIStrategy())
				); 
		gc.setPlayers(players);
		Game game = gc.newGame();
		game.addGameListener(new StandardOutLoggingGameListener());
		game.startDetermineFirstPlayer();
	}
	
	@Test
	public void testLoadPartialGame() throws Exception {
		GameContext gc = new GameContext();
		Game game = gc.loadGame(new File(GameTest.class.getResource(
				"partial.scrybble").toURI()));
		game.load();
		assertEquals(7, gc.getPlayers().get(0).getRack().getTiles().size());
		assertEquals(7, gc.getPlayers().get(1).getRack().getTiles().size());
	}
}