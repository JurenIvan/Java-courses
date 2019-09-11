package zi2.prvi;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Calc extends SimpleFileVisitor<Path> {

	public TreeMap<String, HashMap<Integer, Integer>> values;

	public Calc() {
		values = new TreeMap<String, HashMap<Integer, Integer>>();
	}

	public static void main(String[] args) throws IOException {
		String inPath = "C:\\Users\\juren\\OneDrive - fer.hr\\Dokumenti\\data\\zad1";
		String outPath = "C:\\Users\\juren\\OneDrive - fer.hr\\Dokumenti\\data\\zad1\\outPath.txt";

		Calc visitor = new Calc();
		Files.walkFileTree(Paths.get(inPath), visitor);
		System.out.println("done");

		FileWriter fw = new FileWriter(outPath);
		fw.write(visitor.formatData());
		fw.close();
		System.out.println(visitor.formatData());
		return;
	}
	
	public void DoCalculations(Path root) throws IOException {
		Files.walkFileTree(root, this);
	}

	private String formatData() {
		StringBuilder sb = new StringBuilder();

		for (var entry : values.entrySet()) {
			sb.append(entry.getKey().toString() + "\n");
			sb.append(printValues(entry.getValue()));
		}

		return sb.toString();
	}

	public static String printValues(HashMap<Integer, Integer> map) {
		StringBuilder sb = new StringBuilder();

		for (int i = 1; i < 13; i++) {
			sb.append("\t" + i + ":\t" + map.get(i) + "\n");
		}

		return sb.toString();
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		System.out.println("working");
		if (!file.toAbsolutePath().toString().endsWith(".log"))
			return FileVisitResult.CONTINUE;

		List<String> lines = Files.readAllLines(file);
		for (int i = 0; i < lines.size(); i++) {
			String[] splitted = lines.get(i).split("\\t+");

			int month = Integer.parseInt(splitted[0].trim());
			int quan = Integer.parseInt(splitted[2].trim());
			String name = splitted[1].trim();

			if (!values.containsKey(name)) {
				values.put(name, new HashMap<Integer, Integer>());
				continue;
			}

			var map = values.get(name);
			if (!map.containsKey(month)) {
				map.put(month, quan);
			} else {
				map.put(month, map.get(month) + quan);
			}

		}
		return FileVisitResult.CONTINUE;
	}
}
