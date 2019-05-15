package hr.fer.zemris.java.gui.calc.model;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class JLabelImpl extends JLabel implements CalcValueListener {

	private static final long serialVersionUID = 1L;

	public JLabelImpl() {
		setHorizontalAlignment(SwingConstants.RIGHT);
		setFont(getFont().deriveFont(30f));
	}

	@Override
	public void valueChanged(CalcModel model) {

		this.setText(model.toString());

	}

}
