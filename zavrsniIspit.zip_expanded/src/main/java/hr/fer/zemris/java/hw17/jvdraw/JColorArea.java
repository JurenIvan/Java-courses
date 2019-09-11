package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Swing component that is represented as a square whose width and height are 15
 * pixels. Color of the square is based on the currently selected color.
 * Clicking on this objects opens the {@link JColorChooser} which allows the
 * user to change the color of this component.
 * 
 * @author Marko
 *
 */
public class JColorArea extends JComponent implements IColorProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Currently selected color
	 */
	private Color selectedColor;
	/**
	 * Set of listeners that are subscribed to this object
	 */
	private Set<ColorChangeListener> listeners;
	/**
	 * Width and height of this component
	 */
	private static final int DEFAULT_SIZE = 15;

	/**
	 * Constructor
	 * 
	 * @param selectedColor default color of the component
	 */
	public JColorArea(Color selectedColor) {
		this.selectedColor = selectedColor;
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Color old = JColorArea.this.selectedColor;
				JColorArea.this.selectedColor = JColorChooser.showDialog(JColorArea.this, "Select a color", old);
				if (JColorArea.this.selectedColor == null) {
					JColorArea.this.selectedColor = old;
				}
				notifyColorChanged(old, JColorArea.this.selectedColor);
				repaint();
			}
		});

		listeners = new HashSet<>();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(DEFAULT_SIZE, DEFAULT_SIZE);
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(l);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(selectedColor);
		Insets ins = getInsets();
		g.fillRect(ins.left, ins.top, DEFAULT_SIZE - 2, DEFAULT_SIZE - 2);

	}

	/**
	 * Method that notifies all the observers that the color of the component
	 * changed.
	 * 
	 * @param old      old color
	 * @param newColor new color
	 */
	private void notifyColorChanged(Color old, Color newColor) {
		for (ColorChangeListener l : listeners) {
			l.newColorSelected(this, old, newColor);
		}
	}

}
