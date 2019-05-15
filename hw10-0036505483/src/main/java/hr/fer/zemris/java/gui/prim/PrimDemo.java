package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class PrimDemo extends JFrame  {

	private static final long serialVersionUID = 1L;

	public PrimDemo() {
		setLocation(20, 50);
		setSize(300, 200);
		setTitle("Primes lists");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
	}

	static class DemoListModel<T> implements ListModel<T> {

		private List<T> elementi = new ArrayList<>();
		private List<ListDataListener> promatraci = new ArrayList<>();

		@Override
		public void addListDataListener(ListDataListener l) {
			promatraci.add(l);
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			promatraci.remove(l);
		}

		@Override
		public int getSize() {
			return elementi.size();
		}

		@Override
		public T getElementAt(int index) {
			return elementi.get(index);
		}

		public void add(T element) {
			int pos = elementi.size();
			elementi.add(element);

			ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, pos, pos);
			for (ListDataListener l : promatraci) {
				l.intervalAdded(event);
			}
		}

	}

	private void initGUI() {
		iteratorImpl iter = new iteratorImpl();
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		DemoListModel<Integer> model = new DemoListModel<>();

		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);

		JPanel bottomPanel = new JPanel(new GridLayout(1, 0));

		JButton dodaj = new JButton("Dodaj");
		bottomPanel.add(dodaj);
		model.add(1);

		dodaj.addActionListener(e -> {
			model.add(iter.next());
		});

		JPanel central = new JPanel(new GridLayout(1, 0));
		central.add(new JScrollPane(list1));
		central.add(new JScrollPane(list2));

		cp.add(central, BorderLayout.CENTER);
		cp.add(bottomPanel, BorderLayout.PAGE_END);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			frame.pack();
			frame.setVisible(true);
		});
	}

	private static class iteratorImpl implements Iterator<Integer> {
		
		/**
		 * Variable that stores last calculated prime.
		 */
		private int lastPrime;

		/**
		 * Basic constructor that gets number of primes that will be returned.
		 * 
		 * @param howManyToReturn
		 */
		public iteratorImpl() {
			lastPrime = 1;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public Integer next() {
			if (!hasNext())
				throw new NoSuchElementException("No more primes available!");
			while (!isPrime(++lastPrime))
				;
			return lastPrime;
		}

		/**
		 * Method that analyzes whether a given number is prime or not. works in
		 * O(sqrt(n)/2) time.
		 * 
		 * @param number candidate for being a prime
		 * @return prime-ness of number
		 */
		private boolean isPrime(int number) {
			if (number % 2 == 0 && number > 2)
				return false;

			for (int i = 3; i <= Math.sqrt(number); i = i + 2) {
				if (number % i == 0)
					return false;
			}
			if (number == 1)
				return false;
			return true;
		}

	}

}
