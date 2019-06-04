package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

public class Test {

	public static void main(String[] args) throws IOException {

		String fileName = "C:\\Faks\\G2\\S4\\Java\\workspaces\\MojeZadace\\hw13-0036505483\\src\\main\\webapp\\WEB-INF\\glasanje-rezultati.txt";
		if (!Files.exists(Paths.get(fileName))) {
			Files.createFile(Paths.get(fileName));
		}
		if (!Files.exists(Paths.get(fileName))) {
			Files.createFile(Paths.get(fileName));
		}
		List<String> listOfIDVotes = Files.readAllLines(Paths.get(fileName));
		List<String[]> iDVotes = listOfIDVotes.stream().map(t -> t.split("\\t")).collect(Collectors.toList());
		List<Record> records = readDefinition(null);
		updateRecords(records, iDVotes);

		records.sort((f, s) -> Integer.compare(s.getVotes(), f.getVotes()));
//		List<Record> win = records.stream().filter(t -> t.getVotes() == records.get(0).getVotes())
//				.collect(Collectors.toList());

		return;
	}

	private static List<Record> readDefinition(HttpServletRequest req) throws IOException {
		String fileName = "C:\\Faks\\G2\\S4\\Java\\workspaces\\MojeZadace\\hw13-0036505483\\src\\main\\webapp\\WEB-INF\\glasanje-definicija.txt";
		if (!Files.exists(Paths.get(fileName))) {
			Files.createFile(Paths.get(fileName));
		}
		return Files.readAllLines(Paths.get(fileName)).stream().map((t) -> Record.makeRecord(t))
				.collect(Collectors.toList());
	}

	private static void updateRecords(List<Record> records, List<String[]> iDVotes) {
		for (String[] e : iDVotes) {
			int index;
			if ((index = findIndexOf(e[0], records)) != -1) {
				records.get(index).setVotes(Integer.parseInt(e[1]));
			}
		}
	}

	private static int findIndexOf(String string, List<Record> records) {
		for (int i = 0; i < records.size(); i++) {
			if (string.equals(records.get(i).getId()))
				return i;
		}
		return -1;
	}

}
