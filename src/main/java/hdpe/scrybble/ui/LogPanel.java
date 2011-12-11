package hdpe.scrybble.ui;

import hdpe.scrybble.game.GameContext;
import hdpe.scrybble.game.PlaceTilesMove;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerMove;
import hdpe.scrybble.util.TilePlacements;
import hdpe.scrybble.util.TileRun;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 * @author Ryan Pickett
 *
 */
class LogPanel extends JPanel {

	private DefaultStyledDocument doc;
	private JTextPane pane;
	
	private SimpleAttributeSet attribs;
	private Map<Player, PlayerTextAttributes> playerAttribs = 
			new HashMap<Player, PlayerTextAttributes>();
	
	private String definitionsUrl;
	
	private static final Object HYPERLINK_ATTRIBUTE = new Object();
	
	private Color[] colours = new Color[] {
		Color.BLUE,
		Color.RED,
		Color.GREEN,
		Color.ORANGE,
		Color.CYAN,
		Color.PINK
	};
	
	LogPanel() {
		setLayout(new GridLayout(1, 1));
		doc = new DefaultStyledDocument();
		attribs = new SimpleAttributeSet();
		pane = new JTextPane(doc);
		pane.setEditable(false);
		pane.setBackground(Color.WHITE);
		JScrollPane jsp = new JScrollPane(pane);
		add(jsp);
		
		
		MouseListener ml = new MouseListener();
		pane.addMouseListener(ml);		
		pane.addMouseMotionListener(ml);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(100, 150);
	}
	
	void reset() {
		playerAttribs.clear();
		try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	void log(String msg) {
		log(msg + "\n", attribs);
	}
	
	void log(String msg, Player player) {		
		log(msg + "\n", getPlayerTextAttributes(player).plain);
	}
	
	void log(PlayerMove move, Player player) {
	
		PlayerTextAttributes attribs = getPlayerTextAttributes(player);
	
		String s = move.toString();
		if (move instanceof PlaceTilesMove) {
			PlaceTilesMove ptm = (PlaceTilesMove)move;
			
			List<TileRun> words = new ArrayList<TileRun>(ptm.getTilePlacements()
					.getWords());
			
			log(player.toString() + ": ", attribs.plain);
			for (int i = 0, n = words.size(); i < n; i ++) {
				if (i > 0) {
					log(", ", attribs.plain);
				}
				TileRun word = words.get(i);
				log(word.toWordString(), attribs.hyperlinked);
			}
			if (ptm.getTilePlacements().isBonus()) {
				log(" +" + TilePlacements.ALL_TILES_BONUS, attribs.plain);
			}
			log(" (" + ptm.getTilePlacements().getScore() + ")\n", 
					attribs.plain);
		} else {
			log(player.toString() + ": " + s + "\n", attribs.plain);
		}
	}
	
	void setGameContext(GameContext gameContext) {
		this.definitionsUrl = gameContext.getConfiguration().getDefinitionsUrl();
	}
	
	protected String getDefinitionsUrl(String word) {
		return definitionsUrl
				.replaceAll("%l", word.toLowerCase())
				.replaceAll("%u", word.toUpperCase())
				.replaceAll("%s", word.toUpperCase().substring(0, 1) + word.toLowerCase().substring(1));
	}
	
	protected PlayerTextAttributes getPlayerTextAttributes(Player player) {
		PlayerTextAttributes attribs = playerAttribs.get(player);
		if (attribs == null) {
			attribs = new PlayerTextAttributes();
			playerAttribs.put(player, attribs);			
		}
		return attribs;
	}
	
	protected void log(String msg, AttributeSet attribs) {
		try {
			doc.insertString(doc.getLength(), msg, attribs);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private class PlayerTextAttributes {
		SimpleAttributeSet plain;
		SimpleAttributeSet hyperlinked;
		
		public PlayerTextAttributes() {
			plain = new SimpleAttributeSet();
			StyleConstants.setForeground(plain, colours[playerAttribs.size()]);
			hyperlinked = new SimpleAttributeSet(plain);
			StyleConstants.setUnderline(hyperlinked, true);
			hyperlinked.addAttribute(HYPERLINK_ATTRIBUTE, true);			
		}
	}
	
	private class MouseListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			
			Element el = getElement(e);
			
			if (isHyperlink(el)) {
			
				try {
					String word = doc.getText(el.getStartOffset(), 
							el.getEndOffset() - el.getStartOffset());
					
					if (Desktop.isDesktopSupported()) {
						Desktop desktop = Desktop.getDesktop();
						
						if (desktop.isSupported(Action.BROWSE)) {						
							desktop.browse(new URI(getDefinitionsUrl(word)));						
						}
					}					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}		
		
		public void mouseMoved(MouseEvent e) {
		
			Element el = getElement(e);
			
			if (isHyperlink(el)) {
				pane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			} else {
				pane.setCursor(null);
			}
		}

		protected Element getElement(MouseEvent e) {
			Point clickPoint = e.getPoint();
			int p = pane.viewToModel(clickPoint);
			return doc.getCharacterElement(p);
		}
		
		protected boolean isHyperlink(Element el) {		
			return el.getAttributes().containsAttribute(HYPERLINK_ATTRIBUTE, true);
		} 		
	}
}
