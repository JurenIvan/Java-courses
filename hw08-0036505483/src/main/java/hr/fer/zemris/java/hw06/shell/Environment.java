package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * 
 * This is an abstraction which is passed to each defined command. The each
 * implemented command communicates with user (reads user input and writes
 * response) only through this interface.
 * 
 * @author juren
 *
 */
public interface Environment {

	/**
	 * Method used for reading a line from input. Does all work about managing
	 * default prompt symbols.
	 * 
	 * @return Line that user entered. If multiple lines input was used, all
	 *         secondary chars are ignored.
	 * @throws ShellIOException if communication with shell breaks.
	 */
	String readLine() throws ShellIOException;

	/**
	 * Method used to display text on shell.
	 * 
	 * @param text to be displayed
	 * @throws ShellIOException if communication with shell breaks.
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Method used to display new line of text on shell.
	 * 
	 * @param text to be displayed
	 * @throws ShellIOException if communication with shell breaks.
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * Standard getter for map storing pairs of names of commands and its
	 * implementation
	 * 
	 * @return map of stored commands
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * Getter for multiline symbol.
	 * 
	 * @return multiline symbol used in shell
	 */
	Character getMultilineSymbol();

	/**
	 * Setter for multiline symbol
	 * 
	 * @param symbol to be used in shell.
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Getter for prompt symbol
	 * 
	 * @return prompt symbol used in shell
	 */
	Character getPromptSymbol();

	/**
	 * Setter for prompt symbol
	 * 
	 * @param symbol to be used in shell.
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Getter for MoreLines symbol
	 * 
	 * @return MoreLines symbol used in shell
	 */
	Character getMorelinesSymbol();

	/**
	 * Setter for moreLines symbol
	 * 
	 * @param symbol to be used in shell.
	 */
	void setMorelinesSymbol(Character symbol);

	/**
	 * Method that returns absolute normalized path to current directory where user
	 * process is running
	 * 
	 * @return absolute normalized path to current directory
	 */
	Path getCurrentDirectory();

	/**
	 * Method that sets absolute normalized path to current directory where user
	 * process is running
	 * 
	 * @param path path that is set as a current directory
	 * 
	 */
	void setCurrentDirectory(Path path);

	/**
	 * Returns data stored in map used for shared work
	 * 
	 * @param key String used to retrieve data from map
	 * @return data stored by key or null if no such data exist
	 */
	Object getSharedData(String key);

	/**
	 * Method that stores data into map so that it can be used later
	 * 
	 * @param key   String used to store data into map
	 * @param value Object/data that is stored into map
	 * @throws NullPointerException if data stored is null reference
	 */
	void setSharedData(String key, Object value);

}
