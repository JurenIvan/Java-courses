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
 * 
 * This class represents implementation for dropd command.
 * 
 * Command used to drop thing stored on stack. If no paths are stored or invalid
 * path is at the top of stack, command returns appropriate message.
 * 
 * @author juren
 *
 */
public class DropdShellCommand implements ShellCommand {
	
	/**
	 * Constant that holds key of map where paths data is stored
	 */
	private static final String COMMAND_KEY = "cdstack";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments != null) {
			env.writeln("dropd executes only when no arguments are provided!");
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
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "dropd";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("Command used to drop thing stored on stack.");
		list.add("If no paths are stored or invalid path is at the top of stack, command returns appropriate message.");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
