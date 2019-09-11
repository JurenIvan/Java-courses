package hr.fer.zemris.java.hw17.jvdraw.editing;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw17.jvdraw.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.objects.Triangle;

public class TriangleEditor extends GeometricalObjectEditor {

	private JColorArea bgColor;
	/**
	 * Color area that contains the user's selected foreground color.
	 */
	private JColorArea fgColor;
	
	private Triangle triangle;
	
	public TriangleEditor(Triangle triangle) {
		this.triangle = triangle;
		this.bgColor = new JColorArea(triangle.ispuna);
		this.fgColor = new JColorArea(triangle.rub);

		initWindow();
	}
	
	private void initWindow() {
		this.setLayout(new GridLayout(2, 2, 10, 10));
		this.add(new JLabel("BgColor: "));
		this.add(bgColor);
		this.add(new JLabel("FgColor: "));
		this.add(fgColor);
		
	}
	
	@Override
	public void checkEditing() {
		return;
	}

	@Override
	public void acceptEditing() {
		triangle.update(bgColor.getCurrentColor(), fgColor.getCurrentColor());
	}

}
