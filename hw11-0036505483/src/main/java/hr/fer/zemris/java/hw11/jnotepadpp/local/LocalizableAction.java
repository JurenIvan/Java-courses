package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Abstract Class that models {@link Action} capeable of i18n.
 * 
 * @author juren
 *
 */
public abstract class LocalizableAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	/**
	 * Key used in dictionary to signify that short description is needed
	 */
	private static String DESCRIPTION_KEY = "SD";

	/**
	 * Listener that gets notified whenever localizationChanged occuresF
	 */
	private ILocalizationListener listener;

	
	/**
	 * Standard constructor for {@link LocalizableAction} class
	 * 
	 * @param key    representing key for data that will
	 *               {@link ILocalizationProvider} provide us.
	 * @param parent {@link ILocalizationProvider} that gives us data when
	 *               requested.
	 */
	public LocalizableAction(String key, ILocalizationProvider parent) {

		this.listener = new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				putValue(Action.NAME, parent.getString(key));
			}
		};

		putValue(Action.NAME, parent.getString(key));
		putValue(Action.SHORT_DESCRIPTION, parent.getString(key + DESCRIPTION_KEY));

		listener.localizationChanged();
		parent.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				putValue(Action.NAME, parent.getString(key));
				putValue(Action.SHORT_DESCRIPTION, parent.getString(key + DESCRIPTION_KEY));
			}
		});
	}

	@Override
	public abstract void actionPerformed(ActionEvent e);

}
