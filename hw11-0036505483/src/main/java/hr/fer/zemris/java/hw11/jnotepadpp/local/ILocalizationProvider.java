package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * interface that models localisationProvided. Has methods for adding and
 * removing listeners, and method getString for data retrieval.
 * 
 * @author juren
 *
 */
public interface ILocalizationProvider {
	/**
	 * Adds {@link ILocalizationListener} to internal collection
	 * 
	 * @param l {@link ILocalizationListener} that will be saved
	 */
	void addLocalizationListener(ILocalizationListener l);

	/**
	 * removes {@link ILocalizationListener} from internal collection
	 * 
	 * @param l {@link ILocalizationListener} that will be removed
	 */
	void removeLocalizationListener(ILocalizationListener l);

	/**
	 * Method what works like get in hashmap. Returns data stored under provided
	 * key.
	 * 
	 * @param key for String that we want to retrieve
	 * @return data stored under provided key
	 */
	String getString(String key);
}
