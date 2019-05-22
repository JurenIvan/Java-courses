package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface that models basic LocalisationListener.
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface ILocalizationListener {
	/**
	 * Method that signals that localization changed(language is different now)
	 */
	void localizationChanged();
}
