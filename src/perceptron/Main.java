package perceptron;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import util.ActivationFunctions;
import util.Observation;
import util.Perceptron;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Observation> trainingData = Observation.getDataFromCSV(Paths.get("src/data/iris/iris_training.txt"));
        List<Observation> testData = Observation.getDataFromCSV(Paths.get("src/data/iris/iris_test.txt"));
        String[] labels = { "Iris-setosa", "Iris-virginica", "Iris-versicolor" };
        
        int numberOfAttributes = trainingData.get(0).getNumberOfAttributes();

        for (String positiveLabel : labels) {
            Perceptron perceptron = new Perceptron("",numberOfAttributes, 0.05, ActivationFunctions.BINARYSTEP);

            int epochs = perceptron.train(trainingData, positiveLabel, 500);
            System.out.println("Training completed in " + epochs + " epochs for: "+positiveLabel);

            perceptron.test(testData, positiveLabel);
        }
    }
}
