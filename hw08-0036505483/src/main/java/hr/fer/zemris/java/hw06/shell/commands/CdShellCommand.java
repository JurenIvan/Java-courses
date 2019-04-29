package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw08.shell.util.Splitter;

/**
 * This class represents implementation for cd command. This command can change
 * current directory The cd command takes a single argument: directory name
 * 
 * @author juren
 *
 */
public class CdShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = null;
		try {
			args = Splitter.split(arguments);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if (args.length != 1) {
			env.writeln("Should have been exactly one argument.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			
			env.setCurrentDirectory(env.getCurrentDirectory().resolve(Paths.get(args[0])));
		} catch (InvalidPathException e1) {
			env.writeln("Path is invalid.");
			return ShellStatus.CONTINUE;
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "cd";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("This command can change current directory.");
		list.add("The cd command takes a single argument: directory name");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
