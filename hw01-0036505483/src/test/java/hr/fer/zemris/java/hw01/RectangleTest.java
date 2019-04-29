package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RectangleTest {

	@Test
	void testAreaException() {
		assertThrows(IllegalArgumentException.class, () -> Rectangle.calcRectangleArea(-2, 4.32));
		assertThrows(IllegalArgumentException.class, () -> Rectangle.calcRectangleArea(2, -4.32));
		assertThrows(IllegalArgumentException.class, () -> Rectangle.calcRectangleArea(-2, -4.32));
	}

	@Test
	void testArea() {
		assertTrue(Rectangle.calcRectangleArea(2, 5.123) - 10.246 < 1e-5);
		assertTrue(Rectangle.calcRectangleArea(5.123, 2) - 10.246 < 1e-5);
		assertTrue(Rectangle.calcRectangleArea(2, 5) - 10 < 1e-5);
	}

	@Test
	void testPerimeterException() {
		assertThrows(IllegalArgumentException.class, () -> Rectangle.calcRectanglePerimeter(-2, 4.32));
		assertThrows(IllegalArgumentException.class, () -> Rectangle.calcRectanglePerimeter(2, -4.32));
		assertThrows(IllegalArgumentException.class, () -> Rectangle.calcRectanglePerimeter(-2, -4.32));

	}

	@Test
	void testPerimeter() {
		assertTrue(Math.abs(Rectangle.calcRectanglePerimeter(2, 5.123) - 14.246) <= 1e-5);
		assertTrue(Math.abs(Rectangle.calcRectanglePerimeter(5.123, 2) - 14.246) <= 1e-5);
		assertTrue(Math.abs(Rectangle.calcRectanglePerimeter(2.1, 5) - 14.2) <= 1e-5);
	}

}
