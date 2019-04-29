package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw08.shell.util.Splitter;

/**
 * This class represents implementation for charsets command.
 * 
 * This command lists names of supported charsets for Java platform. Command
 * charsets takes no arguments. A single charset name is written per line.
 * 
 * @author juren
 *
 */
public class CharsetsShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args=null;
		try {
			args = Splitter.split(arguments);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if (args.length != 0) {
			env.writeln("Charsets command expects no arguments.");
			return ShellStatus.CONTINUE;
		}

		env.writeln("List of supported charsets:");
		for (var line : Charset.availableCharsets().keySet()) {
			env.writeln(line);
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "charsets";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("This command lists names of supported charsets for Java platform");
		list.add("Command charsets takes no arguments");
		list.add("A single charset name is written per line.");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
