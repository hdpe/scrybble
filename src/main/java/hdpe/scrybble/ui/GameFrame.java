package hdpe.scrybble.ui;

import hdpe.scrybble.ApplicationConstants;
import hdpe.scrybble.config.Configuration;
import hdpe.scrybble.game.GameContext;
import hdpe.scrybble.game.GameMode;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerMove;
import hdpe.scrybble.game.PlayerStrategy;
import hdpe.scrybble.util.TilePlacement;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Ryan Pickett
 *
 */
public class GameFrame extends JFrame {
	
	private BoardPanel boardPanel;
	private PlayersPanel playersPanel;
	private LogPanel logPanel;
	
	private EventListenerList listeners = new EventListenerList();
	
	private GameContext gameContext;
	private GameMode mode;
	
	private JMenu fileMenu;
	private JMenu configureMenu;
	private JMenu toolsMenu;
	
	private File file;
	
	private Timer clocksTimer;
	
	private JFileChooser chooser = new JFileChooser();
	
	public GameFrame(GameContext gameContext) {
		
		super(ApplicationConstants.APP_NAME);
		
		boardPanel = new BoardPanel(this);
		playersPanel = new PlayersPanel(this);
		logPanel = new LogPanel();

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        ApplicationConstants.APP_NAME + " files", 
		        ApplicationConstants.DEFAULT_EXTENSION);
	    chooser.setFileFilter(filter);
	    
		setGameContext(gameContext);
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		JPanel leftPanel = new JPanel() {{
			add(boardPanel);
		}};
		
		JPanel rightPanel = new JPanel() {{
			setLayout(new BorderLayout());
			add(playersPanel, BorderLayout.NORTH);
			add(logPanel, BorderLayout.CENTER);
		}};
		
