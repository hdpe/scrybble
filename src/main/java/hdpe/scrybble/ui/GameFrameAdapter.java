package hdpe.scrybble.ui;

import hdpe.scrybble.config.Configuration;
import hdpe.scrybble.game.GameMode;
import hdpe.scrybble.game.PlayerStrategy;

import java.io.File;


/**
 * @author Ryan Pickett
 *
 */
public class GameFrameAdapter implements GameFrameListener {

	public void onDoConfigurationChange(Configuration configuration) {

	}

	public void onDoNew() {
		
	}
	
	public void onDoOpen(File file) {
		
	}
	
	public void onDoSave(File file) {
		
	}

	public void onDoStop() {

	}
	
	public void onDoStart(GameMode mode) {

	}

	public void onSquareClick(SquarePanel square) {

	}

	public void onPlayerAdded(String playerName, PlayerStrategy strategy) {
		
	}

	public void onPlayerChanged(int idx, String playerName,
			PlayerStrategy strategy) {
		
	}

	public void onPlayerRemoved(int idx) {
		
	}
}