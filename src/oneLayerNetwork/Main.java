package oneLayerNetwork;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import util.ActivationFunctions;
import util.Layer;
import util.Observation;

public class Main {

	public static void main(String[] args) throws IOException {
		Path trainPath = Paths.get("src/data/languages/trainData");
		List<Observation> trainData = Observation.getDataFromFolder(trainPath);

		Path testPath = Paths.get("src/data/languages/testData");
		List<Observation> testData = Observation.getDataFromFolder(testPath);

		List<String> languages = new ArrayList<>();
		for (Observation observation : trainData) {
			String lang = observation.getLabel();
			if(!languages.contains(lang)) languages.add(lang);
		}

		Layer layer = new Layer(languages,26,ActivationFunctions.SIGMOID);

		layer.train(trainData,600);
		layer.test(testData);

		GUI gui = new GUI(layer);
		gui.run();
	}
}
