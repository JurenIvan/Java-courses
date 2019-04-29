package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class Vector2DTest {
	
	@Test
	void ConstructorAndGettersTest() {
		Vector2D v2d=new Vector2D(1, 23.4);
		
		assertTrue(v2d.getX()==1);
		assertTrue(v2d.getY()==23.4);
	}

	@Test
	void translate (){
		Vector2D v2d=new Vector2D(1, 23.4);
		Vector2D translator=new Vector2D(10, 100);
		
		v2d.translate(translator);
		assertTrue(v2d.getX()==11);
		assertTrue(v2d.getY()==123.4);
	}
	
	@Test
	void translateWITHNULL (){
		Vector2D v2d=new Vector2D(1, 23.4);
		assertThrows(NullPointerException.class,()->v2d.translate(null));
	}
	
	@Test
	void translated (){
		Vector2D v2d=new Vector2D(1, 23.4);
		Vector2D v2dTranslated=v2d.translated(new Vector2D(10, 100));
		
		assertTrue(v2d.getX()+10==v2dTranslated.getX());
		assertTrue(v2d.getY()+100==v2dTranslated.getY());
		assertTrue(v2d.getX()==1);
		assertTrue(v2d.getY()==23.4);
	}
	
	@Test
	void translatedNULL (){
		Vector2D v2d=new Vector2D(1, 23.4);
		assertThrows(NullPointerException.class,()->v2d.translate(null));
	}
	
	@Test
	void rotateTest(){
		Vector2D v2d=new Vector2D(1, 23.4);
		
		v2d.rotate(Math.PI/3);
		assertTrue(Math.abs(v2d.getX()+19.764994)<0.0001);
		assertTrue(Math.abs(v2d.getY()-12.566025)<0.0001);
	}
	
	@Test
	void rotatedTest(){
		Vector2D v2d=new Vector2D(1, 23.4);
		Vector2D v2dRotated=v2d.rotated(Math.PI/3);
		
		assertTrue(Math.abs(v2dRotated.getX()+19.764994)<0.0001);
		assertTrue(Math.abs(v2dRotated.getY()-12.566025)<0.0001);
		assertTrue(v2d.getX()==1);
		assertTrue(v2d.getY()==23.4);
	}
	
	@Test
	void scaleTest(){
		Vector2D v2d=new Vector2D(1, 23.4);
		
		v2d.scale(2);
		assertTrue(v2d.getX()==2);
		assertTrue(v2d.getY()==46.8);
		
	}
	
	@Test
	void scaled(){
		Vector2D v2d=new Vector2D(1, 23.4);
		Vector2D v2dScaled=v2d.scaled(2);
		
		assertTrue(2==v2dScaled.getX());
		assertTrue(46.8==v2dScaled.getY());
		assertTrue(v2d.getX()==1);
		assertTrue(v2d.getY()==23.4);
	}
	
}
