package hr.fer.zemris.java.finalExam;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainGui extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Constant for minimum size of Window .
	 */
	private static final int MINIMUM_WINDOW_SIZE = 500;
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MainGui().setVisible(true));
	}
	
	public MainGui() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		initGUI();
		setMinimumSize(new Dimension(MINIMUM_WINDOW_SIZE, MINIMUM_WINDOW_SIZE));
		pack();
		setLocationRelativeTo(null);
	}

	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
	}

}
