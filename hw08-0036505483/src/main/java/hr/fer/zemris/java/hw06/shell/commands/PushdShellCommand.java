package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw08.shell.util.Splitter;

/**
 * This class represents implementation for pushd command.
 * 
 * Command used to push current directory to stack and at the same time it sets
 * provided directory(only argument) as a current directory but only if it is
 * valid.
 * 
 * @author juren
 *
 */
public class PushdShellCommand implements ShellCommand {

	/**
	 * Constant that holds key of map where paths data is stored
	 */
	private static final String COMMAND_KEY = "cdstack";
	

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = null;
		try {
			args = Splitter.split(arguments);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if (args.length < 1) {
			env.writeln("Should have been atleast one argument.");
			return ShellStatus.CONTINUE;
		}
		if (args.length > 2) {
			env.writeln("Unsupported form of command.Try \"help cat\"");
			return ShellStatus.CONTINUE;
		}
		
		Path pathOfFile;
		try {
			pathOfFile = env.getCurrentDirectory().resolve(Paths.get(args[0]));
		} catch (InvalidPathException e) {
			env.writeln("Path provided is invalid.");
			return ShellStatus.CONTINUE;
		}
		if (!Files.exists(pathOfFile)) {
			env.writeln("Entered path should already exits.");
			return ShellStatus.CONTINUE;
		}
		
		@SuppressWarnings("unchecked")
		Stack<Path> dataStack=(Stack<Path>) env.getSharedData(COMMAND_KEY);
		if(dataStack==null) {
			dataStack=new Stack<Path>();
			dataStack.push(env.getCurrentDirectory());
			env.setSharedData(COMMAND_KEY, dataStack);
		}else {
			dataStack.push(env.getCurrentDirectory());
		}
		env.setCurrentDirectory(pathOfFile);
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "pushd";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("Command used to push current directory to stack.");
		list.add("At the same time it sets provided directory (only argument) as a current directory");
		list.add("But only if it is valid.");
		list.add("See help popd for more info.");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
