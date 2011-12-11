package hdpe.scrybble.persist;

import hdpe.scrybble.game.FutureMove;
import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.PassGoMove;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerMove;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Ryan Pickett
 *
 */
@XmlRootElement(name = "pass")
public class SavedPassGoMove extends SavedPlayerMove {

	public SavedPassGoMove() {
		
	}
	
	public SavedPassGoMove(PassGoMove move) {

	}

	public FutureMove toStartMove(Game game, Player player) {
		return new FutureMove() {
			
			public PlayerMove getPlayerMove(Game game) {

				return new PassGoMove();
			}
		};		
	}

}
