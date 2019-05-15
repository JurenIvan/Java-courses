package hr.fer.zemris.java.gui.calc.model;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;


public class JCheckBoxImpl extends JCheckBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Set<NameListener> listeners;
	
	public JCheckBoxImpl() {
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
