package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public class IllegalMoveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4603106151963323781L;
	
	private PlayerMove move;	
	
	IllegalMoveException(String msg, PlayerMove move) {
		super(msg);
		this.move = move;
	}

	/**
	 * @return
	 */
	public PlayerMove getMove() {
		return move;
	}
}