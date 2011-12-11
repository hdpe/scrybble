package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public abstract class PlayerMove {
	
	protected PlayerMove() {
		
	}
	
	/**
	 * @param player
	 * @param game
	 * @throws IllegalMoveException
	 */
	public abstract void apply(Player player, Game game) throws IllegalMoveException;
	
	/**
	 * @param visitor
	 */
	public abstract void accept(PlayerMoveVisitor visitor);
}
