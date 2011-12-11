package hdpe.scrybble.game;

import hdpe.scrybble.ai.simple.SimpleAIStrategy;
import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.GameContext;
import hdpe.scrybble.game.GameResult;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerImpl;
import hdpe.scrybble.persist.GameIO;
import hdpe.scrybble.util.StandardOutLoggingGameListener;

import java.io.File;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class GameContextTest {

	@Test
	public void testLoadGame() throws Exception {
		
		File file = File.createTempFile("hdpe.scrybble", null);
		
		try {
			GameContext gc = new GameContext();
			gc.setPlayers(Arrays.asList(
					(Player)new PlayerImpl("P1", new SimpleAIStrategy()), 
					(Player)new PlayerImpl("P2", new SimpleAIStrategy())));
			
			Game game = gc.newGame();
			game.addGameListener(new StandardOutLoggingGameListener());
			
			GameResult result = game.start();
			
			GameIO.save(gc, game, file);
			
			game = gc.loadGame(file);
			game.addGameListener(new StandardOutLoggingGameListener());
			
			GameResult result2 = game.load();
			
			Assert.assertEquals(result.getPlayerScores().toString(), result2.getPlayerScores().toString());
			
		} finally {
			file.delete();
		}
	}
}