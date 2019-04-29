package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class StudentDatabaseTest {

	@Test
	public void allTrueTest() throws IOException {
		List<String> input = Files.readAllLines(Paths.get(".//database.txt"), StandardCharsets.UTF_8);

		StudentDatabase sd = new StudentDatabase(input);
		assertTrue(sd.filter(t -> true).size() == 63);
	}

	@Test
	public void allFalseTest() throws IOException {
		List<String> input = Files.readAllLines(Paths.get(".//database.txt"), StandardCharsets.UTF_8);

		StudentDatabase sd = new StudentDatabase(input);
		assertTrue(sd.filter(t -> false).size() == 0);
	}

	@Test
	public void forJmbagTest1() throws IOException {
		List<String> input = Files.readAllLines(Paths.get(".//database.txt"), StandardCharsets.UTF_8);
		StudentDatabase sd = new StudentDatabase(input);

		assertEquals("Gagić", sd.forJMBAG("0000000013").getSurname());
	}

	@Test
	public void forJmbagTestNoSuchElement() throws IOException {
		List<String> input = Files.readAllLines(Paths.get(".//database.txt"), StandardCharsets.UTF_8);
		StudentDatabase sd = new StudentDatabase(input);

		assertEquals(null, sd.forJMBAG("00000000"));
	}

	@Test
	public void failWhileMakingDatabase()  {
		List<String> input = new ArrayList<String>();
		input.add("0000000009	Dean	Nataša	");

		assertThrows(IllegalArgumentException.class, ()->new StudentDatabase(input));
	}

}
