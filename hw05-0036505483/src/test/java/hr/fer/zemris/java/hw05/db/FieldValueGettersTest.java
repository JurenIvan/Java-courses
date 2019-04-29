package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FieldValueGettersTest {

	@Test
	public void fieldValueGettersTestFirstName() {
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);

		assertEquals("Jusufadis", FieldValueGetters.FIRST_NAME.get(record));
		StudentRecord recordNull = null;
		assertThrows(NullPointerException.class,
				() -> assertEquals("0000000005", FieldValueGetters.FIRST_NAME.get(recordNull)));
	}

	@Test
	public void fieldValueGettersTestLastName() {
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);

		assertEquals("Brezović", FieldValueGetters.LAST_NAME.get(record));
		StudentRecord recordNull = null;
		assertThrows(NullPointerException.class,
				() -> assertEquals("0000000005", FieldValueGetters.LAST_NAME.get(recordNull)));
	}

	@Test
	public void fieldValueGettersTestJmbag() {
		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		
		assertEquals("0000000005", FieldValueGetters.JMBAG.get(record));
		StudentRecord recordNull = null;
		assertThrows(NullPointerException.class,
				() -> assertEquals("0000000005", FieldValueGetters.JMBAG.get(recordNull)));
	}

}
