/**
 * High-level trainer class for anomaly detection learning (uniclass prediction). This class takes a model, dataset, and evaluation metric, and handles the training process end-to-end
 * @author Diego Hernández Jiménez
 */

package pal4j.core;

import pal4j.datautils.BufferedDataset;
import pal4j.datautils.EvaluationMetric;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AnomalyDetectionTrainer {

    /**
     * Learning model.
     */
    public PAAnomalyDetector model;

    /**
     * Train dataset.
     */
    public BufferedDataset data;

    public AnomalyDetectionTrainer(
            PAAnomalyDetector model,
            BufferedDataset data
    ) {
        this.model = model;
        this.data = data;
    }

    /**
     * Method for online training of anomaly detection Passive-Aggressive models.
     * @param iterations Number of training iterations. If null, training uses the full dataset.
     */
    public void train(Integer iterations) {

        try (BufferedReader br = new BufferedReader(new FileReader(this.data.FILE_PATH))) {
//        try (BufferedReader br = Files.newBufferedReader(Paths.get(this.data.FILE_PATH))) {
            if (iterations == null) iterations = -1; // by being negative we always satisfy the first part of the while condition
            String line;
            int i = 0;
            // Skip the first line (header)
            br.readLine();

            int[] featureIds = this.data.getFeatureIds();
            double[] x = new double[featureIds.length + 1];

            while (i != iterations && (line = br.readLine()) != null) {
                String[] values = line.split(this.data.SEPARATOR);
                for (int j = 0; j < featureIds.length; j++) {
                    x[j] = Double.parseDouble(values[featureIds[j]]);
                }
                x[x.length - 1] = 0.0;

                this.model.fit(x, 0.0);

                ++i;
            }
            this.model.weights[featureIds.length] = 0.0;

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
    }

    /**
     * Method for model evaluation. The difference with the train method is that the model is "frozen", it's not updated.
     * Also, an evaluation metric is used to assess performance.
     * @param testData Test dataset.
     * @param metric Performance metric.
     */
    public void evaluate(BufferedDataset testData, EvaluationMetric metric) {

        try (BufferedReader br = new BufferedReader(new FileReader(testData.FILE_PATH))) {
            String line;
            // Skip the first line (header)
            br.readLine();

            int[] featureIds = testData.getFeatureIds();
            double[] x = new double[featureIds.length + 1];
            double y;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(testData.SEPARATOR);
                for (int j = 0; j < featureIds.length; j++) {
                    x[j] = Double.parseDouble(values[featureIds[j]]);
                }
                x[x.length - 1] = 0.0;
                y = Double.parseDouble(values[testData.getTargetId()]);

                var prediction = this.model.predict(this.model.calculateScore(x));
                metric.accumulate(y, prediction);
            }

            metric.reduce();
            System.out.printf("Test %s: %.2f\n", metric.getName(), metric.getFinalScore());

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
    }
}
