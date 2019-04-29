package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw08.shell.util.Splitter;

/**
 * This class represents implementation for mkdir command.
 * 
 * This command can create directory structure. The mkdir command takes a single
 * argument: directory name. Creates the directory named by this abstract
 * pathname, including any necessary but nonexistent parent directories.
 * 
 * 
 * @author juren
 *
 */
public class MkdirShellCommand implements ShellCommand {

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
		Path directory;
		try {
			directory = env.getCurrentDirectory().resolve(Paths.get(args[0]));
		} catch (InvalidPathException e1) {
			env.writeln("Path is invalid.");
			return ShellStatus.CONTINUE;
		}
		if (Files.exists(directory)) {
			env.writeln("Already exists.");
			return ShellStatus.CONTINUE;
		}
		try {
			if (Files.createDirectories(directory) != null) {
				env.writeln("Structure succesfully created");
			} else {
				env.writeln("Structure was NOT created succesfully");
			}
		} catch (IOException e) {
			env.writeln("IO error occured.");
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "mkdir";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("This command can create directory structure.");
		list.add("The mkdir command takes a single argument: directory name");
		list.add(
				"Creates the directory named by this abstract pathname, including any necessary but nonexistent parent directories.");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
