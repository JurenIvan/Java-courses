package hr.fer.zemris.java.hw08.vjezba;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	
	private Set<ILocalizationListener> listeners;
	
	public AbstractLocalizationProvider() {
		listeners=new HashSet<>();
	}

	public void addLocalizationListener(ILocalizationListener l) {
		HashSet<ILocalizationListener> newListeners=new HashSet<ILocalizationListener>(listeners);
		newListeners.add(l);
		listeners=newListeners;
	}

	public void removeLocalizationListener(ILocalizationListener l) {
		HashSet<ILocalizationListener> newListeners=new HashSet<ILocalizationListener>(listeners);
		newListeners.remove(l);
		listeners=newListeners;
	}

	public void fire() {
		for(var l:listeners) {
			l.localizationChanged();
		}
	}
}
