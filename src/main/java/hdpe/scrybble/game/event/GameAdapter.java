package hdpe.scrybble.game.event;

/**
 * @author Ryan Pickett
 *
 */
public abstract class GameAdapter implements GameListener {

	public void onDrawStartTile(GameDrawStartTileEvent evt) {
		
	}

	public void onFirstPlayerDetermined(GameFirstPlayerDeterminedEvent evt) {
		
	}

	public void onStart(GameStartEvent event) {

	}

	public void onTurnStart(GameTurnStartEvent event) {

	}

	public void onTurnEnd(GameTurnEndEvent event) {

	}
	
	public void onMove(GameMoveEvent event) {

	}

	public void onEnd(GameResultEvent event) {

	}

	public void onIllegalMove(GameIllegalMoveEvent evt) {
		
	}

	public void onStopped(GameStoppedEvent evt) {
		
	}	
}