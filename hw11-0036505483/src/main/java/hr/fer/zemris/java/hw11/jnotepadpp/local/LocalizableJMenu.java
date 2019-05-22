package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

public class LocalizableJMenu extends JMenu{

	private static final long serialVersionUID = 1L;

	private String key;
	private ILocalizationProvider parent;
	private ILocalizationListener listener;

	public LocalizableJMenu(String key, ILocalizationProvider parent) {
		this.key = key;
		this.parent = parent;
		this.listener = new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				setExtraText();
			}
		};

		listener.localizationChanged();
		parent.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				setExtraText();
			}
		});
	}

	public void setExtraText() {
		setText(parent.getString(key));
	}
}
