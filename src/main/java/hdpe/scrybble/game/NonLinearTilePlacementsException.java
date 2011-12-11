package hdpe.scrybble.game;

/**
 * @author Ryan Pickett
 *
 */
public class NonLinearTilePlacementsException extends IllegalTileLocationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4287145735891777322L;

	NonLinearTilePlacementsException(PlayerMove move) {
		super("Tile placements are not in a straight line", move);
	}

}
