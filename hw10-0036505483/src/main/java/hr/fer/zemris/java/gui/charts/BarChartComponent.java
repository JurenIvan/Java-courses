package hr.fer.zemris.java.gui.charts;

import javax.swing.JComponent;

public class BarChartComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	
	private BarChart refrence;
	
	public BarChartComponent(BarChart refrence) {
	this.refrence=refrence;
	}
}
