package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Interface modeling every command. Has 3 methods:
 * {@link ShellCommand#executeCommand}, {@link ShellCommand#getCommandName},
 * {@link ShellCommand#getCommandDescription}
 * 
 * @author juren
 *
 */
public interface ShellCommand {
	/**
	 * Models a method whose implementations(strategies) are core of functionality
	 * of shell.
	 * 
	 * @param env       input/output stream for every method so that it can output
	 *                  data or ask for more.
	 * @param arguments Single string which represents everything that user entered
	 *                  AFTER the command name. It is expected that in case of
	 *                  multiline input, the shell has already concatenated all
	 *                  lines into a single line and removed MORELINES symbol from
	 *                  line endings (before concatenation).
	 * @return {@link ShellStatus} representing whether program should continue or
	 *         should it be terminated
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Standard getter for commands name
	 * 
	 * @return name of command (string)
	 */
	String getCommandName();

	/**
	 * Getter for unmodifiable list containing text describing functionalities and
	 * ways how this command can be used
	 * 
	 * @return list containing text describing functionalities and ways how this
	 *         command can be used
	 */
	List<String> getCommandDescription();
}
