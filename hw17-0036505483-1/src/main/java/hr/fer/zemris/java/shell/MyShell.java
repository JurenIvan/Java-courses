package hr.fer.zemris.java.shell;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Class containing main method used to start the shell application which
 * expects url to folder with multiple documents. After the app is started it
 * has 4 commands :"query", "results", "type", "exit".
 * 
 * @author juren
 *
 */
public class MyShell {

	private static final double VARIATION_FACTOR = 1.000000001;
	private static final double MINIMUM_MATCH_NUMBER = 0.000001;
	/**
	 * Variable used to store default welcomeMessage
	 */
	private static final String WELCOME_MESSAGE = "Welcome to --Trazilica--";
	/**
	 * Variable storing string representing path to stopWords
	 */
	private static final String pathToStopWords = "./src/main/resources/stopWords.txt";
	/**
	 * Constant storing string used to separate properties part from data part when
	 * printing data
	 */
	private static final String SEPARATOR = "-----------------------------------------------------------";

	/**
	 * Map storing frequencies of all words (except ones that we escaped because
	 * they're too common ) that were found among given documents
	 */
	private static Map<String, Integer> vocabular = new HashMap<>();
	/**
	 * Map that has string as a key. that key is path to document that we have read.
	 * value stored is another map that has pairs of words and their frequencies.
	 */
	private static Map<String, Map<String, Integer>> nameTable = new HashMap<>();

	/**
	 * List of all words used. MUST BE SORTED!
	 */
	private static List<String> vocabularList = new ArrayList<>(vocabular.keySet());
	/**
	 * List of all paths used. MUST BE SORTED!
	 */
	private static List<String> filesList = new ArrayList<>(nameTable.keySet());
	/**
	 * 2D vector of word frequencies per document. With given order of vocabularList
	 * and filesList frequencies of words are written into this array. Example:
	 * wordFreqPerDoc[ordinal number of file][ordinal number of word]=frequency .
	 */
	private static int[][] wordFreqPerDoc;
	/**
	 * 2D vector of term frequency-inverse document frequency per document. With
	 * given order of vocabularList and filesList tfidf of words are written into
	 * this array. Example: tfidf[ordinal number of file][ordinal number of
	 * word]=tfidfValue .
	 */
	private static double[][] tfidf;

	/**
	 * Map storing most recent results. Keys are numbers that represent match value
	 * and value stored are paths to folders matched against.
	 */
	private static TreeMap<Double, String> results;

	/**
	 * Method used to start program(shell).
	 * 
	 * @param args used for getting the initial path for articles
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println(WELCOME_MESSAGE);
		if (args.length == 0) {
			System.out.println("Expected absolute path to articles!");
			return;
		}

		buildVocabular(args[0]);
		buildFreqVector();

		Scanner sc = new Scanner(System.in);
		ShellStatus shellStatus = ShellStatus.CONTINUE;

		do {
			System.out.print("\nEnter command >");
			String line = sc.nextLine().trim();
			int index = line.indexOf(" ") >= line.indexOf("\t") ? line.indexOf(" ") : line.indexOf("\t");

			String arguments = index != -1 ? line.substring(index).trim() : null;
			String command = index != -1 ? line.subSequence(0, index).toString().trim() : line;

			shellStatus = doCommand(command, arguments);

		} while (shellStatus == ShellStatus.CONTINUE);
		sc.close();
	}

	/**
	 * Method used for switching to proper command.
	 * 
	 * @param command   String storing key to shell command
	 * @param arguments arguments given alongside command (expects null if no such
	 *                  exists)
	 * @return {@link ShellStatus} enum that holds information whether given
	 *         operation has to terminate shell or is continuing allowed
	 */
	private static ShellStatus doCommand(String command, String arguments) {
		Objects.requireNonNull(command, "command must be provided");

		if (command.equals("query"))
			return doQuerry(arguments);
		if (command.equals("type"))
			return doTypeCommand(arguments);
		if (command.equals("results"))
			return doResultsCommand(arguments);
		if (command.equals("exit"))
			return doExitCommand(arguments);

		System.out.println("Unsupported command!");
		return ShellStatus.CONTINUE;
	}

