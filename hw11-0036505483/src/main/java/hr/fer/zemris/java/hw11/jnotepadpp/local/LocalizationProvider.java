package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {

//	private String language;
	private ResourceBundle bundle;
	private static LocalizationProvider localization = new LocalizationProvider();
	
	private final String TRANSLATIONS_PATH = "hr.fer.zemris.java.hw11.jnotepadpp.locale.prijevodi";
	private final String DEFAULT_LANGUAGE = "en";

	private LocalizationProvider() {
		setLanguage(DEFAULT_LANGUAGE);
	}

	public static LocalizationProvider getInstance() {
		return localization;
	}

	public void setLanguage(String language) {
//		this.language = language;
		bundle = ResourceBundle.getBundle(TRANSLATIONS_PATH, Locale.forLanguageTag(language));
		fire();
	}

	public String getString(String key) {
		return bundle.getString(key);
	}

}
