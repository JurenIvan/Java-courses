package zi.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Calc {
	
	public List<String> expand;
	public List<String> labels;

	public String calcOut(List<String> lines) {
		labels = new ArrayList<String>();
		List<String> expr = new ArrayList<String>();
		expand = new ArrayList<String>();
		List<List<Double>> inputValues = new ArrayList<List<Double>>();

		splitIntoProper(lines, labels, expr, expand, inputValues);

		StringBuilder sb = new StringBuilder();
		inputValues.forEach(iv -> sb.append(calculateForInput(labels, expr, expand, iv) + "\n"));
		return sb.toString();
	}

	private String calculateForInput(List<String> labels, List<String> expr, List<String> expand, List<Double> iv) {
		Map<String, Double> values = new HashMap<String, Double>();

		for (int i = 0; i < labels.size(); i++)
			values.put(labels.get(i), iv.get(i));

		List<String> expr2 = new ArrayList<>(expr);

		while (expr2.size() > 0) {
			for (int i = 0; i < expr2.size(); i++) {
				String[] equasion = expr2.get(i).split("=");
				String[] rightVars = equasion[1].split("/|\\+|-|\\*");

				String r = equasion[0].trim().substring(1);
				String a = rightVars[0].trim().substring(1);
				String b = rightVars[1].trim().substring(1);

				if (values.containsKey(a) && values.containsKey(b)) {
					if (equasion[1].contains("+"))
						values.put(r, values.get(a) + values.get(b));
					if (equasion[1].contains("-"))
						values.put(r, values.get(a) - values.get(b));
					if (equasion[1].contains("*"))
						values.put(r, values.get(a) * values.get(b));
					if (equasion[1].contains("/"))
						values.put(r, values.get(a) / values.get(b));

					expr2.remove(i);
					break;
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		labels.forEach(e -> sb.append(values.get(e) + ";"));
		expand.forEach(e -> sb.append(values.get(e) + ";"));
		return sb.toString();
	}

	public void splitIntoProper(List<String> lines, List<String> labels, List<String> expr, List<String> expand,
			List<List<Double>> inputValues) {

		lines.forEach(line -> {
			if (line.isBlank())
				return;
			if (line.startsWith("#EXPAND")) {
				line = line.substring(8);
				String splitted[] = line.split(";");
				for (int i = 0; i < splitted.length; i++)
					expand.add(splitted[i].trim());

			} else if (line.startsWith("#EXPR")) {
				line = line.substring(5);
				expr.add(line.trim());
			} else if (line.startsWith("#LABELS")) {
				line = line.substring(7);
				String splitted[] = line.split(";");
				for (int i = 0; i < splitted.length; i++)
					labels.add(splitted[i].trim());
			} else {
				List<String> ls = Arrays.asList(line.split(";"));
				inputValues.add(ls.stream().map(l -> Double.parseDouble(l)).collect(Collectors.toList()));
			}
		});
	}
}
