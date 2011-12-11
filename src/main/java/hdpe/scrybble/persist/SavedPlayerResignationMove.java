package hdpe.scrybble.persist;

import hdpe.scrybble.game.FutureMove;
import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerMove;
import hdpe.scrybble.game.PlayerResignationMove;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Ryan Pickett
 *
 */
@XmlRootElement(name = "resign")
public class SavedPlayerResignationMove extends SavedPlayerMove {

	public SavedPlayerResignationMove() {
		
	}
	
	public SavedPlayerResignationMove(PlayerResignationMove move) {

	}

	public FutureMove toStartMove(Game game, Player player) {
		return new FutureMove() {
			public PlayerMove getPlayerMove(Game game) {
				return new PlayerResignationMove();
			}
		};
	}

}
