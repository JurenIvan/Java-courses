package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import hr.fer.zemris.java.hw05.db.QueryFilter;
import hr.fer.zemris.java.hw05.db.QueryParser;
import hr.fer.zemris.java.hw05.db.StudentDatabase;
import hr.fer.zemris.java.hw05.db.StudentRecord;

/**
 * Class that unifies every supported operation of this Database. it takes path
 * to text file with students records, makes database out of it, and allows us
 * to do some basic queries.
 * 
 * @author juren
 *
 */
public class StudentDB {

	/**
	 * Main method used to demonstrate capabilities of this database implementation.
	 * Reads file whose path is provided in args[0], and enables us to do some basic
	 * queries.
	 * 
	 * @param args are not used
	 */
	public static void main(String[] args) {
		StudentDatabase dataBase;
		try {
			dataBase = new StudentDatabase(makeStringFromInput());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return;
		}
		loopQueriesUntilExit(new Scanner(System.in), dataBase);
	}

	/**
	 * Method that takes scanner and loops itself as long as user doesn't enter
	 * "exit" and gives feedback on provided queries
	 * 
	 * @param sc       Scanner that represent input source for queries
	 * @param dataBase Database where all the records are stored
	 */
	private static void loopQueriesUntilExit(Scanner sc, StudentDatabase dataBase) {
		System.out.print("> ");
		while (sc.hasNextLine()) {
			String querryInput = sc.nextLine();
			if (querryInput.toLowerCase().equals("exit")) {
				System.out.println("GoodBye!");
				sc.close();
				return;
			}

			if (!querryInput.trim().startsWith("query")) {
				System.out.print(
						"query has to be formated like \" query variable operator value and variable operator value \"\n> ");
				continue;
			}

			QueryParser parser;
			try {
				parser = new QueryParser(querryInput.substring(querryInput.toLowerCase().indexOf("query ") + 6));

				List<StudentRecord> toOutput = new ArrayList<StudentRecord>();
				if (parser.isDirectQuery()) {
					StudentRecord r = dataBase.forJMBAG(parser.getQueriedJMBAG());
					System.out.println("Using index for record retrieval.");
					if (r != null) {
						toOutput.add(r);
					}
				} else {
					for (StudentRecord r : dataBase.filter(new QueryFilter(parser.getQuery()))) {
						toOutput.add(r);
					}
				}
				System.out.print(draw(toOutput) + "Records selected:" + toOutput.size() + "\n> ");

			} catch (Exception e) {
				System.out.print(e.getMessage() + "\n> ");
				continue;
			}
		}
		sc.close();
	}

	/**
	 * Method that reads input and returns list of records found in text file
	 * 
	 * @param args path to file where database is stored
	 * @return List of strings whose every represents one record in text form
	 */
	private static List<String> makeStringFromInput() {

		List<String> input;
		try {
			input = Files.readAllLines(Paths.get(".//database.txt"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IllegalArgumentException("File coudn't be read! Please inspect provided path");
		}
		return input;
	}

	/**
	 * Method used to draw table containing results of query
	 * 
	 * @param toOutput list of {@link StudentRecord} that has to be printed
	 * @return table in string format. can be printed via system.out.println();
	 */
	private static String draw(List<StudentRecord> toOutput) {
		Objects.requireNonNull(toOutput, "List of records is null reference");
		if (toOutput.size() == 0 || toOutput.contains(null))
			return "";

		int jmbagLen = toOutput.stream().mapToInt(t -> t.getJmbag().length()).max().getAsInt();
		int surnameLen = toOutput.stream().mapToInt(t -> t.getSurname().length()).max().getAsInt();
		int firstnameLen = toOutput.stream().mapToInt(t -> t.getName().length()).max().getAsInt();

		StringBuilder sb = new StringBuilder();
		sb.append(drawFirstLine(jmbagLen, surnameLen, firstnameLen));
		sb.append(drawTable(toOutput, jmbagLen, surnameLen, firstnameLen));
		sb.append(drawFirstLine(jmbagLen, surnameLen, firstnameLen));
		return sb.toString();
	}

	/**
	 * method used to display and align all student record that passed the filters.
	 * 
	 * @param toOutput     List of all records that should be printed
	 * @param jmbagLen     length of longest jmbag
	 * @param surnameLen   length of longest surname
	 * @param firstnameLen length of longest first name
	 * @return main body of resulting table
	 */
	private static String drawTable(List<StudentRecord> toOutput, int jmbagLen, int surnameLen, int firstnameLen) {

		Objects.requireNonNull(toOutput, "List of records is null reference");
		StringBuilder sb = new StringBuilder();
		for (var record : toOutput) {
			sb.append("| " + record.getJmbag() + " | " + record.getSurname());
			for (int i = 0; i < surnameLen - record.getSurname().length() + 1; i++) {
				sb.append(" ");
			}
			sb.append("| " + record.getName());
			for (int i = 0; i < firstnameLen - record.getName().length() + 1; i++) {
				sb.append(" ");
			}
			sb.append("| " + record.getGrade() + " |\n");
		}
		return sb.toString();
	}

	/**
	 * Method used to return string representing first and last line of table
	 * 
	 * @param jmbagLen     length of longest jmbag
	 * @param surnameLen   length of longest surname
	 * @param firstnameLen length of longest first name
	 * @return first line of resulting table
	 */
	private static String drawFirstLine(int jmbagLen, int surnameLen, int firstnameLen) {

		StringBuilder sb = new StringBuilder();
		sb.append("+");
		for (int i = 0; i < jmbagLen + 2; i++) {
			sb.append("=");
		}
		sb.append("+");
		for (int i = 0; i < surnameLen + 2; i++) {
			sb.append("=");
		}
		sb.append("+");
		for (int i = 0; i < firstnameLen + 2; i++) {
			sb.append("=");
		}
		sb.append("+===+\n");

		return sb.toString();
	}

}
