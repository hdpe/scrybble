package hdpe.scrybble.ui;

import hdpe.scrybble.game.TileState;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * @author Ryan Pickett
 *
 */
class RackTilePanel extends JPanel {
	
	private TileState tile;
	
	RackTilePanel(TileState tile) {
		
		setLayout(new GridLayout(1, 1));
		
		setTile(tile);
		deselect();
	}
	
	TileState getTile() {
		return tile;
	}

	void deselect() {
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}
	
	void select() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
	}
	
	void setTile(TileState tile) {
		this.tile = tile;
		
		removeAll();
		if (tile != null) {
			add(new TilePanel(tile, tile.getLetter()));
		}
		deselect();
		revalidate();
		repaint();
	}

	public Dimension getPreferredSize() {
		return new Dimension(40, 40);
	}
	
	
}
