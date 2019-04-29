package hr.fer.zemris.java.hw06.shell.commands;

import java.io.InputStream;
import java.nio.charset.Charset;
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
 * This class represents implementation for cat command.
 * 
 * This command opens given file and writes its content to console. "Command cat
 * takes one or two arguments. "The first argument is path to some file and is
 * mandatory. "The second argument is charset name that should be used to
 * interpret chars from bytes. If not provided, a default platform charset is
 * used");
 * 
 * 
 * @author juren
 *
 */
public class CatShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args=null;
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

		Path pathOfFileToBeDisplayed;
		try {
			pathOfFileToBeDisplayed = env.getCurrentDirectory().resolve(Paths.get(args[0]));
		} catch (InvalidPathException e) {
			env.writeln("Path is invalid.");
			return ShellStatus.CONTINUE;
		}
		if (!Files.isRegularFile(pathOfFileToBeDisplayed)) {
			env.writeln("Provided argument should have been path to file.");
			return ShellStatus.CONTINUE;
		}

		Charset cs = Charset.defaultCharset();
		if (args.length == 2 && Charset.isSupported(args[1])) {
			cs = Charset.forName(args[1]);
		} else if (args.length == 2 && !Charset.isSupported(args[1])) {
			env.writeln(
					"Unsupported charset. Try command \'charsets\' to see list of supported charsets. Using system default to display file. ");
		}
		displayData(env, pathOfFileToBeDisplayed, cs);
		return ShellStatus.CONTINUE;
	}

	/**
	 * Method used to display data for path to standard output(shell) (provided by
	 * Environment)
	 * 
	 * @param env                     used as OutputStream through it's functions
	 * @param pathOfFileToBeDisplayed path to file to be displayed
	 * @param cs                      Charset that is used
	 */
	private void displayData(Environment env, Path pathOfFileToBeDisplayed, Charset cs) {
		try (InputStream is = Files.newInputStream(pathOfFileToBeDisplayed)) {
			env.writeln("Content of " + pathOfFileToBeDisplayed.getFileName() + " diplayed with " + cs.displayName()
					+ " charset is:");
			byte[] buff = new byte[1024 * 4];
			int r;
			while ((r = is.read(buff)) > 0) {
				String s = new String(buff, 0, r, cs);
				env.write(s);
			}
		} catch (Exception e) {
			env.writeln("Error while reading Occured.");
		}
		env.writeln("");
	}

	@Override
	public String getCommandName() {
		return "cat";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("This command opens given file and writes its content to console.");
		list.add("Command cat takes one or two arguments.");
		list.add("The first argument is path to some file and is mandatory.");
		list.add(
				"The second argument is charset name that should be used to interpret chars from bytes. If not provided, a default platform charset is used");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
