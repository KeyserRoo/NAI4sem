package util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Observation {
	private double[] attributes;
	private String label;

	public Observation(double[] attributes, String label) {
		this.attributes = attributes;
		this.label = label;
	}

	public static List<Observation> getDataFromCSV(Path path) throws IOException {
		List<String> lines = Files.readAllLines(path);
		List<Observation> observations = new ArrayList<>();

		for (String line : lines) {
			observations.add(processCSVLine(line));
		}

		return observations;
	}

	public static List<Observation> getDataFromFolder(Path startPath) throws IOException {
		List<Observation> observations = new ArrayList<>();

		Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String language = file.getParent().getFileName().toString();
				String content = new String(Files.readAllBytes(file));

				observations.add(new Observation(stringToPercentages(content), language));
				return FileVisitResult.CONTINUE;
			}
		});

		return observations;
	}

	@Override
	public String toString() {
		return "Observation [attributes=" + Arrays.toString(attributes) + ", label=" + label + "]";
	}

	public double[] getAttributes() {
		return attributes;
	}

	public int getNumberOfAttributes() {
		return attributes.length;
	}

	public String getLabel() {
		return label;
	}

	private static Observation processCSVLine(String line) {
		try {
			String[] elements = line.replace(",", ".").split("[\t]");

			int numOfAtt = elements.length - 1;
			double[] attrib = new double[numOfAtt];
			for (int i = 0; i < numOfAtt; i++) {
				attrib[i] = Float.parseFloat(elements[i]);
			}

			String lab = elements[elements.length - 1].trim();

			return new Observation(attrib, lab);
		} catch (Exception e) {
			throw new IllegalArgumentException("Zle parametry!");
		}
	}

	public static double[] stringToPercentages(String content) throws IOException {
		String normalized = Normalizer.normalize(content, Normalizer.Form.NFD);
		String noSpecialLetters = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		String noL = noSpecialLetters.replaceAll("ł", "l");
		String justLetters = noL.replaceAll("[^a-zA-Z]", "");
		String finished = justLetters.toLowerCase();

		int[] characters = new int[26];
		double[] percentages = new double[26];

		for (int i = 0; i < finished.length(); i++) {
			characters[finished.charAt(i) - 'a']++;
		}
		int sum = 0;
		for (int i = 0; i < characters.length; i++) {
			sum += characters[i];
		}
		for (int i = 0; i < percentages.length; i++) {
			String formatted = String.format("%.4f", ((double) characters[i] / sum));
			percentages[i] = Double.parseDouble(formatted.replaceAll(",", "."));
		}

		return percentages;
	}
}