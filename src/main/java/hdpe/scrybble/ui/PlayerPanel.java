package hdpe.scrybble.ui;

import hdpe.scrybble.ai.simple.SimpleAIStrategy;
import hdpe.scrybble.game.GameContext;
import hdpe.scrybble.game.PlayerStrategy;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 * @author Ryan Pickett
 *
 */
class PlayerPanel extends JPanel {

	private PlayersPanel playersPanel;
	
	private String playerName;
	private PlayerStrategy playerStrategy;
		
	private JLabel playerIconLabel;
	private JLabel nameLabel;
	private JComboBox strategyField;
	private JLabel scoreLabel;
	private JLabel clockLabel;
	private JButton editButton;
	private JButton deleteButton;
	
	private final Icon computerIcon = UIUtil.loadIcon("computer.png");
	private final Icon humanIcon = UIUtil.loadIcon("user.png");
	
	private final StrategyOption humanOption = new StrategyOption("Human player", HumanStrategy.class);
	private final StrategyOption simpleAiOption = new StrategyOption("Simple AI player", SimpleAIStrategy.class);
	
	PlayerPanel(final PlayersPanel playersPanel, String playerName, 
			PlayerStrategy playerStrategy, 
			final GameContext gameContext) {
		
		this.playersPanel = playersPanel;
		
		this.playerName = playerName;
		this.playerStrategy = playerStrategy;
		
		setLayout(new SpringLayout());
		
		playerIconLabel = new JLabel();
		nameLabel = new JLabel(playerName);
		strategyField = new JComboBox(new StrategyComboBoxModel());
		strategyField.setEditable(true);
		
		if (playerStrategy instanceof HumanStrategy) {
			strategyField.setSelectedItem(humanOption);
			playerIconLabel.setIcon(humanIcon);
		} else if (playerStrategy instanceof SimpleAIStrategy) {
			strategyField.setSelectedItem(simpleAiOption);
			playerIconLabel.setIcon(computerIcon);
		} else {
			strategyField.setSelectedItem(playerStrategy.getClass().getName());
			playerIconLabel.setIcon(humanIcon);
		}

		strategyField.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				updateStrategy();
			}
		});
		
		strategyField.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				updateStrategy();
			}
		});
		
		add(playerIconLabel);
		add(new JPanel() {{
			add(nameLabel);
			setMinimumSize(new Dimension(100, 16));
			setPreferredSize(new Dimension(100, 16));
			setMaximumSize(new Dimension(100, 16));
		}});		
		add(strategyField);
		scoreLabel = new JLabel();
		add(new JPanel() {{
			add(scoreLabel);
			setMinimumSize(new Dimension(30, 16));
			setPreferredSize(new Dimension(30, 16));
		}});
		clockLabel = new JLabel();
		add(new JPanel() {{
			add(clockLabel);
			setClock(gameContext.getConfiguration().getTimePerPlayerSeconds() * 1000);
			setMinimumSize(new Dimension(60, 16));
			setPreferredSize(new Dimension(60, 16));
		}});
		editButton = new JButton(UIUtil.loadIcon("pencil.png"));
		editButton.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				
				String val = (String)JOptionPane.showInputDialog(PlayerPanel.this, 
						"Player name:", "Edit Player", JOptionPane.QUESTION_MESSAGE,
						null, null, PlayerPanel.this.playerName);
				
				if (val != null) {
					val = val.trim();
					if (!val.isEmpty()) {
						PlayerPanel.this.playerName = val;
						nameLabel.setText(val);
						playersPanel.firePlayerChanged(PlayerPanel.this, val, 
								PlayerPanel.this.playerStrategy);
					}
				}					
			}				
		});
		add(editButton);
		deleteButton = new JButton(UIUtil.loadIcon("delete.png"));
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				playersPanel.removePlayerPanel(PlayerPanel.this);
				playersPanel.firePlayerRemoved(PlayerPanel.this);
			}
		});
		add(deleteButton);
		
		UIUtil.makeSprings(this, 1, 7);
	}
	
	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		strategyField.setEnabled(enable);
		editButton.setEnabled(enable);
		deleteButton.setEnabled(enable);
	}
	
	void setScore(int score) {
		scoreLabel.setText(String.valueOf(score));
	}
	
	void setClock(long timeRemaining) {
		int secondsRemaining = (int)(timeRemaining / 1000);
		
		DecimalFormat df = new DecimalFormat("00"); 
		
		String txt = (secondsRemaining < 0 ? "-" : "") + 
				df.format(Math.abs(secondsRemaining / 60)) + 
				":" + 
				df.format(Math.abs(secondsRemaining % 60));
		
//		System.out.println(txt);
		
		clockLabel.setText(txt);
	}

	private void updateStrategy() {
		try {
			Object selectedItem = strategyField.getSelectedItem();
			
			if (selectedItem instanceof StrategyOption) {
				selectedItem = ((StrategyOption)selectedItem).clazz.getName();
			} 
			
			playerStrategy = (PlayerStrategy)Class.forName((String)selectedItem).newInstance();			
			playerIconLabel.setIcon(strategyField.getSelectedItem() == humanOption ? humanIcon : computerIcon);			
			playersPanel.firePlayerChanged(this, playerName, playerStrategy);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(PlayerPanel.this, ex, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	String getPlayerName() {
		return playerName;
	}
		
	PlayerStrategy getPlayerStrategy() {
		return playerStrategy;
	}
	
	private class StrategyComboBoxModel extends DefaultComboBoxModel {
		public StrategyComboBoxModel() {
			addElement(humanOption);
			addElement(simpleAiOption);			
		}
	}

	private static class StrategyOption {
		String label;
		Class<? extends PlayerStrategy> clazz;
		
		StrategyOption(String label, Class<? extends PlayerStrategy> clazz) {
			this.label = label;
			this.clazz = clazz;
		}
		
		public String toString() {
			return label;
		}
	}
}