package hdpe.scrybble.ui;

import java.awt.Component;
import java.awt.Container;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

/**
 * @author Ryan Pickett
 * 
 */
public abstract class UIUtil {

	private UIUtil() {

	}

	/**
	 * @param name
	 * @return
	 */
	public static Icon loadIcon(String name) {
		try {
			return new ImageIcon(ImageIO.read(UIUtil.class.getResource(name)));
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load icon: " + name, e);
		}
	}

	/**
	 * @param parent
	 * @param rows
	 * @param cols
	 */
	public static void makeSprings(Container parent, int rows, int cols) {
		
		SpringLayout layout = (SpringLayout) parent.getLayout();
		
		final int cellSpacing = 5;
		
		Spring x = Spring.constant(cellSpacing);
		for (int c = 0; c < cols; c++) {
			Spring maxWidth = Spring.constant(0);
			for (int r = 0; r < rows; r++) {
				maxWidth = Spring.max(maxWidth, getCellConstraints(r, c, parent, cols).getWidth());
			}
			for (int r = 0; r < rows; r++) {
				Constraints cellCons = getCellConstraints(r, c, parent, cols);
				cellCons.setX(x);
				cellCons.setWidth(maxWidth);
			}
			x = Spring.sum(x, Spring.sum(maxWidth, Spring.constant(cellSpacing)));
		}

		Spring y = Spring.constant(cellSpacing);
		for (int r = 0; r < rows; r++) {
			Spring maxHeight = Spring.constant(0);
			for (int c = 0; c < cols; c++) {
				maxHeight = Spring.max(maxHeight, getCellConstraints(r, c, parent, cols).getHeight());
			}
			for (int c = 0; c < cols; c++) {
				Constraints cellCons = getCellConstraints(r, c, parent, cols);
				cellCons.setY(y);
				cellCons.setHeight(maxHeight);
			}
			y = Spring.sum(y, Spring.sum(maxHeight, Spring.constant(cellSpacing)));
		}

		Constraints parentCons = layout.getConstraints(parent);
		parentCons.setConstraint(SpringLayout.SOUTH, y);
		parentCons.setConstraint(SpringLayout.EAST, x);
	}

	private static Constraints getCellConstraints(int row,
			int col, Container parent, int cols) {
		SpringLayout layout = (SpringLayout) parent.getLayout();
		Component c = parent.getComponent(row * cols + col);
		return layout.getConstraints(c);
	}
}