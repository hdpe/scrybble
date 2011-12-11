package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public interface PlayerMoveVisitor {

	/**
	 * @param move
	 */
	void visitPassGoMove(PassGoMove move);
	
	/**
	 * @param move
	 */
	void visitPlaceTilesMove(PlaceTilesMove move);
	
	/**
	 * @param move
	 */
	void visitPlayerResignationMove(PlayerResignationMove move);
	
	/**
	 * @param move
	 */
	void visitSwapTilesMove(SwapTilesMove move);
}
