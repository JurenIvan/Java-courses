package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw08.shell.util.Splitter;

/**
 * This class represents implementation for symbol command.
 * 
 * Command that can either change the way shell looks or display information on
 * current configuration. Command symbol can take one or two arguments. If one
 * argument is given, user asks for dislaying standard PROMPT, MORELINES and
 * MULTILINE symbols. Example of use \"symbol PROMPT\". Displays current default
 * prompt symbol.
 * 
 * If two arguments are given, user can to change current configuration of
 * PROMPT, MORELINES and MULTILINE symbols .
 * 
 * Example of use \"symbol PROMPT #\" . Sets current default prompt symbol to #.
 * 
 * 
 * @author juren
 *
 */
public class SymbolShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = null;
		try {
			args = Splitter.split(arguments);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if (args.length == 1) {

			switch (args[0]) {
			case "PROMPT":
				env.writeln("Symbol for PROMPT is \'" + env.getPromptSymbol() + "\'");
				return ShellStatus.CONTINUE;
			case "MORELINES":
				env.writeln("Symbol for MORELINES is \'" + env.getMorelinesSymbol() + "\'");
				return ShellStatus.CONTINUE;
			case "MULTILINE":
				env.writeln("Symbol for MULTILINE is \'" + env.getMultilineSymbol() + "\'");
				return ShellStatus.CONTINUE;
			default:
				env.writeln("No such symbol is used in this shell");
				return ShellStatus.CONTINUE;
			}
		}
		if (args.length == 2) {

			if (args[1].length() != 1) {
				env.writeln("Symbol must be a single character.");
				return ShellStatus.CONTINUE;
			}

			switch (args[0]) {
			case "PROMPT":
				env.writeln("Symbol for PROMPT changed from \'" + env.getPromptSymbol() + "\' to \'" + args[1] + "\'");
				env.setPromptSymbol(args[1].charAt(0));
				return ShellStatus.CONTINUE;
			case "MORELINES":
				env.writeln("Symbol for MORELINES changed from \'" + env.getMorelinesSymbol() + "\' to \'" + args[1]
						+ "\'");
				env.setMorelinesSymbol(args[1].charAt(0));
				return ShellStatus.CONTINUE;
			case "MULTILINE":
				env.writeln("Symbol for MULTILINE changed from \'" + env.getMultilineSymbol() + "\' to \'" + args[1]
						+ "\'");
				env.setMultilineSymbol(args[1].charAt(0));
				return ShellStatus.CONTINUE;
			default:
				env.writeln("No such symbol is used in this shell");
				return ShellStatus.CONTINUE;
			}
		}
		env.writeln("Unsuported number of arguments");

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "Symbol";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("Command that can either change the way shell looks or display information on current configuration.");
		list.add("Command symbol can take one or two arguments");
		list.add("If one argument is given, user asks for dislaying standard PROMPT, MORELINES and MULTILINE symbols.");
		list.add("Example of use \"symbol PROMPT\"");
		list.add("Displays current default prompt symbol");
		list.add(
				"if two arguments are given, user can to change current configuration of PROMPT, MORELINES and MULTILINE symbols ");
		list.add("Example of use \"symbol PROMPT #\" ");
		list.add("Sets current default prompt symbol to #");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
