package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public abstract class LocalizableAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	

	private ILocalizationListener listener;

	public LocalizableAction(String key, ILocalizationProvider parent) {
	
		this.listener=new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				putValue(Action.NAME, parent.getString(key));
			}
		};
		
		putValue(Action.NAME, parent.getString(key));
		putValue(Action.SHORT_DESCRIPTION, parent.getString(key+"SD"));
		
		listener.localizationChanged();
		parent.addLocalizationListener(new ILocalizationListener() {
			
			@Override
			public void localizationChanged() {
				putValue(Action.NAME, parent.getString(key));
				putValue(Action.SHORT_DESCRIPTION, parent.getString(key+"SD"));
			}
		});
	}

	@Override
	public abstract void actionPerformed(ActionEvent e);

	
}
