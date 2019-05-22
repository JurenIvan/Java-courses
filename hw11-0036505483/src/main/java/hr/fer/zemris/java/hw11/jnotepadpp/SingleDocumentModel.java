package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Interface that models a single loaded document used by {@link JNotepadPP}
 * 
 * @author juren
 *
 */
public interface SingleDocumentModel {
	/**
	 * Method that returns {@link java.awt.TextComponent} of {@link JTextArea}
	 * 
	 * @return reference to {@link java.awt.TextComponent} of {@link JTextArea}
	 */
	JTextArea getTextComponent();

	/**
	 * Method that returns path of given {@link SingleDocumentModel}
	 * 
	 * @return path of given {@link SingleDocumentModel} or null if no such is yet
	 *         saved
	 */
	Path getFilePath();

	/**
	 * Method that sets path of {@link SingleDocumentModel}
	 * 
	 * @param path that will be saved
	 */
	void setFilePath(Path path);

	/**
	 * Getter for boolean flag that is set when any modification happens
	 * 
	 * @return boolean flag that is set when any modification happens
	 */
	boolean isModified();

	/**
	 * setter for boolean flag that is set when any modification happens
	 * 
	 * @param modified flag that should set when any modification happens
	 */
	void setModified(boolean modified);

	/**
	 * Method that adds listener into collection of listeners for this
	 * {@link SingleDocumentModel}
	 * 
	 * @param l listener
	 */
	void addSingleDocumentListener(SingleDocumentListener l);

	/**
	 * Method that removes provided listener from collection of listeners for this
	 * {@link SingleDocumentModel}
	 * 
	 * @param l listener
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
