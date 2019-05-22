package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JLabel;

/**
 * Class that models {@link JLable} capable of i18n.
 * 
 * @author juren
 *
 */
public class LocalizableJLable extends JLabel {

	private static final long serialVersionUID = 1L;

	/**
	 * Key for which {@link ILocalizationProvider} will give us data
	 */
	private String key;
	/**
	 * String that is added to JLable but is not affected by language change
	 */
	private String extraText;
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
	 * Standard constructor for {@link LocalizableJLable} class
	 * 
	 * @param key    representing key for data that will
	 *               {@link ILocalizationProvider} provide us.
	 * @param parent {@link ILocalizationProvider} that gives us data when
	 *               requested.
	 */
	public LocalizableJLable(String key, ILocalizationProvider parent) {
		this.key = key;
		this.parent = parent;
		this.extraText = "";
		this.listener = new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				setText(extraText);
			}
		};

		listener.localizationChanged();
		parent.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				setExtraText(extraText);
			}
		});
	}

	/**
	 * Method that sets text into {@link JLabel}
	 * 
	 * @param extraText text that is appended to part that is constant and subject
	 *                  to change due to language changes
	 */
	public void setExtraText(String extraText) {
		this.extraText = extraText;
		setText(parent.getString(key) + extraText);
	}
}
