package hr.fer.zemris.java.hw17.jvdraw.editing;

import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw17.jvdraw.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * Implementation of {@link GeometricalObjectEditor} that allows user to edit
 * the {@link Line} objects.
 * 
 * @author Marko
 *
 */
public class LineEditor extends GeometricalObjectEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Line that this editor is editing
	 */
	private Line line;
	/**
	 * {@link JTextArea} that contains information of x of the starting point
	 */
	private JTextArea startX;
	/**
	 * {@link JTextArea} that contains information of y of the starting point
	 */
	private JTextArea startY;
	/**
	 * {@link JTextArea} that contains information of x of the ending point
	 */
	private JTextArea endX;
	/**
	 * {@link JTextArea} that contains information of y of the ending point
	 */
	private JTextArea endY;
	/**
	 * Color area that contains the user's selected color.
	 */
	private JColorArea selectedColor;

	/**
	 * Constructor that initializes all the objects with values that are currently
	 * stored in the given line.
	 * 
	 * @param line line that should be edited.
	 */
	public LineEditor(Line line) {
		Point start = line.getX();
		Point end = line.getY();
		this.line = line;
		this.startX = new JTextArea(Integer.toString(start.x));
		this.startY = new JTextArea(Integer.toString(start.y));
		this.endX = new JTextArea(Integer.toString(end.x));
		this.endY = new JTextArea(Integer.toString(end.y));
		this.selectedColor = new JColorArea(line.getFgColor());

		initWindow();
	}

	/**
	 * Method that creates the layout of the window.
	 */
	private void initWindow() {
		this.setLayout(new GridLayout(5, 2, 10, 10));
		this.add(new JLabel("x1: "));
		this.add(startX);
		this.add(new JLabel("y1: "));
		this.add(startY);
		this.add(new JLabel("x2: "));
		this.add(endX);
		this.add(new JLabel("y2: "));
		this.add(endY);
		this.add(new JLabel("Color: "));
		this.add(selectedColor);
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(startX.getText());
			Integer.parseInt(startY.getText());
			Integer.parseInt(endX.getText());
			Integer.parseInt(endY.getText());
		} catch (NumberFormatException exc) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void acceptEditing() {
		int x1 = Integer.parseInt(startX.getText());
		int y1 = Integer.parseInt(startY.getText());
		int x2 = Integer.parseInt(endX.getText());
		int y2 = Integer.parseInt(endY.getText());
		line.updateLine(x1, y1, x2, y2, selectedColor.getCurrentColor());
	}

}
