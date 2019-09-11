package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * Label that displays the selected foreground and background colors of the
 * {@link JDrawingCanvas} that are currently selected. This class implements
 * {@link ColorChangeListener} and updates its text every time the object that
 * it is subscribed to changes its value.
 * 
 * @author Marko
 *
 */
public class ColorLabel extends JLabel implements ColorChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Object that is providing the foreground color of the canvas.
	 */
	private IColorProvider fgColorProvider;
	/**
	 * Object that is providing the background color of the canvas.
	 */
	private IColorProvider bgColorProvider;
	/**
	 * String that should be formatted with the appropriate background and
	 * foreground values and set as the text of label.
	 */
	private static final String COLOR_STRING = "Foreground color: (%d, %d, %d), background color: (%d, %d, %d).";

	/**
	 * Constructor
	 * 
	 * @param fgColorProvider provider for foreground color
	 * @param bgColorProvider provider for background color
	 */
	public ColorLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		super();
		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;
		fgColorProvider.addColorChangeListener(this);
		bgColorProvider.addColorChangeListener(this);
		updateColor();
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		updateColor();
	}

	/**
	 * Method that is called when the color changes in one of the objects that this
	 * object is subscribed to. Changes the text of the label to match the currently
	 * selected colors of foreground and background providers.
	 */
	private void updateColor() {
		Color fg = fgColorProvider.getCurrentColor();
		Color bg = bgColorProvider.getCurrentColor();
		this.setText(String.format(COLOR_STRING, fg.getRed(), fg.getGreen(), fg.getBlue(), bg.getRed(), bg.getGreen(),
				bg.getBlue()));
	}

}
