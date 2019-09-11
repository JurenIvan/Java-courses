package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw17.jvdraw.colorArea.ColorChangeListener;
import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;

/**
 * Custom implementation of JLabel (this class extends it), where text is
 * automaticaly generated and shows RGB of current foreground and background colors.
 * 
 * @author juren
 *
 */
public class BottomStatusLabel extends JLabel implements ColorChangeListener {
	private static final long serialVersionUID = 1L;
	/**
	 * {@link IColorProvider} for foreground color
	 */
	private IColorProvider fgColorProvider;
	/**
	 * {@link IColorProvider} for background color
	 */
	private IColorProvider bgColorProvider;

	/**
	 * Standard constructor that registers instance of class to list of listeners
	 * 
	 * @param fgColorProvider {@link IColorProvider} for foreground color
	 * @param bgColorProvider {@link IColorProvider} for background color
	 */
	public BottomStatusLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		this.bgColorProvider = bgColorProvider;
		this.fgColorProvider = fgColorProvider;

		fgColorProvider.addColorChangeListener(this);
		bgColorProvider.addColorChangeListener(this);

	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		this.setText(displayText(fgColorProvider.getCurrentColor(), bgColorProvider.getCurrentColor()));
	}

	/**
	 * Method that returns string that will be shown in {@link JLabel}
	 * 
	 * @param fgColor foreground color
	 * @param bgColor background color
	 * @return string that will be shown in {@link JLabel}
	 */
	private String displayText(Color fgColor, Color bgColor) {
		return String.format("Foreground color: (%d, %d, %d)	 Background color: (%d, %d, %d).", fgColor.getRed(),
				fgColor.getGreen(), fgColor.getBlue(), bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
	}

}
