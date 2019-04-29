package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class containing few methods used for statistical analysis of students
 * records
 * 
 * @author juren
 *
 */
public class StudentDemo {

	/**
	 * Main method used to start the program.
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {
		String pathToTextFileWithStudentRecords = ".\\src\\main\\resources\\studenti.txt";
		List<StudentRecord> records = null;
		try {
			List<String> lines = Files.readAllLines(Paths.get(pathToTextFileWithStudentRecords));
			records = convert(lines);
		} catch (IOException e) {
			System.out.println("IO Error occured. Terminating program.");
			return;
		} catch (IllegalArgumentException e) {
			System.out.println("Program terminated due to");
			System.out.println(e.getMessage());
			return;
		}

		System.out.println("Zadatak 1\n=========");
		System.out.println(vratiBodovaViseOd25(records));

		System.out.println("Zadatak 2\n=========");
		System.out.println(vratiBrojOdlikasa(records));

		System.out.println("Zadatak 3\n=========");
		for (var entry : vratiListuOdlikasa(records)) {
			System.out.println(entry);
		}

		System.out.println("Zadatak 4\n=========");
		for (var entry : vratiSortiranuListuOdlikasa(records)) {
			System.out.println(entry);
		}

		System.out.println("Zadatak 5\n=========");
//		for (var entry : vratiPopisNepolozenih(records)) {
//			System.out.println(entry);
//		}
		System.out.println(vratiPopisNepolozenih(records));

		System.out.println("Zadatak 6\n=========");
		Map<Integer, List<StudentRecord>> result = razvrstajStudentePoOcjenama(records);
		for (var entry : result.keySet()) {
			System.out.println("Ocjena " + entry + " :" + result.get(entry));
		}

		System.out.println("Zadatak 7\n=========");
		System.out.println(vratiBrojStudenataPoOcjenama(records));

		System.out.println("Zadatak 8\n=========");
		Map<Boolean, List<StudentRecord>> trueFalseMap = razvrstajProlazPad(records);
		for (var entry : trueFalseMap.keySet()) {
			System.out.println(
					entry ? "Prosao: " + trueFalseMap.get(entry) : "Pao:   " + " " + trueFalseMap.get(entry));
		}

	}

	/**
	 * Method used to convert list of lines representing student records in String
	 * form, into list of Student records.
	 * 
	 * @param lines list of lines representing records
	 * @return list of Student records
	 *
	 * @throws IllegalArgumentException if not enough arguments are found in one
	 *                                  line, or grade is negative, line cannot be
	 *                                  parsed properly
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		Objects.requireNonNull(lines, "List of lines shouldn't be null reference.");
		List<StudentRecord> listOfStudentRecords = new ArrayList<StudentRecord>();
		int linesCounter = 1;
		for (var line : lines) {
			if (line.isBlank())
				continue;
			String[] splitted = line.split("\t++");
			if (splitted.length != 7)
				throw new IllegalArgumentException(
						"Not enough arguments to create Student Record at line " + linesCounter);
			try {
				listOfStudentRecords.add(new StudentRecord(splitted[0], splitted[1], splitted[2],
						Double.parseDouble(splitted[3]), Double.parseDouble(splitted[4]),
						Double.parseDouble(splitted[5]), Integer.parseInt(splitted[6])));
			} catch (NumberFormatException e) {
				throw new NumberFormatException("Error while intepreting line " + linesCounter
						+ "\nExpected number but thing cannot be parsed");
			} catch (NullPointerException | IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"Error while intepreting line " + linesCounter + "/n" + e.getMessage());
			}
			linesCounter++;
		}
		return listOfStudentRecords;
	}

	/**
	 * Method that returns number of Student records that satisfy next condition:
	 * Student has scored more than 25 points.
	 * 
	 * @param records list of all students in database
	 * @return number of records that satisfy:Student has scored more than 25
	 *         points.
	 * @throws NullPointerException when records referenced is null reference
	 */
	public static long vratiBodovaViseOd25(List<StudentRecord> records) {
		Objects.requireNonNull(records, "List provided is null reference.");
		return records.stream().filter(t -> getTotalPoints(t) > 25).count();
	}

