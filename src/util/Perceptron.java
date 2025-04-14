package util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Perceptron {
	private final double[] weights;
	private double bias;
	private final double learningRate;
	private final ActivationFunctions function;

	private String target;

	public String getTarget() {
		return target;
	}

	public Perceptron(String tar, int numberOfAttributes, double learningRate, ActivationFunctions funct) {
		this.target=tar;
		this.weights = new double[numberOfAttributes];
		this.learningRate = learningRate;
		this.function = funct;
		initWeightsAndBias();
	}

	public double compute(double[] attributes) {
		if (attributes.length != weights.length) {
			throw new IllegalArgumentException("Input size must match the number of weights.");
		}

		double sum = weightedSum(attributes) + bias;
		return functions.get(function).apply(sum);
	}

	public int train(List<Observation> trainingData, String oVA, int maxEpochs) {
		int epochs = 0;
		while (epochs < maxEpochs) {
			boolean allCorrect = true;
			for (Observation observation : trainingData) {
				double[] inputs = observation.getAttributes();

				double expectedOutput = observation.getLabel().equals(oVA) ? 1.0 : 0.0;
				double output = compute(observation.getAttributes());
				double error = expectedOutput - output;

				if (error != 0) {
					allCorrect = false;
					for (int i = 0; i < weights.length; i++) {
						weights[i] += learningRate * error * inputs[i];
					}
					bias += learningRate * error;
				}
			}
			epochs++;
			if (allCorrect) {
				break;
			}
		}
		return epochs;
	}

	public void test(List<Observation> testData, String oVA){
		int correctClassifications = 0;
        for (Observation observation : testData) {
            int label = observation.getLabel().equals(oVA) ? 1 : 0;
            int prediction = (int) compute(observation.getAttributes());
            if (label == prediction)
                correctClassifications++;
        }

        double accuracy = ((double) correctClassifications / (double) testData.size()) * 100.0;
        System.out.println("Accuracy: " + accuracy + "%");
	}

	private double weightedSum(double[] attributes) {
		double sum = 0;
		for (int i = 0; i < attributes.length; i++) {
			sum += attributes[i] * weights[i];
		}
		return sum;
	}

	private void initWeightsAndBias() {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Math.random();
		}
		bias = Math.random();
	}

	private static final Map<ActivationFunctions, Function<Double, Double>> functions = Map.of(
			ActivationFunctions.IDENTITY, d -> d,

			ActivationFunctions.BINARYSTEP, d -> d >= 0 ? 1.0 : 0.0,

			ActivationFunctions.RELU, d -> Math.max(0, d),

			ActivationFunctions.SIGMOID, d -> 1 / (1 + Math.exp(-d)),

			ActivationFunctions.TANH, d -> Math.tanh(d),

			ActivationFunctions.GELU,
			d -> 0.5 * d * (1 + Math.tanh(Math.sqrt(2 / Math.PI) * (d + 0.044715 * Math.pow(d, 3)))),

			ActivationFunctions.SIGMOIDDERIVATIVE, d -> {
				double sigmoidValue = 1 / (1 + Math.exp(-d));
				return sigmoidValue * (1 - sigmoidValue);
			},

			ActivationFunctions.TANHDERIVATIVE, d -> 1 - Math.pow(Math.tanh(d), 2));
}
