package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
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
public class PopdShellCommand implements ShellCommand {
	
	/**
	 * Constant that holds key of map where paths data is stored
	 */
	private static final String COMMAND_KEY = "cdstack";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments != null) {
			env.writeln("popd executes only when no arguments are provided!");
			return ShellStatus.CONTINUE;
		}

		@SuppressWarnings("unchecked")
		Stack<Path> dataStack = (Stack<Path>) env.getSharedData(COMMAND_KEY);
		if (dataStack == null) {
			env.writeln("No paths has ever been stored. Use pushd first, or help popd to see how it is used");
			return ShellStatus.CONTINUE;
		}

		if (dataStack.isEmpty()) {
			env.writeln("No paths stored.");
			return ShellStatus.CONTINUE;
		}

		Path restoredPath = dataStack.pop();
		if (restoredPath == null) {
			env.writeln("Path that was stored is invalid. No actions performed.");
			return ShellStatus.CONTINUE;
		}
		if (Files.exists(restoredPath)) {
			env.setCurrentDirectory(restoredPath);
			return ShellStatus.CONTINUE;
		}
		env.writeln("Path that was stored on top of stack is deleted. No actions performed.");
		return ShellStatus.CONTINUE;

	}

	@Override
	public String getCommandName() {
		return "popd";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("Command used to restore current directory from stack.");
		list.add("If no paths are stored or invalid path is at the top of stack, command returns appropriate message.");
		list.add("See help pushd for more info.");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
