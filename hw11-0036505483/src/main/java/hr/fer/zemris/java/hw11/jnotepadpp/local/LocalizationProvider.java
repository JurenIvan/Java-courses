package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class that models provider for localisation. It is Singleton. Has setLanguage
 * methods.
 * 
 * @author juren
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	/**
	 * {@link ResourceBundle} used for word translation
	 */
	private ResourceBundle bundle;
	/**
	 * static reference to {@link LocalizationProvider} instance used to achieve
	 * Singleton pattern.
	 */
	private static LocalizationProvider localization = new LocalizationProvider();

	/**
	 * constant holding path to translations
	 */
	private final String TRANSLATIONS_PATH = "hr.fer.zemris.java.hw11.jnotepadpp.locale.prijevodi";
	/**
	 * constant holding string for default languageF
	 */
	private final String DEFAULT_LANGUAGE = "en";

	/**
	 * private constructor standard for Singleton design pattern
	 */
	private LocalizationProvider() {
		setLanguage(DEFAULT_LANGUAGE);
	}

	/**
	 * Factory method used to return instance of {@link LocalizationProvider}
	 * 
	 * @return instance of {@link LocalizationProvider}
	 */
	public static LocalizationProvider getInstance() {
		return localization;
	}

	/**
	 * method that sets language to the one given as argument and fires registered
	 * listeners.
	 * 
	 * @param language that will be set
	 */
	public void setLanguage(String language) {
		bundle = ResourceBundle.getBundle(TRANSLATIONS_PATH, Locale.forLanguageTag(language));
		fire();
	}

	/**
	 * Method that returns string stored in {@link ResourceBundle} under key
	 * provided
	 * 
	 * @return String stored in {@link ResourceBundle} under key provided
	 */
	public String getString(String key) {
		return bundle.getString(key);
	}

}
