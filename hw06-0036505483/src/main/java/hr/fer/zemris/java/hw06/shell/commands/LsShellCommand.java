package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.Splitter;

/**
 * This class represents implementation if ls command.
 * 
 * This command is used to display files and directories in provided folder with
 * some basic data: Command ls takes a single argument – directory name Writes a
 * directory listing (not recursive).
 * 
 * @author juren
 *
 */
public class LsShellCommand implements ShellCommand {

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

		Path directory = Paths.get(args[0]);
		if (!Files.isDirectory(directory)) {
			env.writeln("Provided argument should have been path to directory.");
			return ShellStatus.CONTINUE;
		}
		env.writeln("List of files and directories in provided folder with some basic data:");
		try {
			Files.list(directory).forEach(t -> env.writeln(getDataString(env, t)));
		} catch (IOException e) {
			env.writeln("IO error occured while reading data.");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Method that models output for file provided. It prints certain flags, size,
	 * date of last edit, and name
	 * 
	 * @param env used as output stream for data with its write method
	 * @param t   file for which this method models output line
	 * @return line with data for this file
	 */
	private String getDataString(Environment env, Path path) {
		try {
			StringBuilder sb = new StringBuilder();

			sb.append(Files.isDirectory(path) ? "d" : "-");
			sb.append(Files.isReadable(path) ? "r" : "-");
			sb.append(Files.isWritable(path) ? "w" : "-");
			sb.append(Files.isExecutable(path) ? "x" : "-");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
					LinkOption.NOFOLLOW_LINKS);
			BasicFileAttributes attributes = faView.readAttributes();
			FileTime fileTime = attributes.creationTime();
			String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
			String dateAndTime = formattedDateTime;

			return String.format("%s %10d %s %s", sb.toString(), Files.size(path), dateAndTime, path.getFileName());
		} catch (Exception e1) {
			e1.printStackTrace();
			return "Error occured while reading data of file: " + path;
		}
	}

	@Override
	public String getCommandName() {
		return "ls";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("This command is used to display files and directories in provided folder with some basic data: ");
		list.add("Command ls takes a single argument – directory name");
		list.add("Writes a directory listing (not recursive).");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
