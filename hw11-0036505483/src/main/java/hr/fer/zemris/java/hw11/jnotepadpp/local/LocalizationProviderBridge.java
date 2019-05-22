package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Class that is part of i18n structure that enables Garbage Collector to do
 * it's job of cleaning memory because of connect/disconnect methods.
 * 
 * @author juren
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * flag representing whether the communication is set or not;
	 */
	private boolean connected;

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
	 * Constuctor that sets {@link ILocalizationProvider} and fires all registered
	 * {@link ILocalizationListener}
	 * 
	 * @param parent {@link ILocalizationProvider} that gives us data when
	 *               requested.
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
		listener = (super::fire);
	}

	/**
	 * Method used to disconnect from {@link ILocalizationListener}
	 */
	public void disconect() {
		if (!connected)
			return;
		parent.removeLocalizationListener(listener);
		connected = false;
	}

	/**
	 * Method used to connect from {@link ILocalizationListener}
	 */
	public void connect() {
		if (connected)
			return;
		parent.addLocalizationListener(listener);
		connected = true;
	}

	/**
	 * Method that returns string stored in {@link ResourceBundle} under key
	 * provided
	 * 
	 * @return String stored in {@link ResourceBundle} under key provided
	 */
	public String getString(String key) {
		return parent.getString(key);
	}

}
