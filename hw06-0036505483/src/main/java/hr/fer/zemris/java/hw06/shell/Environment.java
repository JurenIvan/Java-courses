package hr.fer.zemris.java.hw06.shell;

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

}
