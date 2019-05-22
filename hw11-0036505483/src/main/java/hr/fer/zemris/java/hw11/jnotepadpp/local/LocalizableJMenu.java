package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

public class LocalizableJMenu extends JMenu {

	private static final long serialVersionUID = 1L;

	/**
	 * Key for which {@link ILocalizationProvider} will give us data
	 */
	private String key;
	/**
	 * {@link ILocalizationProvider} reference that is used to retrieve data from
	 * dictionaries
	 */
	private ILocalizationProvider parent;
	/**
	 * Listener that gets notified whenever localizationChanged occures
	 */
	private ILocalizationListener listener;

	/**
	 * Standard constructor for {@link LocalizableJMenu} class
	 * 
	 * @param key    representing key for data that will
	 *               {@link ILocalizationProvider} provide us.
	 * @param parent {@link ILocalizationProvider} that gives us data when
	 *               requested.
	 */
	public LocalizableJMenu(String key, ILocalizationProvider parent) {
		this.key = key;
		this.parent = parent;
		this.listener = new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				setExtraText();
			}
		};

		listener.localizationChanged();
		parent.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				setExtraText();
			}
		});
	}

	/**
	 * Method that sets text into {@link JLabel}
	 * 
	 * @param extraText text that is appended to part that is constant and subject
	 *                  to change due to language changes
	 */
	public void setExtraText() {
		setText(parent.getString(key));
	}
}
