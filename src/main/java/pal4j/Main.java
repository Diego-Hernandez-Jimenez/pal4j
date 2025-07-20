package pal4j;


import pal4j.core.*;
import pal4j.datautils.Accuracy;
import pal4j.datautils.BufferedDataset;
import pal4j.datautils.MAE;
import pal4j.nonlinear.GaussianKernel;
import pal4j.nonlinear.KernelPassiveAggressiveRegressor;

import java.util.Arrays;

public class Main {

    public static void occupancyExample() {
        var features = new String[] {"Temperature", "Humidity", "Light", "CO2", "HumidityRatio"};
        var trainData= new BufferedDataset(
                "src/main/resources/training_occupancy_dataset.csv",
                ",",
                features,
                "Occupancy"

        );
        var testData= new BufferedDataset(
                "src/main/resources/test_occupancy_dataset.csv",
                ",",
                features,
                "Occupancy"

        );
        var metric = new Accuracy();
        var model = new PassiveAggressiveBinaryClassifier("PA_1", 0.1, features.length);
        var trainer = new SupervisedTrainer(model, trainData, metric);
        trainer.train(null);
        System.out.println("Accuracy: " + trainer.metric.finalScore);

        trainer.evaluate(new BufferedDataset("src/main/resources/test_occupancy_dataset.csv", ",", features, "Occupancy"));
    }

    public static void seoulBikeExample() {
        var features = new String[] {
                "temperature",
                "humidity",
                "wind speed",
                "visibility",
                "solar radiation",
                "rainfall",
                "snowfall",
                "dummy_holiday",
                "dummy_functioning_day",
                "dummy_winter",
                "dummy_spring",
                "dummy_summer",
                "dummy_autumn"
        };
        var df = new BufferedDataset(
                "src/main/resources/seoul_bike_dataset.csv",
                ",",
                features,
                "rented bike count"

        );

        System.out.println("Linear model");
        var linearModel = new PassiveAggressiveRegressor("PA", 1.0, features.length, 1.0);
        var linearTrainer = new SupervisedTrainer(linearModel, df, new MAE());
        linearTrainer.train(null);
        linearTrainer.evaluate(new BufferedDataset("src/main/resources/seoul_bike_dataset.csv", ",", features, "rented bike count"));

        System.out.println("Kernel model");
        var kernelModel = new KernelPassiveAggressiveRegressor("PA_2", 1000, features.length, 1.0, new GaussianKernel(1.0));
        var kernelTrainer = new SupervisedTrainer(kernelModel, df, new MAE());
        kernelTrainer.train(null);
        kernelTrainer.evaluate(new BufferedDataset("src/main/resources/seoul_bike_dataset.csv", ",", features, "rented bike count"));

    }

    public static void breastCancerExample() {
        var features = java.util.stream.IntStream.range(3, 32)
                .mapToObj(i -> "V" + i)
                .toArray(String[]::new);

        var df = new BufferedDataset(
                "src/main/resources/breast_cancer_dataset.csv",
                ";",
                features,
                "diagnosis"

        );
        var metric = new Accuracy();
        var model = new PassiveAggressiveBinaryClassifier("PA_1", 0.7, features.length);
        var trainer = new SupervisedTrainer(model, df, metric);
        trainer.train(null);
//        trainer.evaluate(new BufferedDataset("src/main/resources/breast_cancer_dataset.csv", ";", features, "diagnosis"));
    }

    public static void exampleSST2() {
        var features = java.util.stream.IntStream.range(0, 769)
                .mapToObj(i -> "x" + i)
                .toArray(String[]::new);

        var df = new BufferedDataset(
                "src/main/resources/sst2_features_training_dataset.csv",
                ";",
                features,
                "label"

        );
        var metric = new Accuracy();
        var model = new PassiveAggressiveBinaryClassifier("PA", 0.0, features.length);
//        var model = new KernelPassiveAggressiveBinaryClassifier("PA_1", 100, features.length, new GaussianKernel(0.5));
        var trainer = new SupervisedTrainer(model, df, metric);
        trainer.train(null);
        trainer.evaluate(new BufferedDataset("src/main/resources/sst2_features_test_dataset.csv", ";", features, "label"));
    }

