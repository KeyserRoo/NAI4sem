package util;

import java.util.List;

public class Layer {
	private final Perceptron[] perceptrons;

	public Layer(List<String> labels, int numInputs, ActivationFunctions funct) {
		perceptrons = new Perceptron[labels.size()];
		for (int i = 0; i < perceptrons.length; i++) {
			perceptrons[i] = new Perceptron(labels.get(i), numInputs, 0.05, funct);
		}
	}

	public void train(List<Observation> trainData, int maxEpochs) {
		for (int i = 0; i < perceptrons.length; i++) {
			perceptrons[i].train(trainData, perceptrons[i].getTarget(), maxEpochs);
		}
	}

	public void test(List<Observation> testData) {
		double total = 0.0, correct = 0.0;
		for (Observation observation : testData) {
			String predictedLanguage = classifyText(observation.getAttributes());

			System.out.println("Predicted: " + predictedLanguage + " Actual: " + observation.getLabel());
			if (predictedLanguage.equals(observation.getLabel()))
				correct += 1;
			total += 1;
		}

		System.out.println("Poprawnosc: " + ((correct / total) * 100));
	}

	public String classifyText(double[] percentages) {
		double maxOutput = Double.NEGATIVE_INFINITY;
		String predictedLanguage = null;

		for (int i = 0; i < perceptrons.length; i++) {
			double output = perceptrons[i].compute(percentages);
			if (output > maxOutput) {
				maxOutput = output;
				predictedLanguage = perceptrons[i].getTarget();
			}
		}

		return predictedLanguage;
	}
}