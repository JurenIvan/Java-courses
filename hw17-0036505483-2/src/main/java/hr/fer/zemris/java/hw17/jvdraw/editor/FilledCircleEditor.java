package hr.fer.zemris.java.hw17.jvdraw.editor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.colorArea.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledCircle;

public class FilledCircleEditor extends GeometricalObjectEditor {
	private static final long serialVersionUID = 1L;

	private FilledCircle filledCircle;

	private JTextField startLineX;
	private JTextField startLineY;
	private JTextField endLineX;
	private JTextField endLineY;

	private JColorArea colorAreaFg;
	private JColorArea colorAreaBg;

	public FilledCircleEditor(FilledCircle filledCircle) {
		this.filledCircle = filledCircle;

		JPanel mainPanel = new JPanel();

		this.startLineX = new JTextField(String.valueOf(filledCircle.getCenter().x));
		this.startLineY = new JTextField(String.valueOf(filledCircle.getCenter().y));
		this.endLineX = new JTextField(String.valueOf(filledCircle.getOther().x));
		this.endLineY = new JTextField(String.valueOf(filledCircle.getOther().y));
		Color fillColor = filledCircle.getFillColor();
		Color outerColor = filledCircle.getOutlineColor();

		colorAreaFg = new JColorArea(outerColor);
		colorAreaBg = new JColorArea(fillColor);
		mainPanel.setLayout(new GridLayout(5, 3));

		mainPanel.add(new JLabel());
		mainPanel.add(new JLabel("x coordinate"));
		mainPanel.add(new JLabel("y coordinate"));

		mainPanel.add(new JLabel("center of Circle 		Point :"));
		mainPanel.add(startLineX);
		mainPanel.add(startLineY);

		mainPanel.add(new JLabel("other point of Circle	Point :"));
		mainPanel.add(endLineX);
		mainPanel.add(endLineY);

		mainPanel.add(new JLabel("Outline color: "));
		mainPanel.add(colorAreaFg);
		mainPanel.add(new JLabel());

		mainPanel.add(new JLabel("Fill color: "));
		mainPanel.add(colorAreaBg);
		mainPanel.add(new JLabel());

		this.add(mainPanel);

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
		filledCircle
				.setCenter(new Point(Integer.parseInt(startLineX.getText()), Integer.parseInt(startLineY.getText())));
		filledCircle.setOther(new Point(Integer.parseInt(endLineX.getText()), Integer.parseInt(endLineY.getText())));

		filledCircle.setOutlineColor(colorAreaFg.getCurrentColor());
		filledCircle.setFillColor(colorAreaBg.getCurrentColor());

	}

}
