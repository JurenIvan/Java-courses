package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class that models basic StudentRecord.Contains usual
 * getters,constructor,equals, hashcode equals and toString
 * 
 * @author juren
 *
 */
public class StudentRecord {
	/**
	 * variable that stores jmbag of student
	 */
	private String jmbag;
	/**
	 * variable that stores students last name
	 */
	private String lastName;
	/**
	 * variable that stores students first name
	 */
	private String firstName;
	/**
	 * variable that stores students grade
	 */
	private int grade;

	/**
	 * usual getter for jmbag
	 * 
	 * @return String jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * usual getter for surname
	 * 
	 * @return String surnem
	 */
	public String getSurname() {
		return lastName;
	}

	/**
	 * usual getter for firstName
	 * 
	 * @return String firstName
	 */
	public String getName() {
		return firstName;
	}

	/**
	 * usual getter for grede
	 * 
	 * @return int grade
	 */
	public int getGrade() {
		return grade;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StudentRecord))
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}

	/**
	 * Standard constructor for {@link StudentRecord}. Grade must be in range 1-5.
	 * 
	 * @param jmbag   String representing jmbag
	 * @param surname string representing last name
	 * @param name    string representing first name
	 * @param grade   has to be int in range 1-5
	 * 
	 * @throws IllegalArgumentException if grade is not in appropriate range
	 * @throws NullPointerException     if any other arhument is null;
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int grade) {
		Objects.requireNonNull(jmbag, "no null strings allowed");
		Objects.requireNonNull(lastName, "no null strings allowed");
		Objects.requireNonNull(firstName, "no null strings allowed");

		if (grade < 1 || grade > 5)
			throw new IllegalArgumentException("grade cannot be smaller than 1 or greather than 5");
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "jmbag: " + jmbag + " ime: " + firstName + " prezime: " + lastName;
	}

}
