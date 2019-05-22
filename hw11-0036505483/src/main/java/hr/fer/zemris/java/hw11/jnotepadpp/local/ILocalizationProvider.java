package hr.fer.zemris.java.hw11.jnotepadpp.local;

public interface ILocalizationProvider {
	void addLocalizationListener(ILocalizationListener l);

	void removeLocalizationListener(ILocalizationListener l);

	String getString(String key);
}
