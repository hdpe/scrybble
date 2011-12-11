package hdpe.scrybble.ui;

import hdpe.scrybble.config.Configuration;
import hdpe.scrybble.game.GameMode;
import hdpe.scrybble.game.PlayerStrategy;

import java.io.File;
import java.util.EventListener;


/**
 * @author Ryan Pickett
 *
 */
public interface GameFrameListener extends EventListener {

	void onDoConfigurationChange(Configuration configuration);
	
	void onDoNew();
	
	void onDoOpen(File file);
	
	void onDoSave(File file);
	
	void onDoStart(GameMode mode);

	void onDoStop();

	void onSquareClick(SquarePanel square);

	void onPlayerAdded(String playerName, PlayerStrategy strategy);
	
	void onPlayerChanged(int idx, String playerName, PlayerStrategy strategy);
	
	void onPlayerRemoved(int idx);
}