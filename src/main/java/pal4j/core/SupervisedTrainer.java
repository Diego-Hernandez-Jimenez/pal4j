package pal4j.core;

import pal4j.datautils.BufferedDataset;
import pal4j.datautils.EvaluationMetric;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SupervisedTrainer {

    public PassiveAggressiveModel model;
    public BufferedDataset data;
    public EvaluationMetric metric;


    public SupervisedTrainer(
            PassiveAggressiveModel model,
            BufferedDataset data,
            EvaluationMetric metric
    ) {
        this.model = model;
        this.data = data;
        this.metric = metric;
    }

    public void train(Integer iterations) {

        try (BufferedReader br = new BufferedReader(new FileReader(this.data.FILE_PATH))) {
//        try (BufferedReader br = Files.newBufferedReader(Paths.get(this.data.FILE_PATH))) {
            if (iterations == null) iterations = -1; // by being negative we always satisfy the first part of the while condition
            String line;
            int i = 0;
            // Skip the first line (header)
            br.readLine();

            int[] featureIds = this.data.getFeatureIds();
            double[] x = new double[featureIds.length];
            double y;

            while (i != iterations && (line = br.readLine()) != null) {
                String[] values = line.split(this.data.SEPARATOR);
                for (int j = 0; j < featureIds.length; j++) {
                    x[j] = Double.parseDouble(values[featureIds[j]]);
                }
                y = Double.parseDouble(values[this.data.getTargetId()]);

                double score = this.model.calculateScore(x);
                double prediction = this.model.predict(score);
                this.model.fit(x, y);
                this.metric.accumulate(y, prediction);

                ++i;
            }

            this.metric.reduce();
            System.out.println(this.metric.finalScore);

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
//            e.printStackTrace();
        }

    }

//    public void trainOffline(int epochs) {
//
//        List<DataRecord> allLines = new ArrayList<DataRecord>();
//        double accuracy = 0.0;
//
//        // epoch 0
//        try (BufferedReader br = new BufferedReader(new FileReader(this.data.FILE_PATH))) {
//            String line;
//            // Skip the first line (header)
//            br.readLine();
//
//            int[] featureIds = this.data.getFeatureIds();
//            double[] x = new double[featureIds.length];
//            double y;
//
//            while ((line = br.readLine()) != null) {
//                String[] values = line.split(this.data.SEPARATOR);
//                for (int j = 0; j < featureIds.length; j++) x[j] = Double.parseDouble(values[featureIds[j]]);
//                y = Double.parseDouble(values[this.data.getTargetId()]);
//                allLines.add(new DataRecord(x, y));
//
//                double score = this.model.calculateScore(x);
//                this.metric.accumulate(y, this.model.predict(score));
//                this.model.fit(x, y);
//
//            }
//
//        } catch (IOException e) {
//            System.err.println("Error reading the CSV file: " + e.getMessage());
//        }
//
//        // rest of epochs
//        for (int epoch = 1; epoch < epochs; epoch++) {
//            // randomize order of rows
//            Collections.shuffle(allLines);
//            for (DataRecord line : allLines) {
//                double score = this.model.calculateScore(line.x());
//                this.metric.accumulate(line.y(), this.model.predict(score));
//                this.model.fit(line.x(), line.y());
//            }
//        }
//
//        this.metric.reduce();
////        System.out.println(accuracy / (allLines.size() * epochs));
//        System.out.println(this.metric.finalScore);
//    }
//
    public void evaluate(BufferedDataset testData) {
        this.metric.reset();
        int nRows = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(testData.FILE_PATH))) {
            String line;
            // Skip the first line (header)
            br.readLine();

            int[] featureIds = testData.getFeatureIds();
            double[] x = new double[featureIds.length];
            double y;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(testData.SEPARATOR);
                for (int j = 0; j < featureIds.length; j++) x[j] = Double.parseDouble(values[featureIds[j]]);
                y = Double.parseDouble(values[testData.getTargetId()]);

                var prediction = this.model.predict(this.model.calculateScore(x));
                this.metric.accumulate(y, prediction);

                ++nRows;
            }

            this.metric.reduce();
            System.out.println(this.metric.finalScore);

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
    }

}
