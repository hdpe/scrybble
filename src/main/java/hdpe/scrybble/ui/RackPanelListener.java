package hdpe.scrybble.ui;

import hdpe.scrybble.game.TileState;

import java.util.Collection;


/**
 * @author Ryan Pickett
 *
 */
public interface RackPanelListener {
	
	public void onGo();

	public void onSwap(Collection<TileState> tiles);

	public void onPass();

	public void onCheat();
}
