package hdpe.scrybble.ui;

import hdpe.scrybble.game.Dictionary;
import hdpe.scrybble.game.GameContext;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * @author Ryan Pickett
 *
 */
class WordLookupFrame extends JFrame {

	private GameContext gameContext;
	
	WordLookupFrame(final GameFrame gameFrame) {
		
		super("Word Look-up");
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		final JTextField wordField = new JTextField();
		final LookupResultIndicator indicator = new LookupResultIndicator();
		final JButton findButton = new JButton("Look up");
		findButton.setMnemonic(KeyEvent.VK_L);
		
		ButtonGroup searchType = new ButtonGroup();
		final JRadioButton exactButton = new JRadioButton("Exact");
		final JRadioButton startsWithButton = new JRadioButton("Starts with");
		final JRadioButton containsButton = new JRadioButton("Contains");
		final JRadioButton regexButton = new JRadioButton("Regular expression");
		exactButton.setMnemonic(KeyEvent.VK_E);
		startsWithButton.setMnemonic(KeyEvent.VK_S);
		containsButton.setMnemonic(KeyEvent.VK_O);
		regexButton.setMnemonic(KeyEvent.VK_R);
		searchType.add(exactButton);
		searchType.add(startsWithButton);
		searchType.add(containsButton);
		searchType.add(regexButton);
		exactButton.setSelected(true);
		
		
		ActionListener findListener = new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				Dictionary dictionary = gameContext.getDictionary();
				String lookup = wordField.getText().trim().toUpperCase();
				
				if (exactButton.isSelected()) {
					indicator.setResult(dictionary.isValid(lookup));
				} else if (startsWithButton.isSelected()) {
					indicator.setResult(dictionary.getTrie().getNode(lookup) != null);
				} else if (containsButton.isSelected()) {
					for (String word : dictionary.getWords()) {
						if (word.contains(lookup)) {
							indicator.setResult(true);
							return;
						}
						indicator.setResult(false);
					}
				} else if (regexButton.isSelected()) {
					Pattern p = Pattern.compile(lookup);
					for (String word : dictionary.getWords()) {
						if (p.matcher(word).find()) {
							indicator.setResult(true);
							return;
						}
						indicator.setResult(false);
					}
				}
			}
		};		
		
		wordField.addActionListener(findListener);
		findButton.addActionListener(findListener);
		
		wordField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				indicator.setTyping();
			}			
		});
		
		final JButton closeButton = new JButton("Close");
		closeButton.setMnemonic(KeyEvent.VK_C);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WordLookupFrame.this.dispose();
			}
		});
		
		
		c.add(new JPanel() {{
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			add(new JPanel() {{
				setLayout(new SpringLayout());
				add(new JLabel("Find"));
				add(wordField);
				add(indicator);
				UIUtil.makeSprings(this, 1, 3);
			}});
			
			add(new JPanel() {{
				setLayout(new SpringLayout());
				add(exactButton);
				add(startsWithButton);
				add(containsButton);
				add(regexButton);
				UIUtil.makeSprings(this, 2, 2);
			}});
			
		}}, BorderLayout.NORTH);
		c.add(new JPanel() {{
			add(findButton);
			add(closeButton);
		}}, BorderLayout.CENTER);
		
		pack();
	}

	void setGameContext(GameContext gameContext) {
		this.gameContext = gameContext;
	}
	
	private static class LookupResultIndicator extends JPanel {
		JLabel label = new JLabel();

		Icon typingIcon = UIUtil.loadIcon("keyboard.png");
		Icon successIcon = UIUtil.loadIcon("tick.png");
		Icon failIcon = UIUtil.loadIcon("cross.png");
		
		LookupResultIndicator() {
			add(label);
			setMaximumSize(new Dimension(16, 16));
		}
		
		void setResult(boolean success) {
			if (success) {
				label.setIcon(successIcon);
			} else {
				label.setIcon(failIcon);
			}
		}
		
		void setTyping() {
			label.setIcon(typingIcon);
		}
	}
}
