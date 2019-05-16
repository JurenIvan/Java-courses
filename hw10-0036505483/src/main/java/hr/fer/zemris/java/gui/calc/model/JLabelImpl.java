package hr.fer.zemris.java.gui.calc.model;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Implementation of JCheckBox that is listener.
 * 
 * @author juren
 *
 */
public class JLabelImpl extends JLabel implements CalcValueListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Standard constructor that sets some visual apperance of gui
	 */
	public JLabelImpl() {
		setHorizontalAlignment(SwingConstants.RIGHT);
		setFont(getFont().deriveFont(30f));
	}

	@Override
	public void valueChanged(CalcModel model) {

		this.setText(model.toString());

	}

}
