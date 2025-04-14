package oneLayerNetwork;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import util.Layer;
import util.Observation;

public class GUI {
  JFrame frame = new JFrame("Language Classifier");
  JTextField textField = new JTextField(20);
  JButton button = new JButton("Classify");

  public GUI(Layer layer) {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300, 200);

    textField.setBounds(50, 50, 200, 25);

    button.setBounds(50, 100, 200, 25);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        try {
          String inputText = textField.getText();
          String predictedLanguage = layer.classifyText(Observation.stringToPercentages(inputText));
          System.out.println("Predicted language: " + predictedLanguage);
        } catch (IOException error) {
          error.printStackTrace();
        }
      }
    });

    frame.getContentPane().add(textField);
    frame.getContentPane().add(button);

    frame.setLayout(null);
  }

  public void run() {
    frame.setVisible(true);
  }
}
