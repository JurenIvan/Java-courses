package hr.fer.zemris.java.hw17.jvdraw.editor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.colorArea.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.shapes.FTriangle;

public class FTriangleEditor extends GeometricalObjectEditor {
	private static final long serialVersionUID = 1L;

	private FTriangle fTriangle;

	private JTextField X0;
	private JTextField Y0;
	private JTextField X1;
	private JTextField Y1;
	private JTextField X2;
	private JTextField Y2;
	
	private JColorArea colorAreaFg;
	private JColorArea colorAreaBg;
	
	
	public FTriangleEditor(FTriangle fTriangle) {
		this.fTriangle=fTriangle;
		
		JPanel mainPanel = new JPanel();
		
		this.X0 = new JTextField(String.valueOf(fTriangle.getpFirst().x));
		this.Y0 = new JTextField(String.valueOf(fTriangle.getpFirst().y));
		this.X1 = new JTextField(String.valueOf(fTriangle.getpSecond().x));
		this.Y1 = new JTextField(String.valueOf(fTriangle.getpSecond().y));
		this.X2 = new JTextField(String.valueOf(fTriangle.getpThird().x));
		this.Y2 = new JTextField(String.valueOf(fTriangle.getpThird().y));
		
		Color fillColor = fTriangle.getFillColor();
		Color outerColor = fTriangle.getOutlineColor();
		
		colorAreaFg = new JColorArea(outerColor);
		colorAreaBg = new JColorArea(fillColor);
		
		mainPanel.setLayout(new GridLayout(7, 3));
		
		mainPanel.add(new JLabel());
		mainPanel.add(new JLabel("x coordinate"));
		mainPanel.add(new JLabel("y coordinate"));
		
		mainPanel.add(new JLabel("first point of Triangle 		Point :"));
		mainPanel.add(X0);
		mainPanel.add(Y0);
		
		mainPanel.add(new JLabel("first point of Triangle 		Point :"));
		mainPanel.add(X1);
		mainPanel.add(Y1);
		
		mainPanel.add(new JLabel("third point of Triangle 		Point :"));
		mainPanel.add(X2);
		mainPanel.add(Y2);
		
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
			Integer.parseInt(X0.getText());
			Integer.parseInt(X1.getText());
			Integer.parseInt(X2.getText());
			Integer.parseInt(Y0.getText());
			Integer.parseInt(Y1.getText());
			Integer.parseInt(Y2.getText());
		} catch (Exception e) {
			throw new IllegalArgumentException("Wrong Number format!");
		}
	}

	@Override
	public void acceptEditing() {
		fTriangle.setpFirst(new Point(Integer.parseInt(X0.getText()), Integer.parseInt(Y0.getText())));
		fTriangle.setpSecond(new Point(Integer.parseInt(X1.getText()), Integer.parseInt(Y1.getText())));
		fTriangle.setpThird(new Point(Integer.parseInt(X2.getText()), Integer.parseInt(Y2.getText())));
		
	
		fTriangle.setOutlineColor(colorAreaFg.getCurrentColor());
		fTriangle.setFillColor(colorAreaBg.getCurrentColor());

	}

}
