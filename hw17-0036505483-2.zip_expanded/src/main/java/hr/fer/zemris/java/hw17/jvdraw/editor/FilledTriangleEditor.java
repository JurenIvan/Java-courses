package hr.fer.zemris.java.hw17.jvdraw.editor;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.colorArea.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FilledTriangle;

public class FilledTriangleEditor extends GeometricalObjectEditor {

	private JTextField[] x;
	private JTextField[] y;

	private JColorArea colorAreaFg;
	private JColorArea colorAreaBg;

	private FilledTriangle filledTriangle;

	private static final long serialVersionUID = 1L;

	public FilledTriangleEditor(FilledTriangle filledTriangle) {
		this.filledTriangle = filledTriangle;

		JPanel mainPanel = new JPanel();

		for (int i = 0; i < 3; i++) {
			this.x[i] = new JTextField(String.valueOf(filledTriangle.getX()[i]));
		}
		for (int i = 0; i < 3; i++) {
			this.y[i] = new JTextField(String.valueOf(filledTriangle.getY()[i]));
		}

		Color fillColor = filledTriangle.getFillColor();
		Color outerColor = filledTriangle.getOutlineColor();

		colorAreaFg = new JColorArea(outerColor);
		colorAreaBg = new JColorArea(fillColor);

		mainPanel.setLayout(new GridLayout(6, 3));

		mainPanel.add(new JLabel());
		mainPanel.add(new JLabel("x coordinate"));
		mainPanel.add(new JLabel("y coordinate"));

		mainPanel.add(new JLabel("point of Triangle 		Point :"));
		mainPanel.add(x[0]);
		mainPanel.add(y[0]);

		mainPanel.add(new JLabel("point of Triangle 		Point :"));
		mainPanel.add(x[1]);
		mainPanel.add(y[1]);

		mainPanel.add(new JLabel("point of Triangle 		Point :"));
		mainPanel.add(x[2]);
		mainPanel.add(y[2]);

		mainPanel.add(new JLabel("Outher color: "));
		mainPanel.add(colorAreaFg);
		mainPanel.add(new JLabel());

		mainPanel.add(new JLabel("Fill color: "));
		mainPanel.add(colorAreaBg);
		mainPanel.add(new JLabel());

		this.add(mainPanel);
	}

	@Override
	public void checkEditing() {
		try {
			for (int i = 0; i < 3; i++) {
				Integer.parseInt(x[i].getText());
				Integer.parseInt(y[i].getText());
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Wrong Number format!");
		}
	}

	@Override
	public void acceptEditing() {
		int[] xN = new int[3];
		int[] yN = new int[3];

		for (int i = 0; i < 3; i++) {
			xN[i] = Integer.parseInt(x[i].getText());
			yN[i] = Integer.parseInt(y[i].getText());
		}
		filledTriangle.setX(xN);
		filledTriangle.setY(yN);

		filledTriangle.setFillColor(colorAreaFg.getCurrentColor());
		filledTriangle.setOutlineColor(colorAreaBg.getCurrentColor());

	}

}
