package hr.fer.zemris.java.hw17.jvdraw.colorArea;

import java.awt.Color;

public interface ColorChangeListener {
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}