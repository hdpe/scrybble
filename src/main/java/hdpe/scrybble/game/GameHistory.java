package hdpe.scrybble.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ryan Pickett
 *
 */
public class GameHistory {

	private List<HistoryTurn> turns = new ArrayList<HistoryTurn>();
	
	/**
	 * @return
	 */
	public List<HistoryTurn> getTurns() {
		return turns;
	}

	/**
	 * @param player
	 * @param move
	 * @param pickup
	 * @param elapsed
	 */
	public void recordTurn(Player player, PlayerMove move,
			Collection<Tile> pickup, long elapsed) {
		turns.add(new HistoryTurn(player, move, pickup, elapsed));
	}
}