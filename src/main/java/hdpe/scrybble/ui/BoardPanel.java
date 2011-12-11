package hdpe.scrybble.ui;

import hdpe.scrybble.game.Board;
import hdpe.scrybble.game.GameContext;
import hdpe.scrybble.game.PlaceTilesMove;
import hdpe.scrybble.game.PlayerMove;
import hdpe.scrybble.game.SquareState;
import hdpe.scrybble.util.TilePlacement;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * @author Ryan Pickett
 *
 */
class BoardPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6122640564563991290L;

	private GameFrame gameFrame;
	
	private SquarePanel[][] squares;
	
	BoardPanel(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
	}

	void applyMove(PlayerMove move) {
		if (move instanceof PlaceTilesMove) {
			List<TilePlacement> ps = ((PlaceTilesMove)move).getPlacementsList();
			for (TilePlacement p : ps) {
				applyTile(p);
			}
		}
	}

	void applyTile(TilePlacement p) {
		getSquare(p.getSquare()).applyTile(p);
	}

	void removeTile(TilePlacement p) {
		getSquare(p.getSquare()).removeTile();
	}

	void clear() {
		for (int y = 0; y < squares.length; y ++) {
			for (int x = 0; x < squares[0].length; x ++) {
				squares[x][y].clear();
			}
		}
		repaint();
	}

	void setGameContext(GameContext gameContext) {
		Board board = gameContext.getBoard();
		
		removeAll();
		
		setLayout(new GridLayout(board.getWidth(), board.getHeight()));
		
		squares = new SquarePanel[board.getWidth()][board.getHeight()];
		for (int y = 0; y < board.getHeight(); y ++) {
			for (int x = 0; x < board.getWidth(); x ++) {
				SquarePanel square = new SquarePanel(gameFrame, board.getSquare(
						x, y));
				squares[x][y] = square;
				add(square);
			}
		}
		
		setBorder(BorderFactory.createLineBorder(SquarePanel.SQUARE_COLOUR, 5));
		
		revalidate();
		repaint();
	}
	

	SquarePanel getSquare(SquareState square) {
		return squares[square.getX()][square.getY()];
	}
}