package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

public class MyJButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String name;
	protected CalcModel model;

	public MyJButton(String name,CalcModel model) {
		this.name = name;
		this.model=model;
		super.setText(name);
		super.setHorizontalAlignment(SwingConstants.CENTER);
		super.setVerticalAlignment(SwingConstants.CENTER);
		super.setBackground(Color.CYAN);
	}
	
	public String getName() {
		return this.name;
	}

}
