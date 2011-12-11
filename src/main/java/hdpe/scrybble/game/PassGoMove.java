package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public final class PassGoMove extends PlayerMove {

	public void apply(Player player, Game game) throws IllegalMoveException {

		// do nothing
	}
	
	public void accept(PlayerMoveVisitor visitor) {
		visitor.visitPassGoMove(this);
	}
	
	public String toString() {
		
		return "Pass Go";
	}

}
