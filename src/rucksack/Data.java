package rucksack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

	private int noElements;
	private int maxWeight;
	private Map<Integer, List<Element>> dataSetsEl = new HashMap<>();
	private Map<Integer, List<int[]>> dataSetsArr = new HashMap<>();

	public static Data loadData(String path) {
		Data data = new Data();
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			line = reader.readLine();
			String[] elementsAndWeight = line.replaceAll("[^0-9\\s]", "").trim().split("\\s+");
			data.noElements = Integer.parseInt(elementsAndWeight[0]);
			data.maxWeight = Integer.parseInt(elementsAndWeight[1]);

			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("dataset")) continue;

				int key = Integer.parseInt(line.replaceAll("[^0-9]", ""));

				int[] sizes = getLineData(reader, "sizes");
				int[] vals = getLineData(reader, "vals");

				List<Element> valueElements = new ArrayList<>();
				List<int[]> valueArrays = new ArrayList<>();

				for (int i = 0; i < data.noElements; i++) {
					valueElements.add(new Element(sizes[i], vals[i]));
				}
				valueArrays.add(sizes);
				valueArrays.add(vals);

				data.dataSetsEl.put(key, valueElements);
				data.dataSetsArr.put(key, valueArrays);

			}
		} catch (Exception ignored) {
		}
		return data;
	}

	public List<Element> getSetWithElements(int number) {
		return dataSetsEl.get(number);
	}

	public List<int[]> getSetWithArrays(int number) {
		return dataSetsArr.get(number);
	}

	public int getNoElements() {
		return noElements;
	}

	public int getMaxWeight() {
		return maxWeight;
	}

	private static int[] getLineData(BufferedReader reader, String name) throws IOException {
		String line = "";
		while (!(line = reader.readLine()).startsWith(name)) {
		}

		String[] table = line.replaceAll("[^0-9,]", "").split(",");
		int[] toReturn = new int[table.length];
		for (int i = 0; i < table.length; i++) {
			toReturn[i] = Integer.parseInt(table[i]);
		}

		return toReturn;
	}
}

class Element {
	private int size;
	private int value;

	Element(int size, int value) {
		this.size = size;
		this.value = value;
	}

	public int getSize() {
		return size;
	}

	public int getValue() {
		return value;
	}
}