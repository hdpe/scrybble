package hdpe.scrybble.ui;

import hdpe.scrybble.game.PlayerState;
import hdpe.scrybble.game.RackStockingProvider;
import hdpe.scrybble.game.Tile;
import hdpe.scrybble.game.TileBag;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * @author Ryan Pickett
 *
 */
public class RackStockingPromptFrame extends JDialog implements RackStockingProvider {
	
	private Component parent;
	
	private int number;
	private TileBag tileBag;
	
	private JLabel infoLabel;
	private JTextField tilesField;
	
	private Collection<Tile> result;
	private Object lock = new Object();
	private boolean disposing;
	
	public RackStockingPromptFrame(Component parent) {
		this.parent = parent;
		
		JPanel infoPanel = new JPanel() {{
			infoLabel = new JLabel();
			add(infoLabel);
			add(new JLabel("_ = blank"));
		}};
		
		JPanel fieldPanel = new JPanel() {{
			setLayout(new SpringLayout());
			add(new JLabel("Tiles:"));
			tilesField = new JTextField();
			tilesField.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent arg0) {
					onOk();
				}
			});
			add(tilesField);			
			UIUtil.makeSprings(this, 1, 2);
		}};
		
		JPanel buttonPanel = new JPanel() {{
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent evt) {
					onOk();					
				}
			});			
			add(okButton);
		}};
		
		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		c.add(infoPanel);
		c.add(fieldPanel);
		c.add(buttonPanel);
		
		setModal(true);
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(250, 130);
	}
	
	public void dispose() {
		super.dispose();
		synchronized (lock) {
			disposing = true;
			lock.notifyAll();
		}
	}

	public void onOk() {
		Collection<Tile> tiles;
		try {
			tiles = getTiles(tilesField.getText(), number, tileBag);
		} catch (TileEntryException e) {
			JOptionPane.showMessageDialog(RackStockingPromptFrame.this, 
					e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;						
		}
		synchronized (lock) {
			result = tiles;
			lock.notifyAll();
		}
	}
	
	public Collection<Tile> getTiles(final PlayerState player, final int number,
			TileBag tileBag) {
			
		SwingUtilities.invokeLater(new Runnable() {			
			public void run() {
				setTitle(player.getName());
				infoLabel.setText("Enter " + number + " tiles");
				tilesField.setText("");

				setLocationRelativeTo(parent);
				setVisible(true);
			}
		});
		
		synchronized (lock) {
			this.number = number;
			this.tileBag = tileBag;
			result = null;
			
			while (!disposing && result == null) {
				try {
					lock.wait();
				} catch (InterruptedException e) {}
			}
		}
		
		SwingUtilities.invokeLater(new Runnable() {			
			public void run() {
				setVisible(false);
			}
		});
		
		return result;							
	}
	
	private Collection<Tile> getTiles(String val, int number, TileBag tileBag) throws TileEntryException {
		if (val == null) {
			throw new TileEntryException("You must enter the tiles you've picked up");
		}
		
		val = val.toString().toUpperCase();
		
		if (!val.matches("^[A-Z_]{" + number + "}$")) {
			throw new TileEntryException("You must enter " + number + " letters (with _ = blank)");
		}
	
		List<Tile> tiles = new ArrayList<Tile>();
		LETTER_LOOP: for (char c : val.toCharArray()) {
			for (Tile t : tileBag.getTiles()) {
				if (!tiles.contains(t) && ((!t.isBlank() && t.getLetter() == c) ||
						(t.isBlank() && c == '_'))) {
					tiles.add(t);
					continue LETTER_LOOP;
				}
			}
		}
		
		if (tiles.size() < number) {
			throw new TileEntryException("Some of these tiles aren't in the bag!");	
		}
		
		return tiles;
	}
	
	private class TileEntryException extends Exception {

		public TileEntryException(String arg0) {
			super(arg0);
		}		
	}
}