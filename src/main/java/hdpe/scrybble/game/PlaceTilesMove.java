package hdpe.scrybble.game;

import hdpe.scrybble.util.TilePlacement;
import hdpe.scrybble.util.TilePlacements;
import hdpe.scrybble.util.TileRun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @author Ryan Pickett
 *
 */
public final class PlaceTilesMove extends PlayerMove {

	private List<TilePlacement> placements;
	private TilePlacements tp;
	
	private static final Comparator<TilePlacement> SORT_X =  
			new Comparator<TilePlacement>() {
		
		public int compare(TilePlacement t1, TilePlacement t2) {
			return t1.getSquare().getX() - t2.getSquare().getX();
		}
	};
	
	private static final Comparator<TilePlacement> SORT_Y =  
			new Comparator<TilePlacement>() {
		
		public int compare(TilePlacement t1, TilePlacement t2) {
			return t1.getSquare().getY() - t2.getSquare().getY();
		}
	};
	
	/**
	 * @param placements
	 */
	public PlaceTilesMove(List<TilePlacement> placements) {
		this.placements = placements;
	}

	public void apply(Player player, Game game) throws IllegalMoveException {

		int score = calculateScore(game);
		
		if (game.getMode() == GameMode.PLAY && game.getHumanPlayerCount() == 1) {
			try {
				tp.validate(game.getDictionary());
			} catch (NoSuchWordException e) {
				throw new IllegalMoveException(e.getMessage(), this);
			}
		}
		
		for (TilePlacement placement : placements) {

			Tile tile = null;
			TileState target = placement.getTile();
			for (Tile t : player.getRack()) {
				if (t.getState().equals(target)) {
					tile = t;
					break;
				}
			}
			
			game.placeTile(tile, game.getBoard().getSquare(placement.getSquare()
					.getX(), placement.getSquare().getY()), placement.getLetter());
			player.getRack().remove(tile);
		}

		player.incrementScore(score);
	}

	public void accept(PlayerMoveVisitor visitor) {
		visitor.visitPlaceTilesMove(this);
	}

	/**
	 * @return
	 */
	public List<TilePlacement> getPlacementsList() {
		return placements;
	}
	
	/**
	 * @return
	 */
	public TilePlacements getTilePlacements() {
		return tp;
	}

	public String toString() {
		return tp == null ? placements.toString() : tp.toString();
	}

	
	private int calculateScore(Game game) throws IllegalMoveException {
		
		if (placements.size() == 0) {
			throw new NotEnoughTilesPlacedException(this);
		}
		
		if (game.isFirstMove()) {
			boolean centreCovered = false;
			for (TilePlacement p : placements) {
				if (p.getSquare().equals(game.getBoard().getCentreSquare().getState())) {
					centreCovered = true;
					break;
				}
			}
			if (!centreCovered) {
				throw new IllegalTileLocationException("First move must include centre square", this);
			}
		}
		
		TilePlacement first = placements.get(0);
		
		TilePlacement last = first;
		boolean inlineHorizontal = true, inlineVertical = true, 
				nextToExisting = last.getSquare().isClearAndAdjacentToTile();
		
		for (int i = 1; i < placements.size(); i ++) {
			TilePlacement next = placements.get(i);
			if (next.getSquare().getX() != last.getSquare().getX()) {
				inlineVertical = false;
			}
			if (next.getSquare().getY() != last.getSquare().getY()) {
				inlineHorizontal = false;
			}
			if (next.getSquare().isClearAndAdjacentToTile()) {
				nextToExisting = true;
			}
			last = next;
		}
		
		if (!inlineHorizontal && !inlineVertical) {
			throw new NonLinearTilePlacementsException(this);
		}

		if (!game.isFirstMove() && !nextToExisting) {
			throw new IllegalTileLocationException("Not next to already " +
					"placed tiles", this);
		}

		WordDirection direction = inlineHorizontal ? WordDirection.RIGHT : 
				WordDirection.DOWN;
		
		if (placements.size() == 1) {
			if (game.isFirstMove()) {
				throw new NotEnoughTilesPlacedException(this);
			}
			
			SquareState s = first.getSquare();
			if ((s.previous(direction) == null || !s.previous(direction).hasTile()) 
					&& (s.next(direction) == null || !s.next(direction).hasTile())) {
				direction = direction== WordDirection.RIGHT ? WordDirection.DOWN 
						: WordDirection.RIGHT;
			}
		}
		
		List<TilePlacement> placements = new ArrayList<TilePlacement>(this.placements);		
		Collections.sort(placements, direction == WordDirection.RIGHT ? SORT_X : SORT_Y);
		
		first = placements.get(0);
		
		SquareState startSquare = first.getSquare(), s;		
		while ((s = startSquare.previous(direction)) != null && s.hasTile()) {
			startSquare = s;
		}
		
		TileRun run = new TileRun(game.getState(), direction, startSquare);

		s = startSquare;
		while (true) {
			
			TileState tile;
			char letter;
			boolean placedByPlayer;
			if (s.hasTile()) {
				tile = s.getTile();
				letter = tile.getLetter();
				placedByPlayer = false;
			} else {
				TilePlacement placement = placements.remove(0);
				tile = placement.getTile();
				letter = placement.getLetter();
				placedByPlayer = true;				
			}
			
			run = run.append(tile, letter, placedByPlayer, s);
			s = s.next(direction);
			
			if (s == null || (!s.hasTile() && placements.size() == 0)) {
				break;
			}
		}

		tp = run.toTilePlacements();
		return tp.getScore();
	}
}