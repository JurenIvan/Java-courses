package hr.fer.zemris.java.hw07.demo4;

import java.util.Objects;

/**
 * Class that represent a model of student record.
 * 
 * @author juren
 *
 */
public class StudentRecord {
	/**
	 * Variable that stores jmbag of student.
	 */
	private String jmbag;

	/**
	 * Variable that stores surname of student.
	 */
	private String surname;

	/**
	 * Variable that stores first name of student.
	 */
	private String firstName;

	/**
	 * Variable that stores midTerm exam score of student.
	 */
	private double midTermExamScore;

	/**
	 * Variable that stores final exam score of student.
	 */
	private double finalExamScore;

	/**
	 * Variable that stores lab score of student.
	 */
	private double labScore;

	/**
	 * Variable that stores grade of student.
	 */
	private int grade;

	/**
	 * Constructor that creates new Student Record
	 * 
	 * @param jmbag            jmbag of student, must not be null
	 * @param surname           surame of student,must not be null
	 * @param firstName        firstName of student,must not be null
	 * @param midTermExamScore midTermExamScore of student
	 * @param finalExamScore   finalExamScore of student
	 * @param labScore         labScore of student
	 * @param grade            grade of student
	 * 
	 * @throws NullPointerException    if jmbag, name or surname are null
	 * @throws IllegalArgumentException if grade is negative
	 */
	public StudentRecord(String jmbag, String surname, String firstName, double midTermExamScore, double finalExamScore,
			double labScore, int grade) {
		this.jmbag = Objects.requireNonNull(jmbag, "Tried to create StudentRecord with null jmbag");
		this.surname = Objects.requireNonNull(surname, "Tried to create StudentRecord with null surname");
		this.firstName = Objects.requireNonNull(firstName, "Tried to create StudentRecord with null firstname");
		this.midTermExamScore = midTermExamScore;
		this.finalExamScore = finalExamScore;
		this.labScore = labScore;
		
		if (grade < 0)
			throw new IllegalArgumentException("Negative grades does not exits");
		this.grade = grade;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Standard setter.
	 * 
	 * @param jmbag the jmbag to set
	 */
	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Standard setter.
	 * 
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Standard setter.
	 * 
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the midTermExamScore
	 */
	public double getMidTermExamScore() {
		return midTermExamScore;
	}

	/**
	 * Standard setter.
	 * 
	 * @param midTermExamScore the midTermExamScore to set
	 */
	public void setMidTermExamScore(double midTermExamScore) {
		this.midTermExamScore = midTermExamScore;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the finalExamScore
	 */
	public double getFinalExamScore() {
		return finalExamScore;
	}

	/**
	 * Standard setter.
	 * 
	 * @param finalExamScore the finalExamScore to set
	 */
	public void setFinalExamScore(double finalExamScore) {
		this.finalExamScore = finalExamScore;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the labScore
	 */
	public double getLabScore() {
		return labScore;
	}

	/**
	 * Standard setter.
	 * 
	 * @param labScore the labScore to set
	 */
	public void setLabScore(double labScore) {
		this.labScore = labScore;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the grade
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * Standard setter.
	 * 
	 * @param grade the grade to set
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return jmbag + '\t' + surname + '\t' + firstName + '\t' + midTermExamScore + '\t' + finalExamScore + '\t'
				+ labScore + '\t' + grade;
	}

}
