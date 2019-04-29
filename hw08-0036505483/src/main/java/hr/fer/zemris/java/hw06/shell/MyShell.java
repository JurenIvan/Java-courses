package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ListdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MassrenameShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PopdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PushdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PwdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

/**
 * Class representing implementation of basic shell. Has main method. Has ten
 * commands: cat, charsets, copy, exit, help, hexdump, ls, mkdir, symbol, tree.
 * 
 * @author juren
 *
 */
public class MyShell {
	
	/**
	 * Variable used to store default welcomeMessage
	 */
	private static final String WELCOME_MESSAGE = "Welcome to MyShell v 1.0";
	/**
	 * Variable used to store default multiline symbol
	 */
	private static final Character DEFAULT_MULTILINE_SYMBOL = '|';
	/**
	 * Variable used to store default prompt symbol
	 */
	private static final Character DEFAULT_PROMPT_SYMBOL = '>';
	/**
	 * Variable used to store default morelines symbol
	 */
	private static final Character DEFAULT_MORELINES_SYMBOL = '\\';

	/**
	 * Method used to start program(shell).
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println(WELCOME_MESSAGE);

		ShellStatus shellStatus = ShellStatus.CONTINUE;
		Environment environment = new EnvironmentImpl();

		try {
			do {
				String line = environment.readLine();

				int index = line.indexOf(" ") >= line.indexOf("\t") ? line.indexOf(" ") : line.indexOf("\t");
				String arguments = index != -1 ? line.substring(index) : null;

				ShellCommand command = environment.commands().get(index != -1 ? line.subSequence(0, index) : line);
				if (command == null) {
					environment.writeln("Unsuported command");
					continue;
				}
				shellStatus = command.executeCommand(environment, arguments);

			} while (shellStatus != ShellStatus.TERMINATE);
		} catch (ShellIOException e) {
			System.out
					.println("\nFatal Error Occured. No communication between shell and user is considered possible.");
		}
	}

	/**
	 * Implementation of Environment used for shell.
	 * 
	 * @author juren
	 *
	 */
	private static class EnvironmentImpl implements Environment {
		
		/**
		 * Map used for dataStorage
		 */
		private Map<String,Object> sharedData;
		
		/**
		 * Variable that stores current path
		 */
		private Path currentPath;

		/**
		 * variable that stores multilineSymbol used in Environment
		 */
		private Character multilineSymbol;
		/**
		 * variable that stores promptSymbol used in Environment
		 */
		private Character promptSymbol;
		/**
		 * variable that stores moreLinesSymbol used in Environment
		 */
		private Character moreLinesSymbol;
		/**
		 * Sorted map containing names and implementations of commands
		 */
		private SortedMap<String, ShellCommand> commands;
		/**
		 * Scanner used for communication Stream with used.
		 */
		private Scanner sc;

		/**
		 * Standard constructor. Initializes default values and collections used.
		 */
		public EnvironmentImpl(/* InputStream is,OutputStream os */) {
			multilineSymbol = DEFAULT_MULTILINE_SYMBOL;
			promptSymbol = DEFAULT_PROMPT_SYMBOL;
			moreLinesSymbol = DEFAULT_MORELINES_SYMBOL;
			AddAllCommands();
			
			currentPath=Path.of(".").toAbsolutePath().normalize();
			sharedData=new HashMap<String, Object>();
			
			sc = new Scanner(System.in);
		}

		/**
		 * Helper method to organize constructor, used to put all commands in Map.
		 */
		private void AddAllCommands() {
			commands = new TreeMap<String, ShellCommand>();
			commands.put("charsets", new CharsetsShellCommand());
			commands.put("cat", new CatShellCommand());
			commands.put("ls", new LsShellCommand());
			commands.put("tree", new TreeShellCommand());
			commands.put("copy", new CopyShellCommand());
			commands.put("mkdir", new MkdirShellCommand());
			commands.put("hexdump", new HexdumpShellCommand());
			commands.put("help", new HelpShellCommand());
			commands.put("symbol", new SymbolShellCommand());
			commands.put("exit", new ExitShellCommand());
			
			commands.put("cd", new CdShellCommand());
			commands.put("pwd", new PwdShellCommand());
			commands.put("pushd",new PushdShellCommand());
			commands.put("popd",new PopdShellCommand());
			commands.put("listd",new ListdShellCommand());
			commands.put("massrename",new MassrenameShellCommand());
			

			commands = Collections.unmodifiableSortedMap(commands);
		}

		@Override
		public String readLine() throws ShellIOException {
			StringBuilder line = new StringBuilder();
			String lastLine;
			write(getPromptSymbol() + "");

			if (!sc.hasNextLine())
				throw new ShellIOException("Line expected.");
			line.append(sc.nextLine());
			lastLine = line.toString();

			while (lastLine.endsWith(getMorelinesSymbol() + "")) {
				write(getMultilineSymbol() + "");
				line.setLength(line.length() - 1);
				if (!sc.hasNextLine())
					throw new ShellIOException("Line expected.");
				lastLine = sc.nextLine();
				line.append(lastLine);
			}
			return line.toString().trim();
		}

		@Override
		public void write(String text) throws ShellIOException {
			try {
				System.out.print(text + " ");
			} catch (Exception e) {
				throw new ShellIOException("Unable to display text.");
			}
		}

		@Override
		public void writeln(String text) throws ShellIOException {
			try {
				System.out.println(text + " ");
			} catch (Exception e) {
				throw new ShellIOException("Unable to display text.");
			}
		}

		@Override
		public Character getMultilineSymbol() {
			return multilineSymbol;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			this.multilineSymbol = symbol;
		}

		@Override
		public Character getPromptSymbol() {
			return promptSymbol;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			this.promptSymbol = symbol;
		}

		@Override
		public Character getMorelinesSymbol() {
			return moreLinesSymbol;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			moreLinesSymbol = symbol;
		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return commands;
		}

		@Override
		public Path getCurrentDirectory() {
			return this.currentPath;
		}

		@Override
		public void setCurrentDirectory(Path path) {
			Objects.requireNonNull(path,"null reference can not be path");
			this.currentPath=path.normalize();
		}

		@Override
		public Object getSharedData(String key) {
			return sharedData.get(key);
		}

		@Override
		public void setSharedData(String key, Object value) {
			Objects.requireNonNull(key,"null reference can not be used as a key");
			sharedData.put(key, value);
		}
	}
}
