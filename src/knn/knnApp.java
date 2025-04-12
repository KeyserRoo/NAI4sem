package knn;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import util.Observation;

public class knnApp {
    public static void main(String[] args) throws IOException {
        Observation[] test = Observation.getData(Paths.get("../data/iris/iris_test.txt"));
        Observation[] training = Observation.getData(Paths.get("../data/iris/iris_training.txt"));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Classify test data and calculate accuracy");
            System.out.println("2. Manually classify a new observation");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();

            if (choice == 1) {
                classifyTestData(test, training);
            } else if (choice == 2) {
                userObservations(training);
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    public static void classifyTestData(Observation[] testData, Observation[] trainingData) {
        int k = getK(trainingData);
        int correctClassifications = 0;

        for (Observation testObservation : testData) {
            Observation[] nearestNeighbours = getNearestNeighbours(testObservation, trainingData, k);
            String predictedLabel = getMostFrequentLabel(nearestNeighbours);

            if (predictedLabel.equals(testObservation.getLabel())) {
                correctClassifications++;
            }
        }

        double accuracy = (double) correctClassifications / testData.length * 100;
        System.out.println("Correct classifications: " + correctClassifications);
        System.out.println("Accuracy: " + accuracy + "%");
    }

    public static String getMostFrequentLabel(Observation[] neighbours) {
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

    public static void userObservations(Observation[] trainingData) {
        int k = getK(trainingData);

        try (Scanner scanner = new Scanner(System.in)) {
            scanner.nextLine();

            while (true) {
                System.out.println("Enter a new observation with " + trainingData[0].getNumberOfAttributes()
                        + " attributes (comma-separated), or type 'quit' to exit:");
                String userLine = scanner.nextLine();

                if (userLine.equalsIgnoreCase("quit")) {
                    break;
                }

                try {
                    Observation userObservation = new Observation(userLine);
                    Observation[] nearestNeighbours = getNearestNeighbours(userObservation, trainingData, k);
                    String predictedLabel = getMostFrequentLabel(nearestNeighbours);

                    System.out.println("Predicted label: " + predictedLabel);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input. Try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static int getK(Observation[] data) {
        int k = 0;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter k: ");
            k = scanner.nextInt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (k < 1 || k > data.length) {
            System.out.println("Invalid k, setting to 3");
            k = 3;
        }
        return k;
    }

    private static Observation[] getNearestNeighbours(Observation testedObservation, Observation[] trainingData,
            int k) {
        ObservationDistance[] distances = new ObservationDistance[trainingData.length];

        for (int i = 0; i < trainingData.length; i++) {
            double distance = manhattanDistance(testedObservation, trainingData[i]);
            distances[i] = new ObservationDistance(trainingData[i], distance);
        }

        Arrays.sort(distances, (a, b) -> Double.compare(a.distance, b.distance));

        Observation[] nearestNeighbours = new Observation[k];
        for (int i = 0; i < k; i++) {
            nearestNeighbours[i] = distances[i].observation;
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