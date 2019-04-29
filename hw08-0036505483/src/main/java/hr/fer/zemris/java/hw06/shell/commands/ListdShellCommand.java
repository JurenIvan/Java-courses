package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * This class represents implementation for popd command.
 * 
 * Command used to restore current all remembered directories. If no or invalid
 * paths are stored, command returns appropriate message. Prints paths from top
 * to bottom of stack used for storing data.
 * 
 * 
 * @author juren
 *
 */
public class ListdShellCommand implements ShellCommand {
	
	/**
	 * Constant holding message that is outputed when the stack is empty
	 */
	private static final String OUTPUT_MESSAGE = "Nema pohranjenih direktorija.";
	/**
	 * Constant that holds key of map where paths data is stored
	 */
	private static final String COMMAND_KEY = "cdstack";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments != null) {
			env.writeln("listd executes only when no arguments are provided!");
			return ShellStatus.CONTINUE;
		}

		@SuppressWarnings("unchecked")
		Stack<Path> dataStack = (Stack<Path>) env.getSharedData(COMMAND_KEY);
		if (dataStack == null) {
			env.writeln(OUTPUT_MESSAGE);
			return ShellStatus.CONTINUE;
		}

		Object[] arrayOfStoredPaths = dataStack.toArray();
		for (int i = arrayOfStoredPaths.length-1; i >= 0; i--) {
			env.writeln(((Path) arrayOfStoredPaths[i]).toString());
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "listd";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("Command used to restore current all remembered directories.");
		list.add("If no or invalid paths are stored, command returns appropriate message.");
		list.add("Prints paths from top to bottom of stack used for storing data.");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
