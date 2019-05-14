package hr.fer.zemris.java.gui.calc.model;

import javax.swing.JLabel;

public class JLabelImpl extends JLabel implements CalcValueListener{

	private static final long serialVersionUID = 1L;

	@Override
	public void valueChanged(CalcModel model) {
		this.setText(model.toString());
	}

}
