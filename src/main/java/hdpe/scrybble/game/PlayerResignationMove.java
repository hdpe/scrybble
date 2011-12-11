package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public final class PlayerResignationMove extends PlayerMove {

	public void apply(Player player, Game game) throws IllegalMoveException {		
		player.resign();
	}

	public String toString() {
		return "Resign";
	}

	public void accept(PlayerMoveVisitor visitor) {
		visitor.visitPlayerResignationMove(this);
	}
}