package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DefaultSingleDocumentModel implements SingleDocumentModel {

	private List<SingleDocumentListener> listeners;
	private JTextArea jta;
	private boolean modified;
	private Path path;

	public DefaultSingleDocumentModel(Path path, JTextArea jta) {
		listeners=new ArrayList<>();
		this.jta = jta;
		this.modified = false;
		this.path = path;	
		
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
		Objects.requireNonNull(path,"Path must be provided.");
		this.path = path;
		notifyFilePathUpdatedListeners();
	}

	private void notifyFilePathUpdatedListeners() {
		for (var listener:listeners) {
			listener.documentFilePathUpdated(this);
		}
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

	private void notifyStatusUpdatedListeners() {
		for (var listener:listeners) {
			listener.documentModifyStatusUpdated(this);
		}
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		Objects.requireNonNull(l, "Can not register a null listener");
		List<SingleDocumentListener> newListeners = new ArrayList<>(listeners);
		newListeners.add(l);
		listeners = newListeners;
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		List<SingleDocumentListener> newListeners = new ArrayList<>(listeners);
		newListeners.remove(l);
		listeners = newListeners;
	}

}