    public static void exampleAD() {
        var features = java.util.stream.IntStream.range(1, 30)
                .mapToObj(i -> "V" + i)
                .toArray(String[]::new);

        var df = new BufferedDataset(
                "src/main/resources/fraud_normalized_train.csv",
                ",",
                features,
                "class"
        );

        var model = new PassiveAggressiveAnomalyDetector("PA_1", 1.0, features.length, 10);
        var trainer = new AnomalyDetectionTrainer(model, df);
        trainer.train(null);
        System.out.println(Arrays.toString(trainer.model.weights));
        System.out.println(trainer.model.epsilon);

        trainer.evaluate(
                new BufferedDataset("src/main/resources/fraud_normalized_test.csv", ",", features, "class"),
                new Accuracy()
        );
    }

    public static void exampleADThyroid() {
        var features = java.util.stream.IntStream.range(1, 22)
                .mapToObj(i -> "V" + i)
                .toArray(String[]::new);

        var df = new BufferedDataset(
                "C:/Users/Diego/javaprojects/processed_datasets/thyroid_normalized_train.csv",
                ",",
                features,
                "class"
        );

        var model = new PassiveAggressiveAnomalyDetector("PA_1", 0.1, features.length, 1);
        var trainer = new AnomalyDetectionTrainer(model, df);
        trainer.train(null);
        System.out.println(Arrays.toString(trainer.model.weights));
        System.out.println(trainer.model.epsilon);

        trainer.evaluate(
                new BufferedDataset("C:/Users/Diego/javaprojects/processed_datasets/thyroid_normalized_test.csv", ",", features, "class"),
                new Accuracy()
        );
    }

    public static void exampleADSynthetic() {
        var features = new String[] {"x1", "x2"};
        var df = new BufferedDataset(
                "C:/Users/Diego/javaprojects/processed_datasets/synthetic_train_dataset.csv",
                ",",
                features,
                "class"
        );

        var model = new PassiveAggressiveAnomalyDetector("PA_1", 0.1, features.length, 100);
        var trainer = new AnomalyDetectionTrainer(model, df);
        trainer.train(null);
        System.out.println(Arrays.toString(trainer.model.weights));
        System.out.println(trainer.model.epsilon);

        trainer.evaluate(
                new BufferedDataset("C:/Users/Diego/javaprojects/processed_datasets/synthetic_test_dataset.csv", ",", features, "class"),
                new Accuracy()
        );
    }

    public static void exampleADShuttle() {
        var features = java.util.stream.IntStream.range(1, 10)
                .mapToObj(i -> "V" + i)
                .toArray(String[]::new);

        var df = new BufferedDataset(
                "C:/Users/Diego/javaprojects/processed_datasets/shuttle_train.csv",
                ",",
                features,
                "class"
        );

        var model = new PassiveAggressiveAnomalyDetector("PA", 1, features.length, 71);
        var trainer = new AnomalyDetectionTrainer(model, df);
        trainer.train(null);
        System.out.println(Arrays.toString(trainer.model.weights));
        System.out.println(trainer.model.epsilon);

        trainer.evaluate(
                new BufferedDataset("C:/Users/Diego/javaprojects/processed_datasets/shuttle_test.csv", ",", features, "class"),
                new Accuracy()
        );
    }

    public static void main(String[] args) {

//        occupancyExample(); // linear binary classification
//        seoulBikeExample(); // kernel regression
//        breastCancerExample(); // linear binary classification
//        exampleSST2();
//        exampleADThyroid();
        exampleADShuttle();

//        var features =  new String[] {
//                "LIMIT_BAL", "AGE", "PAY_0", "PAY_2",
//                "BILL_AMT1", "BILL_AMT2",
//                "PAY_AMT1", "PAY_AMT2",
//                "DUMMY_SEX",
//                "MARR_1", "MARR_0"
//        };
//        var df = new BufferedDataset(
//                "src/main/resources/default_dataset.csv",
//                ";",
//                "default"
//
//        );
//        var metric = new Accuracy();
////        var model = new PassiveAggressiveBinaryClassifier("PA_2", 10, df.getFeatureIds().length);
//        var model = new KernelPassiveAggressiveBinaryClassifier("PA_2", 0.5, df.getFeatureIds().length, new GaussianKernel(1.0));
//        var trainer = new SupervisedTrainer(model, df, metric);
//        trainer.train(null);




    }

}
