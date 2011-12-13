package hdpe.scrybble.ui;

import hdpe.scrybble.game.GameState;
import hdpe.scrybble.game.PlayerState;
import hdpe.scrybble.game.RackState;
import hdpe.scrybble.game.TileState;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author Ryan Pickett
 *
 */
public class RackPanel extends JPanel {

	private Collection<RackPanelListener> listeners = Collections.synchronizedList(new ArrayList<RackPanelListener>());
	private Set<RackTilePanel> selectedTiles = new HashSet<RackTilePanel>();
	private RackTilePanel[] tiles;
	
	private JCheckBox showTilesCheckbox;
	private JLabel playerLabel;
	private JPanel tilesPanel;
	private JButton goButton;
	private JButton swapButton;
	private JButton passButton;
	private JButton cheatButton;
	
	RackPanel() {

		goButton = new JButton("Go");
		goButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				notifyGo();
			}
		});
		
		swapButton = new JButton("Swap");
		swapButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				notifySwap();
			}
		});
		
		passButton = new JButton("Pass");
		passButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				notifySkip();
			}
		});
		
		cheatButton = new JButton("Cheat");
		cheatButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				notifyCheat();
			}
		});
		
		playerLabel = new JLabel();
		showTilesCheckbox = new JCheckBox("show tiles");
		showTilesCheckbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				tilesPanel.setVisible(showTilesCheckbox.isSelected());
				showTilesCheckbox.setEnabled(false);
			}			
		});
		
		JPanel topPanel = new JPanel() {{
			setLayout(new BorderLayout());
			add(playerLabel, BorderLayout.WEST);
			add(showTilesCheckbox, BorderLayout.EAST);
		}};
		
		tilesPanel = new JPanel() {{
			setLayout(new GridLayout(1, 7));			
		}};
		
		JPanel buttonPanel = new JPanel() {{
			add(goButton);
			add(swapButton);
			add(passButton);
			add(cheatButton);
		}};
		
		setLayout(new BorderLayout());
		add(topPanel, BorderLayout.NORTH);
		add(tilesPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);	
	}

	public void setRack(String playerName, RackState rack, GameState game) {

		playerLabel.setText(playerName);
		
		tilesPanel.removeAll();
		tiles = new RackTilePanel[7];
		selectedTiles.clear();
		
		swapButton.setEnabled(false);
		
		if (game.getHumanPlayerCount() > 1) {
			showTilesCheckbox.setVisible(true);
			showTilesCheckbox.setSelected(false);
			showTilesCheckbox.setEnabled(true);
			tilesPanel.setVisible(false);
			cheatButton.setEnabled(false);
		} else {
			showTilesCheckbox.setVisible(false);
			tilesPanel.setVisible(true);
			cheatButton.setEnabled(true);
		}
		
		int i = -1;
		
		for (ListIterator<TileState> iter = new ArrayList<TileState>(
				rack.getTiles()).listIterator(); iter.hasNext(); ) {
			
			i = iter.nextIndex();
			TileState tile = iter.next();
			tiles[i] = new RackTilePanel(tile);				
			MouseMotionListener mml = new MouseMotionListener(tiles[i]);
			tiles[i].addMouseListener(mml);
//			tiles[i].addMouseMotionListener(mml);
			tilesPanel.add(tiles[i]);
		}
		
		for (int j = i + 1; j < 7; j ++) {
			tiles[j] = new RackTilePanel(null);
			tilesPanel.add(tiles[j]);
		}
		
		revalidate();
	}

	public Set<RackTilePanel> getSelectedTiles() {
		return Collections.unmodifiableSet(selectedTiles);
	}


	public void addTile(TileState tile) {
		for (RackTilePanel t : tiles) {
			if (t.getTile() == null) {
				t.setTile(tile);
				return;
			}
		}
	}

	public void removeTile(TileState tile) {
		for (RackTilePanel t : tiles) {
			if (tile.equals(t.getTile())) {
				t.setTile(null);
				return;
			}
		}
	}	
	
	public void selectTile(RackTilePanel t, boolean selectAdditionally) {
		if (!selectAdditionally) {
			for (RackTilePanel tile : selectedTiles) {
				tile.deselect();
			}
			selectedTiles.clear();
		}
		if (t != null) {
			selectedTiles.add(t);
			t.select();
		}
		swapButton.setEnabled(selectedTiles.size() > 0);
	}
	
	public void addRackFrameListener(RackPanelListener listener) {
		listeners.add(listener);
	}
	
	public void removeRackFrameListener(RackPanelListener listener) {
		listeners.remove(listener);
	}
	
	protected void notifyGo() {
		for (RackPanelListener listener : new ArrayList<RackPanelListener>(listeners)) {
			listener.onGo();
		}
	}
	
	protected void notifySwap() {
		Collection<TileState> tiles = new ArrayList<TileState>();
		for (RackTilePanel t : selectedTiles) {
			tiles.add(t.getTile());
		}
		
		for (RackPanelListener listener : new ArrayList<RackPanelListener>(listeners)) {
			listener.onSwap(tiles);
		}
	}
	
	protected void notifySkip() {
		for (RackPanelListener listener : new ArrayList<RackPanelListener>(listeners)) {
			listener.onPass();
		}
	}
	
	protected void notifyCheat() {
		for (RackPanelListener listener : new ArrayList<RackPanelListener>(listeners)) {
			listener.onCheat();
		}
	}
	
	private class MouseMotionListener extends MouseAdapter {
//		private JPanel tilePanel;
//		private Point pt;
		
		public MouseMotionListener(RackTilePanel tilePanel) {
//			this.tilePanel = tilePanel;
		}
		
//		public void mouseDragged(MouseEvent e) {
//			pt = SwingUtilities.convertPoint(tilePanel, e.getX(), e.getY(), tilePanel.getParent());
//			System.out.println(pt);
//			tilePanel.setBounds(pt.x, pt.y, tilePanel.getWidth(), tilePanel.getHeight());
//		}
		
		public void mousePressed(MouseEvent e) {
			selectTile((RackTilePanel)e.getSource(), 
					e.isControlDown() || e.isShiftDown());
		}
	}

}
