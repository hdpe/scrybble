package hdpe.scrybble.game.event;

import java.util.EventListener;

/**
 * @author Ryan Pickett
 *
 */
public interface GameListener extends EventListener {

	/**
	 * @param evt
	 */
	void onDrawStartTile(GameDrawStartTileEvent evt);

	/**
	 * @param evt
	 */
	void onFirstPlayerDetermined(GameFirstPlayerDeterminedEvent evt);
	
	/**
	 * @param evt
	 */
	void onStart(GameStartEvent evt);
	
	/**
	 * @param evt
	 */
	void onTurnStart(GameTurnStartEvent evt);
	
	/**
	 * @param evt
	 */
	void onTurnEnd(GameTurnEndEvent evt);
	
	/**
	 * @param evt
	 */
	void onMove(GameMoveEvent evt);
	
	/**
	 * @param evt
	 */
	void onEnd(GameResultEvent evt);

	/**
	 * @param evt
	 */
	void onIllegalMove(GameIllegalMoveEvent evt);

	/**
	 * @param evt
	 */
	void onStopped(GameStoppedEvent evt);
}
