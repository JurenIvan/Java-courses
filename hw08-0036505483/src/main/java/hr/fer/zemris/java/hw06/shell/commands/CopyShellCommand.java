package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw08.shell.util.Splitter;

/**
 * 
 * This class represents implementation for help command.
 * 
 * This command enables copying of files. The copy command expects two
 * arguments: source file name and destination file name If destination file
 * exists, shell asks user is it allowed to overwrite it. lCommand works only
 * with files
 * 
 * If the second argument is directory, command assumes that user wants to copy
 * the original file into that directory using the original file name.
 * 
 * @author juren
 *
 */
public class CopyShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args=null;
		try {
			args = Splitter.split(arguments);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if (args.length != 2) {
			env.writeln("Should have been exactly two argument.");
			return ShellStatus.CONTINUE;
		}
		Path fromFile = null;
		Path toFile = null;
		try {
			fromFile = env.getCurrentDirectory().resolve(Paths.get(args[0]));
			toFile = env.getCurrentDirectory().resolve(Paths.get(args[1]));
		} catch (InvalidPathException e) {
			env.writeln("Invalid path provided.");
			return ShellStatus.CONTINUE;
		}
		if (!Files.isRegularFile(fromFile)) {
			env.writeln("First argument should have been path to file");
			return ShellStatus.CONTINUE;
		}

		if (Files.isDirectory(toFile)) {
			toFile =toFile.resolve(fromFile.getFileName());
		}

		try {
			if (Files.exists(toFile)) {
				env.writeln("This file already exists, do you want to overwrite? Y/N");
				String input = env.readLine();
				if (input.toLowerCase().equals("y")) {
					copyFile(fromFile, toFile);
					env.writeln("File copied succesfully");
					return ShellStatus.CONTINUE;
				}
				env.writeln("Copy cancelled.");
				return ShellStatus.CONTINUE;
			}
			copyFile(fromFile, toFile);
			env.writeln("File copied succesfully");
		} catch (ShellIOException | IOException e) {
			env.writeln("Error occured while copying.");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Method that copies source file to destination path. This method is set to
	 * overwrite existing files.
	 * 
	 * @param source file to be copied
	 * @param dest   path where file will be copied
	 * @throws IOException if copying can not be executed
	 */
	private static void copyFile(Path source, Path dest) throws IOException {
		try (InputStream is = Files.newInputStream(source); OutputStream os = Files.newOutputStream(dest)) {
			byte[] buff = new byte[1024 * 4];
			int r;
			while ((r = is.read(buff)) > 0) {
				os.write(buff, 0, r);
			}
		}
	}

	@Override
	public String getCommandName() {
		return "copy";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("This command enables copying of files.");
		list.add("The copy command expects two arguments: source file name and destination file name");
		list.add("If destination file exists, shell asks user is it allowed to overwrite it. ");
		list.add("Command works only with files");
		list.add(
				"If the second argument is directory, command assumes that user wants to copy the original file into that directory using the original file name. ");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
