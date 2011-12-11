package hdpe.scrybble.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author Ryan Pickett
 *
 */
public class GameResult {
	
	private GameResultType type;
	private int winningScore;
	private Collection<Player> winningScorePlayers;
	private LinkedHashMap<Player, Integer> playerScores = new LinkedHashMap<Player, Integer>();
	
	/**
	 * @param players
	 */
	public GameResult(Collection<Player> players) {
		
		winningScore = 0;
		winningScorePlayers = null;
		playerScores = new LinkedHashMap<Player, Integer>();
		
		for (Player p : players) {
			playerScores.put(p, p.getScore());
			
			if (winningScorePlayers == null || 
					p.getScore() > winningScore) {
				winningScorePlayers = Arrays.asList(p);
				winningScore = p.getScore();
			} else if (p.getScore() == winningScore) {
				winningScorePlayers = new ArrayList<Player>(
						winningScorePlayers);
				winningScorePlayers.add(p);
			}
		}
		
		type = winningScorePlayers.size() == 1 ? GameResultType.WIN : 
			GameResultType.DRAW;
	}
	
	/**
	 * @return
	 */
	public GameResultType getType() {
		return type;
	}
	
	/**
	 * @return
	 */
	public int getWinningScore() {
		return winningScore;
	}

	/**
	 * @return
	 */
	public Collection<Player> getWinningScorePlayers() {
		return winningScorePlayers;
	}
	
	/**
	 * @return
	 */
	public LinkedHashMap<Player, Integer> getPlayerScores() {
		return playerScores;
	}
}