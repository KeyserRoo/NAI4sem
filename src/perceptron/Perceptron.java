package perceptron;

import java.util.List;

import util.Observation;

class Perceptron {
    private final double[] weights;
    private double bias;
    private final double learningRate;

    public Perceptron(int numberOfAttributes, double bias, double learningRate) {
        this.weights = new double[numberOfAttributes];
        this.bias = bias;
        this.learningRate = learningRate;
    }

    public int Compute(Observation observation) {
        double[] inputs = observation.getAttributes();
        if (inputs.length != weights.length) {
            throw new IllegalArgumentException("Input size must match the number of weights.");
        }

        double sum = 0;
        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }
        sum += bias;
        return sum >= 0 ? 1 : 0;
    }

    public int Learn(List<Observation> trainingData, String positiveLabel, int maxEpochs) {
        int epochs = 0;
        while (epochs < maxEpochs) {
            boolean allCorrect = true;
            for (Observation observation : trainingData) {
                double[] inputs = observation.getAttributes();
                int expectedOutput = observation.getLabel().equals(positiveLabel) ? 1 : 0;
                int output = Compute(observation);
                int error = expectedOutput - output;

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

    public double[] getWeights() {
        return weights.clone();
    }

    public double getBias() {
        return bias;
    }

    public double getLearningRate() {
        return learningRate;
    }
}