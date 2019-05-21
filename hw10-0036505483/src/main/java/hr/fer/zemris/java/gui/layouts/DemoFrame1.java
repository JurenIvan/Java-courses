package hr.fer.zemris.java.gui.layouts;

import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Demo class containing main method used to demonstrate functiolalities of Calc
 * layout
 * 
 * @author juren
 *
 */
public class DemoFrame1 extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Standard constructor for DemoFrame1
	 */
	public DemoFrame1() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initGUI();
		pack();
	}

	/**
	 * Method used to clear up the constructor.Contains commands to configure GUI.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		((JPanel)cp).setBorder(BorderFactory.createLineBorder(Color.RED, 20));
		cp.setLayout(new CalcLayout(3));
		cp.add(l("tekst 1"), new RCPosition(1, 1));
		cp.add(l("tekst 2"), new RCPosition(2, 3));
		cp.add(l("tekst stvarno najdulji"), new RCPosition(2, 7));
		cp.add(l("tekst kraći"), new RCPosition(4, 2));
		cp.add(l("tekst srednji"), new RCPosition(4, 5));
		cp.add(l("tekst"), new RCPosition(4, 7));
	}

	/**
	 * Method that returns custom JLabel with text and background
	 * 
	 * @param text shown in label
	 * @return created new label
	 */
	private JLabel l(String text) {
		JLabel l = new JLabel(text);
		l.setBackground(Color.YELLOW);
		l.setOpaque(true);
		return l;
	}

	/**
	 * Main method used to start the program and demonstrate it's capeablities
	 * 
	 * @param args not used
	 */

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new DemoFrame1().setVisible(true);
		});
	}
}
