package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Color;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Class that models {@link DefaultMultipleDocumentModel} which is responsible
 * for storing single document and its {@link JTextArea} along with {@link Path} where it is stored.
 * 
 * @author juren
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {

	/**
	 * Collection of {@link SingleDocumentListener}s that will fire when some of
	 * notify methods are called
	 */
	private Set<SingleDocumentListener> listeners;
	/**
	 * Variable that stores reference to {@link JTextArea} where text of file is
	 * represented
	 */
	private JTextArea jta;
	/**
	 * boolean flag representing has {@link SingleDocumentModel} changed since
	 * loading or last save
	 */
	private boolean modified;
	/**
	 * variable that stores path where {@link SingleDocumentModel} is stored
	 */
	private Path path;

	/**
	 * Standard constructor that sets Listener for appropriate actions for each
	 * {@link SingleDocumentModel} created.
	 * 
	 * @param path for current {@link SingleDocumentModel}
	 * @param jta  text area of current {@link SingleDocumentModel}
	 */
	public DefaultSingleDocumentModel(Path path, JTextArea jta) {
		listeners = new HashSet<>();
		this.jta = jta;
		this.modified = false;
		this.path = path;
		jta.setForeground(Color.GREEN);
		jta.setBackground(Color.BLACK);

		jta.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(true);
				notifyStatusUpdatedListeners();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
				notifyStatusUpdatedListeners();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setModified(true);
				notifyStatusUpdatedListeners();
			}
		});
	}

	@Override
	public JTextArea getTextComponent() {
		return jta;
	}

	@Override
	public Path getFilePath() {
		return path;
	}

	@Override
	public void setFilePath(Path path) {
		Objects.requireNonNull(path, "Path must be provided.");
		
		this.path = path;
		notifyFilePathUpdatedListeners();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		if (this.modified != modified) {
			this.modified = modified;
			notifyStatusUpdatedListeners();
		}
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		Objects.requireNonNull(l, "Can not register a null listener");
		Set<SingleDocumentListener> newListeners = new HashSet<>(listeners);
		newListeners.add(l);
		listeners = newListeners;
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		Set<SingleDocumentListener> newListeners = new HashSet<>(listeners);
		newListeners.remove(l);
		listeners = newListeners;
	}

	// ------------NOTIFY METHODS--------------

	/**
	 * Method that notifies all registered listeners when documents status is
	 * changed
	 */
	private void notifyStatusUpdatedListeners() {
		for (var listener : listeners) {
			listener.documentModifyStatusUpdated(this);
		}
	}

	/**
	 * Method that notifies all registered listeners when documents path is changed
	 */
	private void notifyFilePathUpdatedListeners() {
		for (var listener : listeners) {
			listener.documentFilePathUpdated(this);
		}
	}

}