		c.add(leftPanel, BorderLayout.WEST);
		c.add(rightPanel, BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem newItem = new JMenuItem("New", KeyEvent.VK_N);
		newItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireNew();
			}
		});
		fileMenu.add(newItem);
		
		JMenuItem openItem = new JMenuItem("Open", KeyEvent.VK_O);
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = chooser.showOpenDialog(GameFrame.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					fireLoad(chooser.getSelectedFile());
				}
			}
		});
		fileMenu.add(openItem);
		
		JMenuItem saveItem = new JMenuItem("Save", KeyEvent.VK_S);
		saveItem.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				if (file == null) {
					if (chooser.showSaveDialog(GameFrame.this) == JFileChooser.APPROVE_OPTION) {
						fireSave(chooser.getSelectedFile());
					}
				} else {
					fireSave(file);
				}
			}
		});
		fileMenu.add(saveItem);
		
		JMenuItem saveAsItem = new JMenuItem("Save As", KeyEvent.VK_A);
		saveAsItem.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				int result = chooser.showSaveDialog(GameFrame.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					if (chooser.getSelectedFile().exists()) {
						if (JOptionPane.showConfirmDialog(GameFrame.this, 
								"Overwrite existing file?", "Please Confirm", 
								JOptionPane.OK_CANCEL_OPTION, 
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
							fireSave(chooser.getSelectedFile());	
						}
					} else {
						fireSave(chooser.getSelectedFile());
					}
				}
			}
		});
		fileMenu.add(saveAsItem);
		
		configureMenu = new JMenu("Options");
		configureMenu.setMnemonic(KeyEvent.VK_O);
		JMenuItem configureItem = new JMenuItem("Configure", KeyEvent.VK_C);
		
		configureItem.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				ConfigFrame cf = new ConfigFrame(GameFrame.this);
				cf.setConfiguration(GameFrame.this.gameContext.getConfiguration());
				cf.setLocationRelativeTo(GameFrame.this);
				cf.setVisible(true);
			}
		});
		
		configureMenu.add(configureItem);
		
		toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		
		JMenuItem wordLookupItem = new JMenuItem("Word Look-up", KeyEvent.VK_W);
		wordLookupItem.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				WordLookupFrame wlf = new WordLookupFrame(GameFrame.this);
				wlf.setGameContext(GameFrame.this.gameContext);
				wlf.setLocationRelativeTo(GameFrame.this);
				wlf.setVisible(true);
			}
		});
		toolsMenu.add(wordLookupItem);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);
		aboutItem.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(GameFrame.this, "Sired by Ryan",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(aboutItem);
		
		menuBar.add(fileMenu);
		menuBar.add(configureMenu);
		menuBar.add(toolsMenu);
		menuBar.add(helpMenu);		
		setJMenuBar(menuBar);
		
		GameFrameHolder.register(this);
		
		setConfigControlsEnabled(true);
		
		pack();
	}
	

	public void setGameContext(GameContext gameContext) {
		this.gameContext = gameContext;
		boardPanel.setGameContext(gameContext);
		logPanel.setGameContext(gameContext);
		playersPanel.setGameContext(gameContext);
	}

	public void applyMove(PlayerMove move) {
		boardPanel.applyMove(move);		
	}
	
	public void applyTile(TilePlacement t) {
		boardPanel.applyTile(t);
	}
	
	public void removeTile(TilePlacement t) {
		boardPanel.removeTile(t);
	}
	
	public void clear() {
		setTitle(ApplicationConstants.APP_NAME);
		setCurrentPlayer(null);
		file = null;
		playersPanel.clear();
		boardPanel.clear();
		logPanel.reset();
	}

	public void log(String msg) {
		logPanel.log(msg);
	}
	
	public void log(String msg, Player player) {
		logPanel.log(msg, player);
	}

	public void log(PlayerMove move, Player player) {
		logPanel.log(move, player);
		playersPanel.setPlayerScore(player, player.getScore());
	}
	
	public void addGameFrameListener(GameFrameListener listener) {
		listeners.add(GameFrameListener.class, listener);
	}
	
	public void removeGameFrameListener(GameFrameListener listener) {
		listeners.remove(GameFrameListener.class, listener);
	}
	
	protected void fireConfigurationChange(Configuration configuration) {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onDoConfigurationChange(configuration);
		}
	}
	
	protected void fireNew() {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onDoNew();
		}
	}
	
	protected void fireLoad(File file) {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onDoOpen(file);
		}
	}
	
	protected void fireSave(File file) {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onDoSave(file);
		}
	}

	protected void fireSquareClick(SquarePanel square) {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onSquareClick(square);
		}
	}	
	
	protected void firePlayPress() {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onDoStart(GameMode.PLAY);
		}
	}
	
	protected void fireRecordPress() {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onDoStart(GameMode.RECORD);
		}
	}
	
	protected void fireStopPress() {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onDoStop();
		}
	}
	
	protected void firePlayerAdded(String playerName, PlayerStrategy strategy) {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onPlayerAdded(playerName, strategy);
		}
	}
	
	protected void firePlayerChanged(int idx, String playerName, PlayerStrategy strategy) {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onPlayerChanged(idx, playerName, strategy);
		}
	}
	
	protected void firePlayerRemoved(int idx) {
		for (GameFrameListener l : listeners.getListeners(GameFrameListener.class)) {
			l.onPlayerRemoved(idx);
		}
	}

	public void setConfigControlsEnabled(boolean enabled) {
		configureMenu.setEnabled(enabled);
		toolsMenu.setEnabled(enabled || mode == GameMode.RECORD);
		playersPanel.setConfigControlsEnabled(enabled);
	}
	
	public void setGoControlEnabled(boolean enabled) {
		playersPanel.setGoControlsEnabled(enabled);
	}
	
	public void setStopControlEnabled(boolean enabled) {
		playersPanel.setStopControlEnabled(enabled);
	}

	public BoardPanel getBoard() {
		return boardPanel;
	}

	public RackPanel getRackPanel() {
		return playersPanel.getRackPanel();
	}

	public void setFile(File file) {
		this.file = file;
		setTitle(file.getName());
	}
	
	public void setDirty() {
		if (file == null) {
			setTitle("*" + ApplicationConstants.APP_NAME);
		} else {
			setTitle("*" + file.getName());
		}
	}
	
	public void setCurrentPlayer(final Player player) {
		if (clocksTimer != null) {
			clocksTimer.stop();
		}
		
		if (player != null) {
			playersPanel.updatePlayerClock(player);
			clocksTimer = new Timer(1000, new ActionListener() {			
				public void actionPerformed(ActionEvent e) {
					playersPanel.updatePlayerClock(player);
				}
			});			
			clocksTimer.start();
		}
	}
	
	public void setMode(GameMode mode) {
		this.mode = mode;
	}
}