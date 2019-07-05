package hr.fer.zemris.java.hw17.jvdraw.colorArea;

import java.awt.Color;

public interface IColorProvider {
	public Color getCurrentColor();

	public void addColorChangeListener(ColorChangeListener l);

	public void removeColorChangeListener(ColorChangeListener l);
}
