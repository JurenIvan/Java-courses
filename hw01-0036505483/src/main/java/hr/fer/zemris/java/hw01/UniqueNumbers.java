package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Class that contains main method which reads users input through keyboard,
 * accepts only whole numbers that has not been previously added into structure
 * and after user inputs "kraj" it prints unique values in ascending and
 * descending order. In case of invalid input, gives user a message feedback.
 * 
 * Args are not used for input.
 * 
 * @author juren
 *
 */
public class UniqueNumbers {

	/**
	 * String sequence used to determine end of user input.
	 */
	private static final String END_TOKEN = "kraj";

	/**
	 * Static class used to represent node in tree structure. Contains two
	 * references to its 'children' (TreeNode) and one integer which stores the
	 * value of the node.
	 * 
	 * @author juren
	 *
	 */
	public static class TreeNode {		
		TreeNode left, right;
		int value;
	}

	/**
	 * Method that starts the program.
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		TreeNode root = null;

		String inputToken;
		System.out.print("Unesite broj > ");
		while (sc.hasNext()) {
			inputToken = sc.next();

			try {
				int value = Integer.parseInt(inputToken);
				if (containsValue(root, value)) {
					System.out.println("Broj već postoji. Preskačem");
					continue;
				}
				root = addNode(root, value);
				System.out.println("Dodano");

			} catch (NumberFormatException e) {
				if (inputToken.equals(END_TOKEN)) {

					System.out.print("Ispis od najmanjeg: ");
					printTree(root, true);
					System.out.print("\nIspis od najvećeg: ");
					printTree(root, false);

					sc.close();
					return;
				}
				System.out.println("'" + inputToken + "' nije cijeli broj.");
			}
			System.out.print("Unesite broj > ");
		}

		sc.close();
		return;
	}

	/**
	 * Adds a new node to a structure, whose root is given as a parameter,
	 * recursively. Structure is binary tree where value of left child is smaller
	 * than value of parent.
	 * 
	 * @param root  refers to a root of structure in which a new value(node) is
	 *              added.
	 * @param value refers to value which will be added into structure.
	 * @return returns root TreeNode reference
	 */
	public static TreeNode addNode(TreeNode root, int value) {
		if (root == null) {
			root = new TreeNode();
			root.value = value;
		} else {
			if (root.value > value) {
				root.left = addNode(root.left, value);
			} else if (root.value < value) {
				root.right = addNode(root.right, value);
			}
		}
		return root;
	}

	/**
	 * Counts number of nodes in a given subtree by visiting each node. O(n)
	 * complexity.
	 * 
	 * @param root reference to a subtree whose nodes we want to count
	 * @return returns a number of Nodes in tree structure(integer)
	 */
	public static int treeSize(TreeNode root) {
		if (root == null) {
			return 0;
		}
		return treeSize(root.left) + treeSize(root.right) + 1;
	}

	/**
	 * Checks whether or not a given value is present in structure.
	 * 
	 * @param root  reference to a subtree where we want to check for value
	 * @param value integer whose presence method checks
	 * @return value found->(boolean) true, value not found->false
	 */
	public static boolean containsValue(TreeNode root, int value) {
		if (root == null) {
			return false;
		}

		if (root.value == value) {
			return true;
		} else if (root.value > value) {
			return containsValue(root.left, value);
		} else {
			return containsValue(root.right, value);
		}

	}

	/**
	 * Method used for printing sorted array of values stored in structure. Can be
	 * used to print values in either ascending or descending order (depending on
	 * isAscending parameter).
	 * 
	 * @param root        reference to subtree which we want to print
	 * @param isAscending boolean which if it's true prints ascending otherwise
	 *                    descending order of values stored in structure
	 */
	public static void printTree(TreeNode root, boolean isAscending) {
		if (root == null) {
			return;
		}
		if (isAscending) {
			printTree(root.left, isAscending);
			System.out.print(root.value + " ");
			printTree(root.right, isAscending);
		} else {
			printTree(root.right, isAscending);
			System.out.print(root.value + " ");
			printTree(root.left, isAscending);
		}
	}

	/**
	 * Method used for printing ascending sorted array of values stored in
	 * structure.
	 * 
	 * @param root reference to subtree which we want to print
	 */
	public static void printTree(TreeNode root) {
		printTree(root, true);
	}

}
