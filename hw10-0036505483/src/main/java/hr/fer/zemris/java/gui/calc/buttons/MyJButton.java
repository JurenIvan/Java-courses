package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Class that represents button that holds some data used by my other
 * implementation of button
 * 
 * @author juren
 *
 */
public abstract class MyJButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * private variable used for storing name of button
	 */
	private String name;
	/**
	 * private variable used for storing implemetation of {@link CalcModel} used by
	 * listeners of buttons
	 */
	protected CalcModel model;

	/**
	 * Standard constructor that does some basic visual enhancements
	 */
	public MyJButton(String name, CalcModel model) {
		this.name = name;
		this.model = model;
		super.setText(name);
		super.setHorizontalAlignment(SwingConstants.CENTER);
		super.setVerticalAlignment(SwingConstants.CENTER);
		super.setBackground(Color.CYAN);
	}

	/**
	 * Standard getter for stored name.
	 * @return stored name
	 */
	public String getName() {
		return this.name;
	}

}
