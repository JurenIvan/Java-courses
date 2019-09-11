package hr.fer.zemris.java.hw17.jvdraw.editor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.colorArea.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw17.jvdraw.shapes.GeometricalObject;

/**
 * Class that is concrete implementation of {@link GeometricalObjectEditor} for
 * {@link GeometricalObject} of type Circle.
 * 
 * @author juren
 *
 */
public class CircleEditor extends GeometricalObjectEditor {
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to circle that is edited
	 */
	private Circle circle;

	/**
	 * {@link JTextField} used for input of x coordinate of start
	 */
	private JTextField startLineX;
	/**
	 * {@link JTextField} used for input of y coordinate of start
	 */
	private JTextField startLineY;
	/**
	 * {@link JTextField} used for input of x coordinate of end
	 */
	private JTextField endLineX;
	/**
	 * {@link JTextField} used for input of y coordinate of end
	 */
	private JTextField endLineY;

	/**
	 * {@link JColorArea} used for input of color
	 */
	private JColorArea colorArea;

	/**
	 * Constructor that is used to setUp gui.
	 * 
	 * @param circles
	 */
	public CircleEditor(Circle circle) {
		this.circle = circle;

		JPanel mainPanel = new JPanel();

		this.startLineX = new JTextField(String.valueOf(circle.getCenter().x));
		this.startLineY = new JTextField(String.valueOf(circle.getCenter().y));
		this.endLineX = new JTextField(String.valueOf(circle.getOther().x));
		this.endLineY = new JTextField(String.valueOf(circle.getOther().y));
		Color currColor = circle.getOutlineColor();

		colorArea = new JColorArea(currColor);

		mainPanel.setLayout(new GridLayout(4, 3));

		mainPanel.add(new JLabel());
		mainPanel.add(new JLabel("x coordinate"));
		mainPanel.add(new JLabel("y coordinate"));
		mainPanel.add(new JLabel("center of Circle 		Point :"));
		mainPanel.add(startLineX);
		mainPanel.add(startLineY);
		mainPanel.add(new JLabel("other point of Circle	Point :"));
		mainPanel.add(endLineX);
		mainPanel.add(endLineY);

		mainPanel.add(new JLabel("color: "));
		mainPanel.add(colorArea);
		mainPanel.add(new JLabel("Select color"));

		this.add(mainPanel);
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(startLineX.getText());
			Integer.parseInt(startLineY.getText());
			Integer.parseInt(endLineX.getText());
			Integer.parseInt(endLineY.getText());
		} catch (Exception e) {
			throw new IllegalArgumentException("Wrong Number format!");
		}

	}

	@Override
	public void acceptEditing() {
		circle.setCenter(new Point(Integer.parseInt(startLineX.getText()), Integer.parseInt(startLineY.getText())));
		circle.setOther(new Point(Integer.parseInt(endLineX.getText()), Integer.parseInt(endLineY.getText())));

		circle.setOutlineColor(colorArea.getCurrentColor());
	}

}
