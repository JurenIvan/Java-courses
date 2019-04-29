package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * class that models database or collection of StudentRecords. Has a bundle of
 * method useful for retrieving studentRecords out of it.
 * 
 * @author juren
 *
 */
public class StudentDatabase {
	/**
	 * variable used to store all of the {@link StudentRecord} in database in list
	 * so we can iterate through them
	 */
	private List<StudentRecord> records;
	/**
	 * variable used to map jmbag to record so we can retrieve {@link StudentRecord}
	 * in O(1) time
	 */
	private Map<String, StudentRecord> jmbagToRecordMap;

	/**
	 * Constructor for {@link StudentDatabase}. As argument gets String, which it
	 * parses and stores records as List of {@link StudentRecord}.
	 *
	 * @param data list of string lines representing a {@link StudentRecord} each
	 *
	 * @throws NullPointerException     if data is null reference
	 * @throws IllegalArgumentException if grade is out of range
	 * @throws NumberFormatException    if number cant be parsed
	 */
	public StudentDatabase(List<String> data) {
		Objects.requireNonNull(data, "Provided input string is null reference");
		records = new ArrayList<>(data.size());
		jmbagToRecordMap = new HashMap<>(data.size() * 2);

		String splitted[];
		for (var line : data) {
			splitted = line.split("\\s+");
			if (splitted.length < 4)
				throw new IllegalArgumentException("Line doesn't contain enough data to make student record.");

			if (jmbagToRecordMap.containsKey(splitted[0]))
				throw new IllegalArgumentException("Mutiple people with same jmbag tried to be added");

			int grade;
			try {
				grade = Integer.parseInt(splitted[splitted.length - 1]);
			} catch (NumberFormatException e) {
				throw new NumberFormatException("Grade must be parseable to integer");
			}

			if (grade < 1 || grade > 5)
				throw new IllegalArgumentException("Grade must be integer between 1 and 5");

			StringBuilder surname = new StringBuilder();
			for (int i = 1; i < splitted.length - 2; i++) {
				surname.append(splitted[i]);
				surname.append(" ");
			}

			StudentRecord sr = new StudentRecord(splitted[0], surname.toString().trim(), splitted[splitted.length - 2],
					grade);

			records.add(sr);
			jmbagToRecordMap.put(splitted[0], sr);
		}
	}

	/**
	 * Method that returns StudentRecord for provided jmbag in O(1) complexity
	 * 
	 * @param jmbag key to find {@link StudentRecord}
	 * @return {@link StudentRecord} with given key, or null if no such entry is
	 *         found
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return jmbagToRecordMap.get(jmbag);
	}

	/**
	 * Method that filters all records and returns ones that satisfy filter.
	 * 
	 * @param filter condition that filters records
	 * @return list of records that passed the filter
	 * @throws NullPointerException if given filter is null reference
	 */
	public List<StudentRecord> filter(IFilter filter) {
		Objects.requireNonNull("Provided filter must not be null reference!");
		List<StudentRecord> acceptableList = new ArrayList<StudentRecord>();
		for (var entry : records) {
			if (filter.accepts(entry)) {
				acceptableList.add(entry);
			}
		}
		return acceptableList;
	}

}
