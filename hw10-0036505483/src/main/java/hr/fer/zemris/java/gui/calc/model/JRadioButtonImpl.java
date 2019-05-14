package hr.fer.zemris.java.gui.calc.model;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JRadioButton;


public class JRadioButtonImpl extends JRadioButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Set<NameListener> listeners;
	
	public JRadioButtonImpl() {
		listeners=new HashSet<>();
		addActionListener((e)->notifyObservers());
	}

	private void notifyObservers() {
		for(var o:listeners) {
			o.valueChanged();
		}
	}
	
	public void addListener(NameListener nl) {
		listeners.add(nl);
	}
	
	public void removeListener(NameListener nl) {
		listeners.remove(nl);
	}
	
}
