package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
class LoadedMove {
	
	private Player player;
	private FutureMove move;
	private FuturePickup pickup;
	private long elapsed;
	
	LoadedMove(Player player, FutureMove move, FuturePickup pickup,
			long elapsed) {		
		this.player = player;
		this.move = move;
		this.pickup = pickup;
		this.elapsed = elapsed;
	}
	/**
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}
	/**
	 * @return
	 */
	public FutureMove getMove() {
		return move;
	}
	/**
	 * @return
	 */
	public FuturePickup getPickup() {
		return pickup;
	}
	/**
	 * @return
	 */
	public long getElapsed() {
		return elapsed;
	}
}