package hr.fer.zemris.java.hw08.vjezba;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class FormLocalizationProvider extends LocalizationProviderBridge {

	public FormLocalizationProvider(ILocalizationProvider parent,JFrame frame) {
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
