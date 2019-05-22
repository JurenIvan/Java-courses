package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.Component;
import java.awt.HeadlessException;

import javax.swing.Icon;
import javax.swing.JOptionPane;

public class LocalizableJOptionPane {

	public static void showMessageDialog(Component parentComponent, String message, String title, int messageType,
			ILocalizationProvider flp) throws HeadlessException {
		 JOptionPane.showMessageDialog(parentComponent, flp.getString(message), flp.getString(title),
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static int showOptionDialog(Component parentComponent,
	        String message, String title, int optionType, int messageType,
	        Icon icon, String[] options, Object initialValue,ILocalizationProvider parent) {
		
		return showOptionDialog(parentComponent,message,title,optionType,messageType,icon,options,initialValue,parent,"");
	}
	
	public static int showOptionDialog(Component parentComponent,
	        String message, String title, int optionType, int messageType,
	        Icon icon, String[] options, Object initialValue,ILocalizationProvider parent,String extraText) {
		
		Object[] newOptions = new String[options.length];
		for (int i = 0; i < options.length; i++) {
			newOptions[i] = parent.getString(options[i]);
		}
		
		return JOptionPane.showOptionDialog(parentComponent, parent.getString(message)+extraText, parent.getString(title), optionType,
				messageType, icon, newOptions, initialValue);
		
	}

	

	
	
}
