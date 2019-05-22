package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JLabel;

public class LocalizableJLable extends JLabel {

	private static final long serialVersionUID = 1L;

	private String key;
	private String extraText;
	private ILocalizationProvider parent;
	private ILocalizationListener listener;

	public LocalizableJLable(String key, ILocalizationProvider parent) {
		this.key = key;
		this.parent = parent;
		this.extraText="";
		this.listener = new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				setText(extraText);
			}
		};

		listener.localizationChanged();
		parent.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				setExtraText(extraText);
			}
		});
	}

	public void setExtraText(String extraText) {
		this.extraText = extraText;
		setText(parent.getString(key) + extraText);
	}
}
