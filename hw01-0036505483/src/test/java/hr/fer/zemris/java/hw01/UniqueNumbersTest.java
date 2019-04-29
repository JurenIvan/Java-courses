package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.hw01.UniqueNumbers.TreeNode;

class UniqueNumbersTest {

	@Test
	void treeSizeTestNull() {
		TreeNode root = null;
		assertEquals(0, UniqueNumbers.treeSize(root));
	}

	@Test
	void treeSizeTestNormalUseCase() {
		TreeNode root = null;
		root = UniqueNumbers.addNode(root, 12);
		root = UniqueNumbers.addNode(root, 2);
		root = UniqueNumbers.addNode(root, 12);

		assertEquals(2, UniqueNumbers.treeSize(root));

		root = UniqueNumbers.addNode(root, 24);
		root = UniqueNumbers.addNode(root, 16);
		root = UniqueNumbers.addNode(root, 4);
		root = UniqueNumbers.addNode(root, 6);

		assertEquals(6, UniqueNumbers.treeSize(root));

	}

	@Test
	void containsTestNull() {
		TreeNode root = null;
		assertFalse(UniqueNumbers.containsValue(root, 14));
	}

	@Test
	void containsTest() {
		TreeNode root = null;
		root = UniqueNumbers.addNode(root, 12);
		root = UniqueNumbers.addNode(root, 2);
		root = UniqueNumbers.addNode(root, 12);
		root = UniqueNumbers.addNode(root, 24);
		root = UniqueNumbers.addNode(root, 16);
		root = UniqueNumbers.addNode(root, 4);
		root = UniqueNumbers.addNode(root, 6);

		assertFalse(UniqueNumbers.containsValue(root, 36));
		assertTrue(UniqueNumbers.containsValue(root, 24));
	}

	@Test
	void masterTest() {
		TreeNode root = null;

		assertEquals(0, UniqueNumbers.treeSize(root));

		root = UniqueNumbers.addNode(root, 42);
		root = UniqueNumbers.addNode(root, 76);
		root = UniqueNumbers.addNode(root, 21);
		root = UniqueNumbers.addNode(root, 76);

		assertEquals(3, UniqueNumbers.treeSize(root));

		root = UniqueNumbers.addNode(root, 35);

		assertEquals(4, UniqueNumbers.treeSize(root));
		assertTrue(UniqueNumbers.containsValue(root, 42));
		assertFalse(UniqueNumbers.containsValue(root, 20));

		assertEquals(42, root.value);
		assertEquals(21, root.left.value);
		assertEquals(35, root.left.right.value);
		assertEquals(76, root.right.value);

		assertFalse(UniqueNumbers.containsValue(root, -2));
		assertFalse(UniqueNumbers.containsValue(root, 200));
		assertTrue(UniqueNumbers.containsValue(root, 21));
		assertTrue(UniqueNumbers.containsValue(root, 76));

	}

}
