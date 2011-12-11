package hdpe.scrybble.util;

import hdpe.scrybble.game.Board;
import hdpe.scrybble.game.GameResult;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerMove;
import hdpe.scrybble.game.event.GameAdapter;
import hdpe.scrybble.game.event.GameDrawStartTileEvent;
import hdpe.scrybble.game.event.GameMoveEvent;
import hdpe.scrybble.game.event.GameResultEvent;
import hdpe.scrybble.game.event.GameTurnStartEvent;

import java.util.Collection;


/**
 * @author Ryan Pickett
 *
 */
public class StandardOutLoggingGameListener extends GameAdapter {

	public void onTurnStart(GameTurnStartEvent event) {
		
		int turn = event.getTurn();
		Player player = event.getPlayer();
		int tilesRemaining = event.getGame().getTilesRemaining();

		System.out.println("\nTurn " + turn + " " + player + 
				" (score " + player.getScore() + ")");
		System.out.println(tilesRemaining + " tiles left");
		System.out.println(player.getRack());		
	}

	public void onMove(GameMoveEvent event) {

		PlayerMove move = event.getMove();
		Board board = event.getGame().getBoard();
		
		System.out.println("Playing move: " + move);					
		System.out.println(board);					
	}

	public void onEnd(GameResultEvent event) {

		GameResult result = event.getResult();
		Collection<Player> winningScorePlayers = result.getWinningScorePlayers();
		int winningScore = result.getWinningScore();
		
		if (winningScorePlayers.size() == 1) {
		
			System.out.println("Player " + winningScorePlayers.iterator().next() 
					+ " wins (" + winningScore + ")");
		} else {
			
			System.out.println("Draw " + winningScorePlayers 
					+ " (" + winningScore + ")");
		}		
	}

	public void onDrawStartTile(GameDrawStartTileEvent evt) {
		System.out.println("Player " + evt.getPlayer() + " draws " + 
				evt.getTile().getLetter());
	}
}