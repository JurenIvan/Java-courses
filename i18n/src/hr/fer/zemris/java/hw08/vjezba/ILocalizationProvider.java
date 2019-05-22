package hr.fer.zemris.java.hw08.vjezba;

public interface ILocalizationProvider {
	void addLocalizationListener(ILocalizationListener l);

	void removeLocalizationListener(ILocalizationListener l);

	String getString(String key);
}
