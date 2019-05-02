package searching.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;

/**
 * Class containing main method that solves given puzzle. Program prints
 * solution (and steps required) on console
 * 
 * @author juren
 *
 */
public class SlagalicaDemo {
	/**
	 * Method used to start a program. Program prints solution (and steps required)
	 * on console
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {

		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(new int[] { 2, 3, 0, 1, 4, 6, 7, 5, 8 }));
		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfsv(slagalica, slagalica, slagalica);

		if (rješenje == null) {
			System.out.println("Nisam uspio pronaći rješenje.");
			return;
		}

		System.out.println("Imam rješenje. Broj poteza je: " + rješenje.getCost());
		List<KonfiguracijaSlagalice> lista = new ArrayList<>();
		Node<KonfiguracijaSlagalice> trenutni = rješenje;

		while (trenutni != null) {
			lista.add(trenutni.getState());
			trenutni = trenutni.getParent();
		}

		Collections.reverse(lista);

		lista.stream().forEach(k -> {
			System.out.println(k);
			System.out.println();
		});

	}

}
