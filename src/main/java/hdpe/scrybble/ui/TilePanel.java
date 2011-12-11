package hdpe.scrybble.ui;

import hdpe.scrybble.game.TileState;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 * @author Ryan Pickett
 *
 */
class TilePanel extends JPanel {
	
	private static Color TILE_COLOUR = new Color(0xf3eb94);

	private static Font LETTER_FONT = new Font("Arial", Font.PLAIN, 18);
	private static Font VALUE_FONT = new Font("Arial", Font.PLAIN, 9);
	
	TilePanel(TileState tile, char letter) {
		setBackground(TILE_COLOUR);
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		setLayout(null);
		Color textColor = tile.isBlank() ? Color.GRAY : Color.BLACK;
		
		JLabel letterLabel = new JLabel(String.valueOf(letter));
		letterLabel.setFont(LETTER_FONT);
		letterLabel.setForeground(textColor);
		letterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		letterLabel.setVerticalAlignment(SwingConstants.CENTER);
		letterLabel.setBounds(0, 0, 20, 30);
		add(letterLabel);
		
		if (!tile.isBlank()) {
			JLabel valueLabel = new JLabel(String.valueOf(tile.getValue()));
			valueLabel.setFont(VALUE_FONT);
			valueLabel.setBounds(16, 14, 14, 16);
			valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
			valueLabel.setVerticalAlignment(SwingConstants.CENTER);
			add(valueLabel);
		}		
	}
}