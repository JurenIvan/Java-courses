package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw08.shell.util.Splitter;

/**
 * This class represents implementation for tree command.
 * 
 * This command is used to recursively display folders tree structure. The tree
 * command expects a single argument: directory name. Command prints a tree
 * (each directory level shifts output two characters to the right.
 * 
 * @author juren
 *
 */
public class TreeShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args=null;
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
		if (!Files.exists(directory)) {
			env.writeln("Provided path doesn't exits.");
			return ShellStatus.CONTINUE;
		}

		if (!Files.isDirectory(directory)) {
			env.writeln("tree function can recursively print only directories.");
			return ShellStatus.CONTINUE;
		}

		try {
			Files.walkFileTree(directory, new FolderWalker(env));
		} catch (IOException e) {
			env.writeln("Error occured. Unable to recursively print directories.");
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "tree";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("This command is used to recursively display folders tree structure");
		list.add("The tree command expects a single argument: directory name");
		list.add("Command prints a tree (each directory level shifts output two characters to the right.");
		list.add("");
		return Collections.unmodifiableList(list);
	}

	/**
	 * Implementation of {@link FileVisitor} that recursively walks directory and
	 * prints its children.
	 * 
	 * @author juren
	 *
	 */
	private static class FolderWalker extends SimpleFileVisitor<Path> {
		/**
		 * Variable that saves level(how deep are we) in file structure.
		 */
		private int level;
		/**
		 * variable that saves Environment for this class
		 */
		private Environment env;

		/**
		 * Standard constructor which takes and saves Environment so that fileVisitor can output data
		 * 
		 * @param env Environment to output data through 
		 */
		public FolderWalker(Environment env) {
			this.env = env;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			env.writeln(" ".repeat(2 * level) + dir.getFileName());
			level++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			env.writeln(" ".repeat(2 * level) + file.getFileName());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			level--;
			return FileVisitResult.CONTINUE;
		}

	}

}
