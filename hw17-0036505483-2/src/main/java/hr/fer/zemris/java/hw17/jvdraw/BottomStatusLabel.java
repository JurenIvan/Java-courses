package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw17.jvdraw.colorArea.ColorChangeListener;
import hr.fer.zemris.java.hw17.jvdraw.colorArea.IColorProvider;

public class BottomStatusLabel extends JLabel implements ColorChangeListener {
	private static final long serialVersionUID = 1L;

	private IColorProvider fgColorProvider;
	private IColorProvider bgColorProvider;

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

	private String displayText(Color fgColor, Color bgColor) {
		return String.format("Foreground color: (%d, %d, %d)	 Background color: (%d, %d, %d).", fgColor.getRed(),
				fgColor.getGreen(), fgColor.getBlue(), bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
	}
	
}
