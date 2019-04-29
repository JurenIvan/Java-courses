package hr.fer.zemris.java.hw08.shell.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility class containing method split that splits input line into array of
 * arguments.
 * 
 * @author juren
 *
 */
public class Splitter {

	/**
	 * Method that parses given string into array of strings that represent an
	 * arguments. If argument contains double quotes, escaping is enabled. Meaning
	 * that we can have double quotes inbetween other two double quotes.
	 * 
	 * @param arg
	 * @return array of "words" that user inputed
	 */
	public static String[] split(String arg) {
		if (arg == null)
			return new String[0];
		QLexer ql = new QLexer(arg);
		return ql.getArgs();
	}

	/**
	 * Class representing a special kind of lexer that knows all rules for splitting
	 * and doesn't need parser to control it's states. QLexer has "2 states". One is
	 * when normal text is read, other when it's situated inside double quotes. Text
	 * is everything between start and first " and then after second " etc.
	 * .Everything else is considered part of String and it has separate and
	 * different set of rules for Splitting.
	 * 
	 * @author juren
	 *
	 */
	private static class QLexer {
		/**
		 * Array of input chars. Text that it has to tokenize.
		 */
		private char[] data;
		/**
		 * Represents index of character where we got while tokenizing.
		 */
		private int currentIndex;
		/**
		 * Holds the tokens that it parsed.
		 */
		private List<String> args;

		/**
		 * Constructor for this class. It receives String and turns it into array of
		 * characters.
		 * 
		 * @param text String that lexer turns into tokens
		 * 
		 * @throws NullPointerException if null reference is provided
		 */
		public QLexer(String text) {
			Objects.requireNonNull(text);
			this.data = text.toCharArray();
			this.currentIndex = 0;
			args = new ArrayList<String>();
			solve();
		}

		/**
		 * Getter for generated list of arguments.
		 * 
		 * If in any argument, whitespacaces are found,
		 * 
		 * @return array of parsed arguments
		 */
		public String[] getArgs() {
			String[] toBeReturned = new String[args.size()];
			for (int i = 0; i < args.size(); i++) {
				toBeReturned[i] = args.get(i);// .trim();
			}
			return toBeReturned;
		}

		/**
		 * Method that is equivalent to iterative calling of nextToken method in any
		 * lexer. This application requires all arguments parsed at the same time so
		 * this method parses whole input at once.
		 */
		public void solve() {
			while (currentIndex < data.length) {
				if (data[currentIndex] == '\"') {
					currentIndex++;
					String text = readQuotes();
					// if (text.length() > 0) {
					// if command "" "" is interpreted as command with no
					// arguments, diable this comments
					args.add(text);
					// }
					if (currentIndex < data.length && data[currentIndex] != ' ')
						throw new IllegalArgumentException(
								"Having double quote or any char (except space) right after closing double quote is not allowed.");
				}
				String text = readNormal();
				if (text.length() > 0) {
					args.add(text);
				}
			}
		}

		/**
		 * Method that is called when Qlexer read normal arguments. It knows how to make
		 * a argument out of string. Method changes currentIndex.
		 * 
		 * @return String(argument) made by reading part of input char array
		 */
		private String readNormal() {
			StringBuilder sb = new StringBuilder();
			while (currentIndex < data.length) {
				if (data[currentIndex] == '\"') {
					return sb.toString();
				} else if (Character.isWhitespace(data[currentIndex])) {
					currentIndex++;
					return sb.toString();
				}

				sb.append(data[currentIndex++]);
			}
			return sb.toString();
		}

		/**
		 * Method that is called when string is detected. It knows how to make an
		 * argument out of string. Method changes currentIndex.
		 * 
		 * @return String(argument) made by reading part of input char array
		 * 
		 */
		private String readQuotes() {
			StringBuilder sb = new StringBuilder();
			while (currentIndex < data.length) {
				if (currentIndex < data.length - 1 && data[currentIndex] == '\\') {
					if (data[currentIndex + 1] == '\"') {
						sb.append('\"');
						currentIndex += 2;
						continue;
					}
					sb.append("\\");
					currentIndex++;
				}
				if (data[currentIndex] == '\"') {
					currentIndex++;
					return sb.toString();
				}
				sb.append(data[currentIndex++]);
			}
			throw new IllegalArgumentException("Never ending string. Try closing the quotes.");
		}

	}
}