	/**
	 * Method that calculates sum of scores of provided student
	 * 
	 * @param t student whose total score is calculated
	 * @return double representing sum of scores
	 * @throws NullPointerException if provided student is null
	 */
	private static double getTotalPoints(StudentRecord t) {
		Objects.requireNonNull(t);
		return t.getMidTermExamScore() + t.getFinalExamScore() + t.getLabScore();
	}

	/**
	 * Method that returns number of Student records that satisfy next condition:
	 * Student has grade "5"
	 * 
	 * @param records list of all students in database
	 * @return number of records that satisfy: Student has grade "5"
	 * @throws NullPointerException when records referenced is null reference
	 */
	public static long vratiBrojOdlikasa(List<StudentRecord> records) {
		Objects.requireNonNull(records, "List provided is null reference.");
		return records.stream().filter(t -> t.getGrade() == 5).count();
	}

	/**
	 * Method that returns list of Student records that satisfy next condition:
	 * Student has grade "5"
	 * 
	 * @param records list of all students in database
	 * @return list of records that satisfy: Student has grade "5"
	 * @throws NullPointerException when records referenced is null reference
	 */
	public static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		Objects.requireNonNull(records, "List provided is null reference.");
		return records.stream().filter(t -> t.getGrade() == 5).collect(Collectors.toList());
	}

	/**
	 * Method that returns list of Student records that satisfy next condition:
	 * Student has grade "5" and list is sorted by total score number
	 * 
	 * @param records list of all students in database
	 * @return sorted list of records that satisfy: Student has grade "5"
	 * @throws NullPointerException when records referenced is null reference
	 */
	public static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		Objects.requireNonNull(records, "List provided is null reference.");
		return records.stream().filter(t -> t.getGrade() == 5)
				.sorted((t1, t2) -> Double.compare(getTotalPoints(t2), getTotalPoints(t1)))
				.collect(Collectors.toList());
	}

	/**
	 * Method that returns list of Students JMBAGs that satisfy next condition:
	 * Student didn't get positive grade (>2)
	 * 
	 * @param records list of all students in database
	 * @return list of Students JMBAGs that satisfy: Student didn't get positive
	 *         grade (>2)
	 * @throws NullPointerException when records referenced is null reference
	 */
	public static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		Objects.requireNonNull(records, "List provided is null reference.");
		return records.stream().filter(t -> t.getGrade() == 1).map(t -> t.getJmbag()).sorted()
				.collect(Collectors.toList());
	}

	/**
	 * Method that returns Map of Student records and their grades. Each grade is
	 * key, and value is list of students that scored that grade
	 * 
	 * @param records list of all students in database
	 * @return Map of Student records and their grades. Each grade is key, and value
	 *         is list of students that scored that grade
	 * @throws NullPointerException when records referenced is null reference
	 */
	public static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		Objects.requireNonNull(records, "List provided is null reference.");
		return records.stream().collect(Collectors.groupingBy(t -> t.getGrade()));
	}

	/**
	 * Method that returns map that count occurrences of each grade. Key is grade,
	 * value is number of that grade in list of students.
	 * 
	 * @param records list of all students in database
	 * @return map that count occurrences of each grade.
	 * @throws NullPointerException when records referenced is null reference
	 */
	public static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
		Objects.requireNonNull(records, "List provided is null reference.");
		return records.stream().collect(Collectors.toMap(t -> t.getGrade(), stud -> 1, (value, stud) -> value + 1));
	}

	/**
	 * Method that returns map with 2 keys:true and false. List stored with key true
	 * represents list of all students that passed.
	 * 
	 * @param records list of all students in database
	 * @return map with 2 keys:true and false. List stored with key true represents
	 *         list of all students that passed.
	 * @throws NullPointerException when records referenced is null reference
	 */
	public static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
		Objects.requireNonNull(records, "List provided is null reference.");
		return records.stream().collect(Collectors.partitioningBy(t -> t.getGrade() > 1));
	}

}
