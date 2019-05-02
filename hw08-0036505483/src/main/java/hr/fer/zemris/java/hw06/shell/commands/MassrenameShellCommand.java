package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.FilterResult;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw08.shell.util.NameBuilder;
import hr.fer.zemris.java.hw08.shell.util.NameBuilderParser;
import hr.fer.zemris.java.hw08.shell.util.Splitter;

/**
 * This class represents implementation for exit command.
 * 
 * This command enables four things with its for subcommands. First; filter ->
 * it prints only files that fit provided regex for every file in folder
 * provided Second; groups -> it prints groups that were matched with provided
 * regex for every file in folder provided Third; show -> enables preview of
 * file names if we decide to do move of all of those that meet provided
 * criteria Forth; execute -> moves all files from first path to another and
 * renames them accordingly.
 * 
 * Syntax for this method is massrename path1 path2 subCommandName inputRegex or
 * massrename path1 path2 subCommandName inputRegex outputRegex if execute
 * subcomand is used.
 * 
 * 
 * @author juren
 *
 */
public class MassrenameShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = null;
		try {
			args = Splitter.split(arguments);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}

		if (args.length != 4 && args.length != 5) {
			env.writeln("Command uses 4 or 5 arguments. Check help massrename for more info.");
			return ShellStatus.CONTINUE;
		}

		Path fromDir = null;
		Path toDir = null;
		try {
			fromDir = env.getCurrentDirectory().resolve(Paths.get(args[0]));
			toDir = env.getCurrentDirectory().resolve(Paths.get(args[1]));
			if (!Files.isDirectory(fromDir)) {
				env.writeln("First path provided is invalid.");
				return ShellStatus.CONTINUE;
			}
			if (!Files.isDirectory(toDir)) {
				env.writeln("Second path provided is invalid.");
				return ShellStatus.CONTINUE;
			}
		} catch (InvalidPathException e) {
			env.writeln("Invalid path provided.");
			return ShellStatus.CONTINUE;
		}
		try {
			return decideWhichSubCommand(env, args, fromDir, toDir);
		} catch (IOException e) {
			env.writeln("IO Error occured.Check provided Paths.");
			return ShellStatus.CONTINUE;
		}

	}

	/**
	 * Method used to switch to proper subcommand.
	 * 
	 * @param env     Environment used for output
	 * @param args    parsed arguments from input used in subcommands
	 * @param fromDir resolved first path
	 * @param toDir   resolved second path
	 * @return ShellStatus used to signal Shell whether it should continue woking or
	 *         not
	 * @throws IOException
	 */
	private ShellStatus decideWhichSubCommand(Environment env, String[] args, Path fromDir, Path toDir)
			throws IOException {
		switch (args[2]) {
		case "filter":
			if (args.length != 4) {
				env.writeln("filter sub-command uses 4 arguments.");
				return ShellStatus.CONTINUE;
			}
			filterTask(fromDir, toDir, args[3], env);
			return ShellStatus.CONTINUE;
		case "groups":
			if (args.length != 4) {
				env.writeln("filter sub-command uses 4 arguments.");
				return ShellStatus.CONTINUE;
			}
			groupsTask(fromDir, toDir, args[3], env);
			return ShellStatus.CONTINUE;
		case "show":
			if (args.length != 5) {
				env.writeln("show sub-command uses 5 arguments.");
				return ShellStatus.CONTINUE;
			}
			showTask(fromDir, toDir, args[3], args[4], env);
			return ShellStatus.CONTINUE;
		case "execute":
			if (args.length != 5) {
				env.writeln("execute sub-command uses 5 arguments.");
				return ShellStatus.CONTINUE;
			}
			executeTask(fromDir, toDir, args[3], args[4], env);
			return ShellStatus.CONTINUE;
		default:
			env.writeln("subcommand not recognised");
			return ShellStatus.CONTINUE;

		}

	}

	/**
	 * Method that does job of execute subcomand. This subcommand moves all files
	 * from first path to another and renames them accordingly.
	 * 
	 * @param fromFolder  resolved first path
	 * @param toFolder    resolved second path
	 * @param inputRegex  Regex used for determining whether files in fromFolder are
	 *                    acceptable for operation
	 * @param outputRegex Regex used to form path of output files
	 * @param env         Environment used for output of data
	 * @throws IOException If path can not be obtained
	 */
	private void executeTask(Path fromFolder, Path toFolder, String inputRegex, String outputRegex, Environment env)
			throws IOException {
		List<FilterResult> result = filter(fromFolder, inputRegex);

		NameBuilderParser nbp;
		try {
			nbp = new NameBuilderParser(outputRegex);
		} catch (NumberFormatException e) {
			env.writeln("Irregular expression");
			return;
		}

		NameBuilder nb = nbp.getNameBuilder();
		for (var line : result) {
			StringBuilder firstFile = new StringBuilder();
			firstFile.append(fromFolder.toString());
			firstFile.append("\\");
			firstFile.append(line.toString());
			StringBuilder secondFile = new StringBuilder();
			secondFile.append(toFolder.toString());
			secondFile.append("\\");
			StringBuilder sb = new StringBuilder();
			try {
				nb.execute(line, sb);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("No such group found.");
				return;
			}
			secondFile.append(sb.toString());

			Files.move(Paths.get(firstFile.toString()), Paths.get(secondFile.toString()),
					StandardCopyOption.REPLACE_EXISTING);
		}
	}

	/**
	 * Method that enables preview of file names if we decide to do move of all of
	 * those that meet provided criteria
	 * 
	 * @param fromFolder  resolved first path
	 * @param toFolder    resolved second path
	 * @param inputRegex  Regex used for determining whether files in fromFolder are
	 *                    acceptable for operation
	 * @param outputRegex Regex used to form path of output files
	 * @param env         Environment used for output of data
	 * @throws IOException If path can not be obtained
	 */
	private void showTask(Path fromFolder, Path toFolder, String inputRegex, String outputRegex, Environment env)
			throws IOException {
		List<FilterResult> result = filter(fromFolder, inputRegex);
		NameBuilderParser nbp;
		try {
			nbp = new NameBuilderParser(outputRegex);
		} catch (NumberFormatException e) {
			env.writeln("Irregular expression");
			return;
		}
		NameBuilder nb = nbp.getNameBuilder();
		for (var line : result) {
			StringBuilder output = new StringBuilder();
			output.append(line.toString());
			output.append(" => ");

			StringBuilder sb = new StringBuilder();
			try {
				nb.execute(line, sb);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("No such group found.");
				return;
			}
			output.append(sb.toString());
			env.writeln(output.toString());
		}

	}

	/**
	 * Method thatprints groups that were matched with provided regex for every file
	 * in folder provided
	 * 
	 * @param fromFolder resolved first path
	 * @param toFolder   resolved second path
	 * @param inputRegex Regex used for determining whether files in fromFolder are
	 *                   acceptable for operation
	 * @param env        Environment used for output of data
	 * @throws IOException If path can not be obtained
	 */
	private void groupsTask(Path fromFolder, Path toFile, String inputRegex, Environment env) throws IOException {
		List<FilterResult> result = filter(fromFolder, inputRegex);
		for (var line : result) {
			env.write(line.toString() + " ");
			for (int i = 0; i < line.numberOfGroups(); i++) {
				env.write(i + ": ");
				env.write(line.group(i));
			}
			env.writeln("");
		}
	}

	/**
	 * method that prints only files that fit provided regex for every file in
	 * folder provided.
	 * 
	 * @param fromFolder resolved first path
	 * @param toFolder   resolved second path
	 * @param inputRegex Regex used for determining whether files in fromFolder are
	 *                   acceptable for operation
	 * @param env        Environment used for output of data
	 * @throws IOException If path can not be obtained
	 */
	private void filterTask(Path fromFolder, Path toFile, String inputRegex, Environment env) throws IOException {
		Objects.requireNonNull(fromFolder);
		Objects.requireNonNull(inputRegex);
		Objects.requireNonNull(env);

		List<FilterResult> result;
		try {
			result = filter(fromFolder, inputRegex);
		} catch (PatternSyntaxException e) {
			env.writeln("Invalid pattern provided");
			return;
		}
		for (var line : result) {
			env.writeln(line.toString());
		}
	}

	/**
	 * Method that filters files from provided directory and ouputs them as
	 * FilterResults
	 * 
	 * @param dir  resolved source path
	 * @param mask Regex used for determining whether files in fromFolder are
	 *             acceptable
	 * @return List of acceptable files, stored in FilterResult form
	 * @throws IOException If path can not be obtained
	 */
	private static List<FilterResult> filter(Path dir, String mask) throws IOException {
		Objects.requireNonNull(dir, "Provided directory can not be null");
		Objects.requireNonNull(mask, "Provided pattern can not be null");

		List<FilterResult> listToReturn = new ArrayList<FilterResult>();
		Pattern pattern = Pattern.compile(mask, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		List<String> listOfChildren = Files.list(dir).filter(t -> Files.isRegularFile(t))
				.map(t -> t.getName(t.getNameCount() - 1).toString()).collect(Collectors.toList());
		for (var fileName : listOfChildren) {
			List<String> groups = new ArrayList<String>();
			Matcher m = pattern.matcher(fileName);
			if (!m.matches())
				continue;

			for (int i = 0; i <= m.groupCount(); i++) {
				groups.add(m.group(i));
			}
			listToReturn.add(new FilterResult(fileName, groups));
		}
		return listToReturn;
	}

	@Override
	public String getCommandName() {
		return "massrename";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("his command enables four things with its for subcommands. First; filter ->");
		list.add("it prints only files that fit provided regex for every file in folder");
		list.add("provided Second; groups -> it prints groups that were matched with provided");
		list.add("regex for every file in folder provided Third; show -> enables preview of");
		list.add("file names if we decide to do move of all of those that meet provided");
		list.add("criteria Forth; execute -> moves all files from first path to another and");
		list.add("renames them accordingly.");
		list.add("");
		list.add("Syntax for this method is massrename path1 path2 subCommandName inputRegex or");
		list.add("massrename path1 path2 subCommandName inputRegex outputRegex if execute");
		list.add("subcomand is used.");
		return Collections.unmodifiableList(list);
	}

}
