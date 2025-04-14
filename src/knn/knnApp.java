package knn;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import util.Observation;

public class knnApp {
	public static void main(String[] args) throws IOException {
		List<Observation> test = Observation.getDataFromCSV(Paths.get("src/data/iris/iris_test.txt"));
		List<Observation> training = Observation.getDataFromCSV(Paths.get("src/data/iris/iris_training.txt"));

		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Choose an option:");
			System.out.println("1. Classify test data and calculate accuracy");
			System.out.println("2. Manually classify a new observation");
			System.out.println("3. Exit");
			int choice = scanner.nextInt();

			if (choice == 1) {
				classifyTestData(test, training, scanner);
			} else if (choice == 2) {
				userObservations(training, scanner);
			} else if (choice == 3) {
				System.out.println("Exiting...");
				break;
			} else {
				System.out.println("Invalid choice. Try again.");
			}
		}

		scanner.close();
	}

	public static void classifyTestData(List<Observation> testData, List<Observation> trainingData, Scanner scanner) {
		int k = getK(trainingData, scanner);
		int correctClassifications = 0;

		for (Observation testObservation : testData) {
			List<Observation> nearestNeighbours = getNearestNeighbours(testObservation, trainingData, k);
			String predictedLabel = getMostFrequentLabel(nearestNeighbours);

			if (predictedLabel.equals(testObservation.getLabel())) {
				correctClassifications++;
			}
		}

		double accuracy = (double) correctClassifications / testData.size() * 100;
		System.out.println("Correct classifications: " + correctClassifications);
		System.out.println("Accuracy: " + accuracy + "%");
	}

	public static String getMostFrequentLabel(List<Observation> neighbours) {
		HashMap<String, Integer> labelCounts = new HashMap<>();

		for (Observation neighbour : neighbours) {
			labelCounts.put(
					neighbour.getLabel(),
					labelCounts.getOrDefault(neighbour.getLabel(), 0) + 1);
		}

		int maxOccureces = 0;
		String toReturn = "";

		for (Map.Entry<String, Integer> pair : labelCounts.entrySet()) {
			if (maxOccureces < pair.getValue()) {
				maxOccureces = pair.getValue();
				toReturn = pair.getKey();
			}
		}

		return toReturn;
	}

	public static void userObservations(List<Observation> trainingData, Scanner scanner) {
		int k = getK(trainingData, scanner);

		scanner.nextLine();

		while (true) {
			System.out.println("Enter a new observation with " + trainingData.get(0).getNumberOfAttributes()
					+ " attributes (comma-separated), or type 'quit' to exit:");
			String userLine = scanner.nextLine();

			if (userLine.equalsIgnoreCase("quit")) {
				break;
			}

			try {
				String[] elements = userLine.split(",");
				double[] attrib = new double[elements.length - 1];
				for (int i = 0; i < attrib.length; i++) {
					attrib[i] = Double.parseDouble(elements[i]);
				}
				Observation userObservation = new Observation(attrib, elements[elements.length - 1]);
				List<Observation> nearestNeighbours = getNearestNeighbours(userObservation, trainingData, k);
				String predictedLabel = getMostFrequentLabel(nearestNeighbours);

				System.out.println("Predicted label: " + predictedLabel);
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid input. Try again.");
			}
		}
	}

	private static int getK(List<Observation> data, Scanner scanner) {
		int k = 0;
		System.out.println("Enter k: ");
		k = scanner.nextInt();

		if (k < 1 || k > data.size()) {
			System.out.println("Invalid k, setting to 3");
			k = 3;
		}
		return k;
	}

	private static List<Observation> getNearestNeighbours(Observation testedObservation, List<Observation> trainingData,
			int k) {
		List<ObservationDistance> distances = new ArrayList<>();

		for (Observation observation : trainingData) {
			double distance = manhattanDistance(testedObservation, observation);
			distances.add(new ObservationDistance(observation, distance));
		}

		distances.sort((a, b) -> Double.compare(a.distance, b.distance));

		List<Observation> nearestNeighbours = new ArrayList<>();
		for (int i = 0; i < k; i++) {
			nearestNeighbours.add(distances.get(i).observation);
		}

		return nearestNeighbours;
	}

	private static class ObservationDistance {
		Observation observation;
		double distance;

		ObservationDistance(Observation observation, double distance) {
			this.observation = observation;
			this.distance = distance;
		}
	}

	private static double euclideanDistance(Observation userObservation, Observation trainingData) {
		double[] temp = new double[userObservation.getNumberOfAttributes()];
		for (int i = 0; i < userObservation.getNumberOfAttributes(); i++)
			temp[i] = Math.pow(userObservation.getAttributes()[i] - trainingData.getAttributes()[i], 2);

		double distance = 0;
		for (int i = 0; i < temp.length; i++)
			distance += temp[i];

		return Math.sqrt(distance);
	}

	private static double manhattanDistance(Observation userObservation, Observation trainingData) {
		double[] temp = new double[userObservation.getNumberOfAttributes()];
		for (int i = 0; i < userObservation.getNumberOfAttributes(); i++)
			temp[i] = Math.abs(userObservation.getAttributes()[i] - trainingData.getAttributes()[i]);

		double distance = 0;
		for (int i = 0; i < temp.length; i++)
			distance += temp[i];

		return distance;
	}
}