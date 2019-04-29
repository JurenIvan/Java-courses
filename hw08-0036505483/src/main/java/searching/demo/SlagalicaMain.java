package searching.demo;

import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;
import searching.slagalica.gui.SlagalicaViewer;

public class SlagalicaMain {
	public static void main(String[] args) {
		int input[] = new int[9];
		if (args.length != 1) {
			System.out.println("Exaclty one array expected");
			return;
		}
		int sumdigits = 0;
		try {
			for (int i = 0; i < 9; i++) {
				if (!args[0].contains(i + "")) {
					System.out.println("Array should have every digit");
				}
				input[i] = Integer.parseInt(String.valueOf(args[0].charAt(i)));
				sumdigits = sumdigits + input[i];
			}
			if (sumdigits != 36)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			System.out.println("Array should have exactly all digits exaclty once");
			return;
		}

		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(input));
		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfsv(slagalica, slagalica, slagalica);

		if (rješenje == null) {
			System.out.println("Nisam uspio pronaći rješenje.");
			return;
		}

		SlagalicaViewer.display(rješenje);

	}
}
