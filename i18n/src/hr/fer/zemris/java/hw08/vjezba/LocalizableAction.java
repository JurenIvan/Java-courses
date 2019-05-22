package hr.fer.zemris.java.hw08.vjezba;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class LocalizableAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	
	private String key;
	private ILocalizationProvider parent;
	private ILocalizationListener listener;

	public LocalizableAction(String key, ILocalizationProvider parent) {
		this.key=key;
		this.parent=parent;
		this.listener=new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				putValue(Action.NAME, parent.getString(key));
			}
		};
		
		putValue(Action.NAME, parent.getString(key));
		
		listener.localizationChanged();
		parent.addLocalizationListener(new ILocalizationListener() {
			
			@Override
			public void localizationChanged() {
				putValue(Action.NAME, parent.getString(key));
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		putValue(Action.NAME, parent.getString(key));
	}

	
}
