package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;

class LSystemBuilderImplTest {
	
	@Test
	void testGenerate1() {
		LSystemBuilderImpl tester=new LSystemBuilderImpl();
		LSystem t=tester.registerProduction('F', "F+F--F+F").setAxiom("F").build();
		assertEquals("F",t.generate(0));
		assertEquals("F+F--F+F",t.generate(1));
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F",t.generate(2));
	}
	
	@Test
	void testGenerate2() {
		LSystemBuilderImpl tester=new LSystemBuilderImpl();
		LSystem t=tester.registerProduction('F', "F+F").setAxiom("F").build();
		assertEquals("F",t.generate(0));
		assertEquals("F+F+F+F",t.generate(2));
		assertEquals("F+F",t.generate(1));
	}
	
	@Test
	void testGenerate3() {
		LSystemBuilderImpl tester=new LSystemBuilderImpl();
		LSystem t=tester.registerProduction('F', "F+F--F+F").registerProduction('A', "A-A").setAxiom("FA").build();
		assertEquals("FA",t.generate(0));
		assertEquals("F+F--F+FA-A",t.generate(1));
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+FA-A-A-A",t.generate(2));
	}
	
}
