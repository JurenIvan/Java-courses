package hr.fer.zemris.java.hw17.jvdraw.editor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.colorArea.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Circle;

public class CircleEditor extends GeometricalObjectEditor {
	private static final long serialVersionUID = 1L;

	private Circle circle;

	private JTextField startLineX;
	private JTextField startLineY;
	private JTextField endLineX;
	private JTextField endLineY;

	private JColorArea colorArea;

	public CircleEditor(Circle circle) {
		this.circle = circle;

		JPanel mainPanel = new JPanel();

		this.startLineX = new JTextField(String.valueOf(circle.getCenter().x));
		this.startLineY = new JTextField(String.valueOf(circle.getCenter().y));
		this.endLineX = new JTextField(String.valueOf(circle.getOther().x));
		this.endLineY = new JTextField(String.valueOf(circle.getOther().y));
		Color currColor = circle.getOutlineColor();
		
		colorArea=new JColorArea(currColor);

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
