package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Splitter;

/**
 * This class represents implementation for help command.
 * 
 * This is command that provides info on supported commands and gives user
 * example of use. If started with no arguments, it lists names of all supported
 * commands. If started with single argument, it must print name and the
 * description of selected command. Then prints command description of provided
 * command.
 * 
 * 
 * @author juren
 *
 */
public class HelpShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = null;
		try {
			args = Splitter.split(arguments);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if (args.length == 0) {
			env.writeln(
					"This is list of all commands. If you want help with particular one try for example \"help ls\".");
			for (var commands : env.commands().keySet()) {
				env.writeln(commands);
			}
			return ShellStatus.CONTINUE;
		}

		if (args.length > 1) {
			env.writeln(
					"Unsupported form of command.You can use help with no arguments or with name of exactly one command.");
			return ShellStatus.CONTINUE;
		}

		if (env.commands().containsKey(args[0])) {
			env.writeln("This is documentation for " + args[0] + " command");
			for (var line : env.commands().get(args[0]).getCommandDescription()) {
				env.writeln(line);
			}
		} else {
			env.writeln("Unsupported command");
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("This is command that provides info on supported commands and gives user example of use");
		list.add("If started with no arguments, it lists names of all supported commands.");
		list.add("If started with single argument, it must print name and the description of selected command");
		list.add("Then prints command description of provided command");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
