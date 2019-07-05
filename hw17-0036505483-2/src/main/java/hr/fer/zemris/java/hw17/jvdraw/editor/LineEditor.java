package hr.fer.zemris.java.hw17.jvdraw.editor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.colorArea.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.shapes.Line;

public class LineEditor extends GeometricalObjectEditor {
	private static final long serialVersionUID = 1L;

	private Line line;

	private JTextField startLineX;
	private JTextField startLineY;
	private JTextField endLineX;
	private JTextField endLineY;

	private JColorArea colorArea;

	public LineEditor(Line line) {
		this.line = line;

		JPanel mainPanel = new JPanel();

		this.startLineX = new JTextField(String.valueOf(line.getStart().x));
		this.startLineY = new JTextField(String.valueOf(line.getStart().y));
		this.endLineX = new JTextField(String.valueOf(line.getEnd().x));
		this.endLineY = new JTextField(String.valueOf(line.getEnd().y));
		Color currColor = line.getColor();

		colorArea=new JColorArea(currColor);

		mainPanel.setLayout(new GridLayout(4, 3));

		mainPanel.add(new JLabel());
		mainPanel.add(new JLabel("x coordinate"));
		mainPanel.add(new JLabel("y coordinate"));
		
		mainPanel.add(new JLabel("start of line 	Point :"));
		mainPanel.add(startLineX);
		mainPanel.add(startLineY);
		
		mainPanel.add(new JLabel("end of line		Point :"));
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
		line.setStart(new Point(Integer.parseInt(startLineX.getText()), Integer.parseInt(startLineY.getText())));
		line.setEnd(new Point(Integer.parseInt(endLineX.getText()), Integer.parseInt(endLineY.getText())));
		
		line.setColor(colorArea.getCurrentColor());
	}

}