	/**
	 * Method used for executing query command.
	 * 
	 * @param arguments string holding word(s) that will be searched for
	 * @return {@link ShellStatus} enum that holds information whether given
	 *         operation has to terminate shell or is continuing allowed
	 */
	private static ShellStatus doQuerry(String arguments) {
		if (arguments.length() == 0) {
			System.out.println("Text expected!");
			return ShellStatus.CONTINUE;
		}
		List<String> words = parseLine(arguments);
		System.out.println("Query is:" + words);

		// frequencies
		int[] freq = new int[vocabularList.size()];
		for (var word : words) {
			int wordIndex = getIndexInList(vocabularList, word);
			if (wordIndex > 0) {
				freq[wordIndex] += 1;
			}
		}

		// tfidf value
		double[] tfidfForQuerry = new double[vocabularList.size()];
		for (int j = 0; j < vocabularList.size(); j++) {
			if (freq[j] == 0)
				continue;
			tfidfForQuerry[j] = freq[j] * Math.log(filesList.size() / howManyDocumentsHaveIt(j, wordFreqPerDoc));
		}

		results = compareToAll(tfidfForQuerry);
		System.out.println("Najboljih " + 10 + " rezultata");
		printResults(10);

		return ShellStatus.CONTINUE;
	}

	/**
	 * Method used for executing type command. Prints document whose result was
	 * n-th, where n is provided as argument
	 * 
	 * @param arguments string holding number of result that will be printed
	 * @return {@link ShellStatus} enum that holds information whether given
	 *         operation has to terminate shell or is continuing allowed
	 */
	private static ShellStatus doTypeCommand(String arguments) {
		if (arguments.length() != 1) {
			System.out.println("Order number of result expected");
			return ShellStatus.CONTINUE;
		}

		int num = Integer.parseInt(arguments);
		String path = null;
		for (var entry : results.entrySet()) {
			path = entry.getValue();
			if (num-- == 0)
				break;
		}
		if (num >= 0) {
			System.out.println("No such result!");
			return ShellStatus.CONTINUE;
		}

		try {
			outputData(path);
		} catch (IOException e) {
			System.out.println("Error while opening file " + path);
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Method that reads and prints document whose path was provided to System.out
	 * 
	 * @param path path of file that will be written in console
	 * @throws IOException if no such file exist
	 */
	private static void outputData(String path) throws IOException {
		System.out.println(SEPARATOR);
		System.out.println("Dokument: " + path);
		System.out.println(SEPARATOR);

		List<String> content = Files.readAllLines(Paths.get(path));
		for (var line : content) {
			System.out.println(line);
		}
		System.out.println(SEPARATOR);
	}

	/**
	 * Method used for executing results command. Prints results of last query.
	 * 
	 * @param arguments not used. (does not execute if arguments are provided)
	 * @return {@link ShellStatus} enum that holds information whether given
	 *         operation has to terminate shell or is continuing allowed
	 */
	private static ShellStatus doResultsCommand(String arguments) {
		if (arguments != null) {
			System.out.println("No arguments expected!");
			return ShellStatus.CONTINUE;
		}

		printResults(10);
		return ShellStatus.CONTINUE;
	}

	/**
	 * Method used for exiting from shell.
	 * 
	 * @param arguments not used. (does not execute if arguments are provided)
	 * @return {@link ShellStatus} enum that holds information whether given
	 *         operation has to terminate shell or is continuing allowed
	 */
	private static ShellStatus doExitCommand(String arguments) {
		if (arguments == null)
			return ShellStatus.TERMINATE;
		System.out.println("Illegal number of arguments");
		return ShellStatus.CONTINUE;
	}

	/**
	 * Method that holds bundle of commands used to build {@link #wordFreqPerDoc}
	 * {@link #tfidf}
	 */
	private static void buildFreqVector() {
		vocabularList = new ArrayList<>(vocabular.keySet());
		vocabularList.sort(null);
		filesList = new ArrayList<>(nameTable.keySet());
		filesList.sort(null);

		wordFreqPerDoc = new int[filesList.size()][vocabularList.size()];

		// frequency
		for (var table : nameTable.entrySet()) {
			int fileIndex = getIndexInList(filesList, table.getKey());
			for (var word : table.getValue().entrySet()) {
				wordFreqPerDoc[fileIndex][getIndexInList(vocabularList, word.getKey())] += word.getValue();
			}
		}

		tfidf = new double[filesList.size()][vocabularList.size()];

		// tfidf
		for (int i = 0; i < filesList.size(); i++) {
			for (int j = 0; j < vocabularList.size(); j++) {
				if (wordFreqPerDoc[i][j] == 0)
					continue;
				tfidf[i][j] = wordFreqPerDoc[i][j]
						* Math.log(filesList.size() / howManyDocumentsHaveIt(j, wordFreqPerDoc));
			}
		}
	}

	/**
	 * Method that goes through {@link #wordFreqPerDoc} and count how much documents
	 * have had this word
	 * 
	 * @param j              index of word in ordered list {@link #vocabularList }
	 * @param wordFreqPerDoc {@link #wordFreqPerDoc}
	 * @return frequency of that words in all documents
	 */
	private static int howManyDocumentsHaveIt(int j, int[][] wordFreqPerDoc) {
		int counter = 0;
		for (int i = 0; i < filesList.size(); i++) {
			if (wordFreqPerDoc[i][j] > 0)
				counter++;
		}
		return counter;
	}

	/**
	 * Method that goes through all files in provided folder and registers all words
	 * that have been used
	 * 
	 * @param pathToArticles path provided by used from where all files are read
	 * @throws IOException if no such folder exist
	 */
	private static void buildVocabular(String pathToArticles) throws IOException {
		Files.walkFileTree(Paths.get(pathToArticles), new SimpleFileVisitor<Path>() {

			private List<String> stopWords = Files.readAllLines(Paths.get(pathToStopWords));

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				List<String> linesOfFile = Files.readAllLines(file);
				Map<String, Integer> documentVocabular = new HashMap<>();

				for (String line : linesOfFile) {
					List<String> words = parseLine(line);
					for (var word : words) {
						documentVocabular.merge(word, 1, Integer::sum);
						vocabular.merge(word, 1, Integer::sum);
					}
				}

				for (String stopWord : stopWords) {
					documentVocabular.remove(stopWord);
				}

				nameTable.put(file.toAbsolutePath().toString(), documentVocabular);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path file, IOException exc) throws IOException {
				for (String stopWord : stopWords)
					vocabular.remove(stopWord);
				return FileVisitResult.CONTINUE;
			}
		});

		System.out.println("Veličina riječnika je " + vocabular.size() + " riječi");
	}

	/**
	 * Method used to parse line of either user input or line from text. Used
	 * because if for example two words are connected by numbers ("house123doctor")
	 * it should be interpret as "house" and "doctor"
	 * 
	 * @param line String that is parsed
	 * @return List of words parsed by rule stated above
	 */
	private static List<String> parseLine(String line) {
		List<String> words = new ArrayList<String>();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < line.length(); i++) {
			char currChar = line.charAt(i);
			while (Character.isAlphabetic(currChar)) {
				sb.append(currChar);
				i++;
				if (i >= line.length())
					break;
				currChar = line.charAt(i);
			}
			String word = sb.toString().toLowerCase().trim();
			if (word.isBlank())
				continue;
			words.add(word);
			sb = new StringBuilder();
		}
		return words;
	}

