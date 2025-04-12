package perceptron;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import util.Observation;

public class App {
    public static void main(String[] args) throws Exception {
        List<Observation> trainingData = Arrays.asList(Observation.getData(Paths.get("../data/iris/iris_training.txt")));
        List<Observation> testData = Arrays.asList(Observation.getData(Paths.get("../data/iris/iris_test.txt")));

        int numberOfAttributes = trainingData.get(0).getNumberOfAttributes();
        Perceptron perceptron = new Perceptron(numberOfAttributes, 0.0, 0.1);

        String positiveLabel = "Iris-setosa";
        int maxEpochs = 100;
        int epochs = perceptron.Learn(trainingData, positiveLabel, maxEpochs);
        System.out.println("Training completed in " + epochs + " epochs.");

        int correctClassifications = 0;
        for (Observation observation : testData) {
            int output = perceptron.Compute(observation);
            String predictedLabel = output == 1 ? positiveLabel : "Not-" + positiveLabel;
            if (predictedLabel.equals(observation.getLabel())) {
                correctClassifications++;
            }
        }

        double accuracy = (double) correctClassifications / testData.size() * 100;
        System.out.println("Accuracy: " + accuracy + "%");
    }
}

