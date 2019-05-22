package hr.fer.zemris.java.hw08.vjezba;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {

//	private String language;
	private ResourceBundle bundle;
	private static LocalizationProvider localization = new LocalizationProvider();
	
	private final String TRANSLATIONS_PATH = "hr.fer.zemris.java.hw08.vjezba.prijevodi";
	private final String DEFAULT_LANGUAGE = "de";

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
