package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class QueryFilterTest {

	@Test
	public void filterTestComplexQuerry() throws IOException {

		List<String> input = Files.readAllLines(Paths.get(".//database.txt"), StandardCharsets.UTF_8);
		StudentDatabase sd = new StudentDatabase(input);

		QueryParser parser = new QueryParser("firstName LIKE \"*\" and firstName > \"I\" and lastName <\"ZZZZZ\" and jmbag LIKE \"*\"  ");
		List<StudentRecord> allStudentRecords = new ArrayList<>(63);

		for (StudentRecord r : sd.filter(new QueryFilter(parser.getQuery()))) {
			allStudentRecords.add(r);
		}

		assertTrue(allStudentRecords.size() == 36);
	}
	
	@Test
	public void filterTestComplexQuerrySuggestionForLike() throws IOException {

		assertThrows(IllegalArgumentException.class, ()->  new QueryParser("firstName like \"*\" "));
	}
	

	@Test
	public void filterTest2() throws IOException {

		List<String> input = Files.readAllLines(Paths.get(".//database.txt"), StandardCharsets.UTF_8);
		StudentDatabase sd = new StudentDatabase(input);
		QueryParser parser = new QueryParser("jmbag = \"0000000003\"");
		List<StudentRecord> allStudentRecords = new ArrayList<>(63);

		for (StudentRecord r : sd.filter(new QueryFilter(parser.getQuery()))) {
			allStudentRecords.add(r);
		}

		assertTrue(allStudentRecords.size() == 1);
	}
	

	@Test
	public void filterTest3() throws IOException {

		List<String> input = Files.readAllLines(Paths.get(".//database.txt"), StandardCharsets.UTF_8);
		StudentDatabase sd = new StudentDatabase(input);
		QueryParser parser = new QueryParser("jmbag = \"0000000003\" and lastName LIKE \"B*\"");
		List<StudentRecord> allStudentRecords = new ArrayList<>(63);

		for (StudentRecord r : sd.filter(new QueryFilter(parser.getQuery()))) {
			allStudentRecords.add(r);
		}

		assertTrue(allStudentRecords.size() == 1);
	}
	

	@Test
	public void filterTest4() throws IOException {

		List<String> input = Files.readAllLines(Paths.get(".//database.txt"), StandardCharsets.UTF_8);
		StudentDatabase sd = new StudentDatabase(input);
		QueryParser parser = new QueryParser(" jmbag = \"0000000004\" and lastName LIKE \"H*\"");
		List<StudentRecord> allStudentRecords = new ArrayList<>(63);

		for (StudentRecord r : sd.filter(new QueryFilter(parser.getQuery()))) {
			allStudentRecords.add(r);
		}

		assertTrue(allStudentRecords.size() == 0);
	}
	

	@Test
	public void filterTest5() throws IOException {

		List<String> input = Files.readAllLines(Paths.get(".//database.txt"), StandardCharsets.UTF_8);
		StudentDatabase sd = new StudentDatabase(input);
		QueryParser parser = new QueryParser(" lastName LIKE \"B*\" ");
		List<StudentRecord> allStudentRecords = new ArrayList<>(63);

		for (StudentRecord r : sd.filter(new QueryFilter(parser.getQuery()))) {
			allStudentRecords.add(r);
		}

		assertTrue(allStudentRecords.size() == 4);
	}

}
