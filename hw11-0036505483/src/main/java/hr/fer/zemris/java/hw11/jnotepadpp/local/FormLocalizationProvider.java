package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Class that extends {@link LocalizationProviderBridge} and implements a
 * {@link java.awt.event.WindowListener} to provided {@link JFrame}
 * 
 * @author juren
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Standard constructor that delegates job to superclass and registers
	 * {@link java.awt.event.WindowListener} to given frame
	 * 
	 * @param parent atribute that can get us data neded
	 * @param frame  provided {@link JFrame}
	 */
	public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
		super(parent);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				disconect();
			}
		});

	}

}