	/**
	 * Method that prints i best matches to console
	 * 
	 * @param i number of results that will (at most) be printed.
	 */
	private static void printResults(int i) {
		int counter = 0;
		for (var entry : results.entrySet()) {
			System.out.printf("[%2d] (%5.4f) %s %n", counter++, entry.getKey(), entry.getValue());
			if (counter > i - 1)
				break;
		}
	}

	/**
	 * Method that compares provided vector to all other vectors pre-computed from
	 * given folder.
	 * 
	 * @param tfidfForQuerry {@link #tfidf} for users query
	 * @return double representing match-value
	 */
	private static TreeMap<Double, String> compareToAll(double[] tfidfForQuerry) {
		TreeMap<Double, String> results = new TreeMap<>((f, s) -> Double.compare(s, f));

		for (int noOfPath = 0; noOfPath < filesList.size(); noOfPath++) {
			double[] documentArray = new double[vocabularList.size()];
			for (int i = 0; i < vocabularList.size(); i++) {
				documentArray[i] = tfidf[noOfPath][i];
			}

			double matchingResult = Vectors.cosBetweenVectors(documentArray, tfidfForQuerry);
			if (matchingResult > MINIMUM_MATCH_NUMBER) {
				while (results.containsKey(matchingResult)) {
					matchingResult = matchingResult * VARIATION_FACTOR;
				}
				results.put(matchingResult, filesList.get(noOfPath));
			}
		}
		return results;
	}

	/**
	 * Gets index of item stored in a array. Requires sorted list. Uses binary
	 * search. check {@link #Collections.binarySearch}
	 * 
	 * @param filesList SORTED list
	 * @param key       of object that is searched for
	 * @return index of item
	 */
	private static int getIndexInList(List<String> filesList, String key) {
		return Collections.binarySearch(filesList, key);
	}

}
