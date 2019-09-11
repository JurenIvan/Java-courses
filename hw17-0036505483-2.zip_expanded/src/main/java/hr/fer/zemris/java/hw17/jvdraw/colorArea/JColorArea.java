package hr.fer.zemris.java.hw17.jvdraw.colorArea;

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
 * Custom {@link JComponent} that is used to select color. Opens JColorPicker
 * and remembers it's selection
 * 
 * @author juren
 *
 */
public class JColorArea extends JComponent implements IColorProvider {
	private static final long serialVersionUID = 1L;
	/**
	 * Constant for icon width
	 */
	private static final int DEFAULT_WIDTH = 15;
	/**
	 * Constant for icon height
	 */
	private static final int DEFAULT_HEIGTH = 15;

	/**
	 * variable storing reference to currently selected color
	 */
	private Color selectedColor;

	/**
	 * set of listeners that are registered to this object
	 */
	private Set<ColorChangeListener> listeners;

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGTH);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	/**
	 * Standard constructor that also initializes GUI
	 * 
	 * @param selectedColor currently selected color
	 */
	public JColorArea(Color selectedColor) {
		this.selectedColor = selectedColor;
		listeners = new HashSet<>();

		this.setVisible(true);
		this.setBackground(selectedColor);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color c = JColorChooser.showDialog(null, "Choose a color", selectedColor);
				if (c != null) {
					setSelectedColor(c);
				}
			}
		});
	}

	/**
	 * Standard setter that remembers current and previous state
	 * 
	 * @param selectedColor
	 */
	public void setSelectedColor(Color selectedColor) {
		Color oldColor = this.selectedColor;
		this.selectedColor = selectedColor;
		this.setBackground(selectedColor);

		for (var listener : listeners) {
			listener.newColorSelected(this, oldColor, selectedColor);
		}
	}

	@Override
	public Color getCurrentColor() {
		return this.selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		Set<ColorChangeListener> newSet = new HashSet<ColorChangeListener>(listeners);
		newSet.add(l);
		listeners = newSet;
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		Set<ColorChangeListener> newSet = new HashSet<ColorChangeListener>(listeners);
		newSet.remove(l);
		listeners = newSet;
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(selectedColor);
		Insets ins = getInsets();
		g.fillRect(ins.left, ins.top, DEFAULT_WIDTH - (ins.left + ins.right), DEFAULT_HEIGTH - (ins.bottom + ins.top));
	}
}
