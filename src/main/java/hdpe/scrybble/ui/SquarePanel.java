package hdpe.scrybble.ui;

import hdpe.scrybble.game.Square;
import hdpe.scrybble.util.TilePlacement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * @author Ryan Pickett
 *
 */
class SquarePanel extends JPanel {
	
	static Color SQUARE_COLOUR = new Color(0xcfdea2);
	static Color SQUARE_BORDER_COLOUR = Color.WHITE;
	
	static Color DOUBLE_LETTER_COLOUR = new Color(0x8fcfc7);
	static Color TRIPLE_LETTER_COLOUR = new Color(0x162acf);
	static Color DOUBLE_WORD_COLOUR = Color.PINK;
	static Color TRIPLE_WORD_COLOUR = new Color(0xf30f0f);
	
	private Square square;
	private TilePlacement tilePlacement;
	
	SquarePanel(final GameFrame gameFrame, Square square) {
		
		this.square = square;
		
		setLayout(new GridLayout(1, 1));
		if (square.getLetterMultiplier() == 2) {
			setBackground(DOUBLE_LETTER_COLOUR);
		} else if (square.getLetterMultiplier() == 3) {
			setBackground(TRIPLE_LETTER_COLOUR);
		} else if (square.getWordMultiplier() == 2) {
			setBackground(DOUBLE_WORD_COLOUR);
		} else if (square.getWordMultiplier() == 3) {
			setBackground(TRIPLE_WORD_COLOUR);
		} else {
			setBackground(SQUARE_COLOUR);
		}
		setBorder(BorderFactory.createLineBorder(SQUARE_BORDER_COLOUR, 1));
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				gameFrame.fireSquareClick(SquarePanel.this);
			}			
		});
		
		addMouseMotionListener(new MouseAdapter() {

			public void mouseDragged(MouseEvent e) {
				System.out.println("in");
			}
		});
	}

	public Dimension getPreferredSize() {
		return new Dimension(30, 30);
	}

	Square getSquare() {
		return square;
	}

	TilePlacement getTilePlacement() {
		return tilePlacement;
	}

	void applyTile(TilePlacement p) {
		tilePlacement = p;
		add(new TilePanel(p.getTile(), p.getLetter()));
		revalidate();
	}
	
	void removeTile() {
		tilePlacement = null;
		removeAll();
		repaint();
	}
	
	void clear() {
		removeTile();
	}
}