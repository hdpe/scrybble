package hdpe.scrybble.ui;

import hdpe.scrybble.ai.simple.SimpleAIStrategy;
import hdpe.scrybble.game.GameContext;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerStrategy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author Ryan Pickett
 *
 */
class PlayersPanel extends JPanel {
	
	private GameFrame gameFrame;
	private Map<Player, PlayerPanel> playerPanels = new HashMap<Player, PlayerPanel>();
	private JButton addPlayerButton;
	private JButton playButton;
	private JButton recordButton;
	private JButton stopButton;

	private GameContext gameContext;
	
	private RackPanel rackPanel;
	
	private JPanel fieldsPanel;
	
	PlayersPanel(GameFrame board) {
		this.gameFrame = board;
		
		setLayout(new BorderLayout());
		
		fieldsPanel = new JPanel() {{
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}};
		
		addPlayerButton = new JButton("Add player", UIUtil.loadIcon("add.png"));
		addPlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String playerName = "Player " + (playerPanels.size() + 1);
				PlayerStrategy strategy = new SimpleAIStrategy();
				
				PlayerPanel playerPanel = new PlayerPanel(PlayersPanel.this, 
						playerName, strategy, gameContext);
				gameFrame.firePlayerAdded(playerName, strategy);
				fieldsPanel.add(playerPanel);
			}			
		});
		
		playButton = new JButton("Play", UIUtil.loadIcon("control_play.png"));
		playButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					gameFrame.firePlayPress();
				} catch (Exception e1) {
					e1.printStackTrace();
					
					JOptionPane.showMessageDialog(gameFrame, e1.getClass().getName() + ": " + e1.getMessage(), "Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		recordButton = new JButton("Record", UIUtil.loadIcon("stop.png"));
		recordButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					gameFrame.fireRecordPress();
				} catch (Exception e1) {
					e1.printStackTrace();
					
					JOptionPane.showMessageDialog(gameFrame, e1.getClass().getName() + ": " + e1.getMessage(), "Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		stopButton = new JButton("Pause", UIUtil.loadIcon("control_pause.png"));
		stopButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				gameFrame.fireStopPress();
			}
		});

		add(new JPanel() {{
			setLayout(new BorderLayout());
			add(fieldsPanel, BorderLayout.NORTH);
			add(new JPanel() {{
				add(addPlayerButton);
				add(recordButton);
				add(playButton);				
				add(stopButton);
			}}, BorderLayout.CENTER);
		}}, BorderLayout.NORTH);
		
		rackPanel = new RackPanel();
		rackPanel.setVisible(false);
		
		add(new JPanel() {{
			add(rackPanel);
		}}, BorderLayout.CENTER);
	}

	RackPanel getRackPanel() {
		return rackPanel;
	}
	
	void clear() {
		rackPanel.setVisible(false);
		for (Component playerPanel : fieldsPanel.getComponents()) {
			((PlayerPanel)playerPanel).setScore(0);
			((PlayerPanel)playerPanel).setClock(gameContext.getConfiguration()
					.getTimePerPlayerSeconds() * 1000);
		}
	}
	
	void removePlayerPanel(PlayerPanel playerPanel) {
		playerPanels.remove(playerPanel);
		fieldsPanel.remove(playerPanel);
		revalidate();
		repaint();
	}
	
	void setGameContext(GameContext gameContext) {
		this.gameContext = gameContext;
		fieldsPanel.removeAll();
		playerPanels.clear();
		for (Player player : gameContext.getPlayers()) {
			PlayerPanel pp = new PlayerPanel(this, player.getName(), 
					player.getStrategy(), gameContext);
			playerPanels.put(player, pp);
			fieldsPanel.add(pp);
		}
		clear();
	}
	
	void setPlayerScore(Player player, int score) {
		playerPanels.get(player).setScore(score);
	}
	
	void updatePlayerClock(Player player) {
		playerPanels.get(player).setClock(player.getClock().getTimeRemaining());
	}
	
	void setGoControlsEnabled(boolean b) {
		playButton.setEnabled(b);
		recordButton.setEnabled(b);
		if (b) {
			rackPanel.setVisible(false);
		}
	}
	
	void setStopControlEnabled(boolean b) {
		stopButton.setEnabled(b);
	}
	
	void setConfigControlsEnabled(boolean b) {
		addPlayerButton.setEnabled(b);
		for (Component playerPanel : fieldsPanel.getComponents()) {
			((PlayerPanel)playerPanel).setEnabled(b);
		}
	}

	void firePlayerRemoved(PlayerPanel playerPanel) {
		gameFrame.firePlayerRemoved(indexOf(playerPanel));
	}

	void firePlayerChanged(PlayerPanel playerPanel, String playerName,
			PlayerStrategy strategy) {
		gameFrame.firePlayerChanged(indexOf(playerPanel), playerName, strategy);
	}
	
	private int indexOf(PlayerPanel playerPanel) {
		Component[] c = fieldsPanel.getComponents();
		for (int i = 0; i < c.length; i ++) {
			if (c[i] == playerPanel) {
				return i;
			}
		}
		return -1;
	}
}