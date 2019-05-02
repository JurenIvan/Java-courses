package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw08.shell.util.Splitter;
import hr.fer.zemris.java.hw08.shell.util.Util;

/**
 * This class represents implementation for hexdump command.
 * 
 * This command produces hex-output of file content. This command expects a
 * single argument: file name. On the right side of the display only a standard
 * subset of characters is shown because all bytes whose value is less than 32
 * or greater than 127 are replaced with '.'.
 * 
 * @author juren
 *
 */
public class HexdumpShellCommand implements ShellCommand {
	/**
	 * Defines minimal char value that isn't replaced with dot
	 */
	private static final int MIN_VALUE_TO_DISPLAY = 32;
	/**
	 * Defines maximal char value that isn't replaced with dot
	 */
	private static final int MAX_VALUE_TO_DISPLAY = 127;

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
		} catch (InvalidPathException e) {
			env.writeln("Path is invalid.");
			return ShellStatus.CONTINUE;
		}
		
		if (!Files.isRegularFile(directory)) {
			env.writeln("hexdump can dump only files.");
			return ShellStatus.CONTINUE;
		}

		hexOuput(env, directory);

		return ShellStatus.CONTINUE;
	}

	/**
	 * Method used to output content of file content. This command expects a single
	 * argument: file name. On the right side of the display only a standard subset
	 * of characters is shown because all bytes whose value is less than 32 or
	 * greater than 127 are replaced with '.'.
	 * 
	 * @param env       used as output stream for data with its write method
	 * @param directory source that is read
	 */
	private void hexOuput(Environment env, Path directory) {
		try (InputStream is = Files.newInputStream(directory, StandardOpenOption.READ)) {
			byte[] buff = new byte[1024 * 4];
			byte[] outputBuff = new byte[16];
			int outputPosition = 0, r, counter = 0;

			while ((r = is.read(buff)) > 0) {
				int i = 0;
				while (r > 0) {
					while (outputPosition < 16 && r > 0) {
						outputBuff[outputPosition++] = buff[i++];
						r--;
					}
					if (outputPosition == 16) {
						env.writeln(displayHex(counter, outputBuff, outputPosition));
						counter = counter + 1;
						outputPosition = 0;
					}
				}
			}
			env.writeln(displayHex(counter, outputBuff, outputPosition));
		} catch (IOException e) {
			env.writeln("Error occured.");
		}
	}

	/**
	 * Method used to output content buffer. On the right side of the display only a
	 * standard subset of characters is shown because all bytes whose value is less
	 * than 32 or greater than 127 are replaced with '.'. In the middle there is hex
	 * representation of file, and on the left is cardinal number of line.
	 * 
	 * @param counter        cardinal number of line
	 * @param outputBuff     array of data to be displayed
	 * @param outputPosition number of chars in array to be displayed
	 * @return String line with appropriate representation of data
	 */
	private String displayHex(int counter, byte[] outputBuff, int outputPosition) {
		String nonZeroHexPart = Integer.toHexString(counter * 16);
		StringBuilder sb = new StringBuilder();
		sb.append("0".repeat(8 - nonZeroHexPart.length()) + nonZeroHexPart.toUpperCase());
		for (int outher = 0; outher < 2; outher++) {
			for (int i = 0; i < 8; i++) {
				sb.append(" ");
				if (outputPosition <= i || (outher*8+i>=outputPosition)) {
					sb.append("  ");
				} else {
					sb.append(Util.transformToString(outputBuff[outher * 8 + i]).toUpperCase());
				}
			}
			sb.append("|");
		}

		for (int i = 0; i < 16 && i < outputPosition; i++) {
			sb.append((char) (outputBuff[i] < MIN_VALUE_TO_DISPLAY || outputBuff[i] > MAX_VALUE_TO_DISPLAY ? (byte) '.'
					: outputBuff[i]));
		}
		return sb.toString();
	}

	@Override
	public String getCommandName() {
		return "hexdump";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("This command produces hex-output of file content");
		list.add("This command expects a single argument: file name.");
		list.add("On the right side of the display only a standard subset of characters is shown because ");
		list.add("all bytes whose value is less than 32 or greater than 127 are replaced  with '.')");
		list.add("");
		return Collections.unmodifiableList(list);
	}

}
