package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Observation {
  private double[] attributes;
  private int numberOfAttributes;
  private String label;

  public Observation(String line) {
      try {
          String[] elements = line.replace(",", ".").split("[\t]");
          numberOfAttributes = elements.length - 1;
          attributes = new double[numberOfAttributes];
          for (int i = 0; i < numberOfAttributes; i++) {
              attributes[i] = Float.parseFloat(elements[i]);
          }
          label = elements[elements.length - 1].trim();
      } catch (Exception e) {
          throw new IllegalArgumentException("Zle parametry!");
      }
  }

  public static Observation[] getData(Path path) throws IOException {
      List<String> lines = Files.readAllLines(path);
      Observation[] observations = new Observation[lines.size()];

      for (int i = 0; i < observations.length; i++) {
          observations[i] = new Observation(lines.get(i));
      }

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
      return numberOfAttributes;
  }

  public String getLabel() {
      return label;
  }

}