package hdpe.scrybble.persist;

import hdpe.scrybble.game.FutureMove;
import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.Player;

import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * @author Ryan Pickett
 *
 */
@XmlSeeAlso(value = {
		SavedSwapTilesMove.class,
		SavedPassGoMove.class,
		SavedPlaceTilesMove.class,
		SavedPlayerResignationMove.class
}) 
public abstract class SavedPlayerMove {

	public abstract FutureMove toStartMove(Game game, Player player);
	
}
