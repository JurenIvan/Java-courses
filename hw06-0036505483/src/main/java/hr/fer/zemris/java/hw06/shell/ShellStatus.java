package hr.fer.zemris.java.hw06.shell;
/**
 * Enum representing possible return values of command execution.
 * 
 * @author juren
 *
 */
public enum ShellStatus {
	/**
	 * Means that the shell operation should be continued.
	 */
	CONTINUE, 
	/**
	 * Means that the shell operation should be terminated.
	 */
	TERMINATE
}
