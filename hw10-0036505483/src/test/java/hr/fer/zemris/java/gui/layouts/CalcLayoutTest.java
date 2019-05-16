package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

class CalcLayoutTest {

	@Test
	void allGoodTest() {
		CalcLayout cl = new CalcLayout();
		cl.addLayoutComponent(new JLabel(), "1,1");
	}

	@Test
	void negativeFirstParametherTest() {
		CalcLayout cl = new CalcLayout();
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "-1,1"));
	}

	@Test
	void negativeSecondParametherTest() {
		CalcLayout cl = new CalcLayout();
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "1,-1"));
	}

	@Test
	void negativeBothParametherTest() {
		CalcLayout cl = new CalcLayout();
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "-1,-1"));
	}

	@Test
	void IllegalPositionTest1_2() {
		CalcLayout cl = new CalcLayout();
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "1,2"));
	}

	@Test
	void IllegalPositionTest1_3() {
		CalcLayout cl = new CalcLayout();
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "1,3"));
	}

	@Test
	void IllegalPositionTest1_4() {
		CalcLayout cl = new CalcLayout();
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "1,4"));
	}

	@Test
	void IllegalPositionTest1_5() {
		CalcLayout cl = new CalcLayout();
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "1,5"));
	}

	@Test
	void legitPositionTest1_6() {
		CalcLayout cl = new CalcLayout();
		cl.addLayoutComponent(new JLabel(), "1,6");
	}

	@Test
	void OutOfBoundsTestFirstParam() {
		CalcLayout cl = new CalcLayout();
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "10,4"));
	}

	@Test
	void OutOfBoundsTestSecondParam() {
		CalcLayout cl = new CalcLayout();
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "1,50"));
	}

	@Test
	void OutOfBoundsTestBothParam() {
		CalcLayout cl = new CalcLayout();
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "10,50"));
	}

	@Test
	void doubleOnSamePositionTest1() {
		CalcLayout cl = new CalcLayout();
		cl.addLayoutComponent(new JLabel(), "2,5");
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "2,5"));
	}

	@Test
	void doubleOnSamePositionTest2() {
		CalcLayout cl = new CalcLayout();
		cl.addLayoutComponent(new JLabel(), "2,5");
		cl.addLayoutComponent(new JLabel(), "3,5");
		cl.addLayoutComponent(new JLabel(), "4,5");
		cl.addLayoutComponent(new JLabel(), "5,5");
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "3,5"));
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "4,5"));
		assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(new JLabel(), "5,5"));
	}

	@Test
	void dimensionsTest1() {

		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel("");
		l1.setPreferredSize(new Dimension(10, 30));
		JLabel l2 = new JLabel("");
		l2.setPreferredSize(new Dimension(20, 15));
		p.add(l1, new RCPosition(2, 2));
		p.add(l2, new RCPosition(3, 3));
		Dimension dim = p.getPreferredSize();

		assertTrue(152 == dim.width);
		assertTrue(158 == dim.height);

	}

	@Test
	void dimensionsTest2() {

		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel("");
		l1.setPreferredSize(new Dimension(108, 15));
		JLabel l2 = new JLabel("");
		l2.setPreferredSize(new Dimension(16, 30));
		p.add(l1, new RCPosition(1, 1));
		p.add(l2, new RCPosition(3, 3));
		Dimension dim = p.getPreferredSize();

		assertTrue(152 == dim.width);
		assertTrue(158 == dim.height);

	}

}
