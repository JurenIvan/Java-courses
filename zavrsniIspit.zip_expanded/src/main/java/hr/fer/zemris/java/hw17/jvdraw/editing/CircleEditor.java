package hr.fer.zemris.java.hw17.jvdraw.editing;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw17.jvdraw.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;

/**
 * Implementation of {@link GeometricalObjectEditor} that allows user to edit
 * the {@link CircleEditor} objects.
 * 
 * @author Marko
 *
 */
public class CircleEditor extends GeometricalObjectEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Circle that this editor is editing
	 */
	private Circle circle;
	/**
	 * {@link JTextArea} that contains information of x of the center point
	 */
	private JTextArea centerX;
	/**
	 * {@link JTextArea} that contains information of y of the center point
	 */
	private JTextArea centerY;
	/**
	 * {@link JTextArea} that contains information of radius of the circle
	 */
	private JTextArea radius;
	/**
	 * Color area that contains the user's selected foreground color.
	 */
	private JColorArea selectedColor;

	/**
	 * Constructor that initializes all the objects with values that are currently
	 * stored in the given Circle.
	 * 
	 * @param circle {@link Circle} that should be edited.
	 */
	public CircleEditor(Circle circle) {
		this.circle = circle;
		this.centerX = new JTextArea(Integer.toString(circle.getCenter().x));
		this.centerY = new JTextArea(Integer.toString(circle.getCenter().y));
		this.radius = new JTextArea(Integer.toString(circle.getRadius()));
		this.selectedColor = new JColorArea(circle.getFgColor());

		initWindow();
	}

	/**
	 * Method that creates the layout of the window.
	 */
	private void initWindow() {
		this.setLayout(new GridLayout(4, 2, 10, 10));
		this.add(new JLabel("Center x: "));
		this.add(centerX);
		this.add(new JLabel("Center y: "));
		this.add(centerY);
		this.add(new JLabel("Radius : "));
		this.add(radius);
		this.add(new JLabel("Color: "));
		this.add(selectedColor);
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(centerX.getText());
			Integer.parseInt(centerY.getText());
			Integer.parseInt(radius.getText());
		} catch (NumberFormatException exc) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void acceptEditing() {
		int x1 = Integer.parseInt(centerX.getText());
		int y1 = Integer.parseInt(centerY.getText());
		int rad = Integer.parseInt(radius.getText());
		circle.updateCircle(x1, y1, rad, selectedColor.getCurrentColor());

	}

}
