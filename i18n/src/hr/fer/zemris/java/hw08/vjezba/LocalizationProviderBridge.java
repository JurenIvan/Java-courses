package hr.fer.zemris.java.hw08.vjezba;

public class LocalizationProviderBridge extends AbstractLocalizationProvider  {
	
	private boolean connected;
	private ILocalizationProvider parent;
	private ILocalizationListener listener;
	
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent=parent;
		listener=(super::fire);
	}
	
	public void disconect() {
		if(!connected)
			return;
		parent.removeLocalizationListener(listener);
		connected=false;
	}
	public void connect() {
		if(connected)
			return;
		parent.addLocalizationListener(listener);
		connected=true;
	}
	
	public String getString(String key) {
		return parent.getString(key);
	}

}
