package pal4j;


import pal4j.core.*;
import pal4j.datautils.*;
import pal4j.nonlinear.GaussianKernel;
import pal4j.nonlinear.KernelPARegressor;

public class Main {
    
    public static final String basePath = "processed_datasets/";

    public static void exampleOccupancy() {

        System.out.println("Occupancy");

        var features = new String[] {"Temperature", "Humidity", "Light", "CO2", "HumidityRatio"};
        var trainData= new BufferedDataset(
                basePath + "occupancy_training.csv",
                ",",
                features,
                "Occupancy"

        );
        var testData= new BufferedDataset(
                basePath + "occupancy_test.csv",
                ",",
                features,
                "Occupancy"

        );
        var model = new PABinaryClassifier("PA_1", 0.1, features.length);
        var metric = new Accuracy();
        var trainer = new SupervisedTrainer(model, trainData, metric);
        trainer.train(null);
        trainer.evaluate(testData);

        System.out.println();
    }

    public static void exampleSeoulBike() {
        System.out.println("Seoul bikes");

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
                "dummy_autumn",
                "night",
                "morning",
                "afternoon",
                "evening"
        };
        var trainData = new BufferedDataset(
                basePath + "seoul_bike_train.csv",
                ",",
                features,
                "rented bike count"

        );
        var testData = new BufferedDataset(
                basePath + "seoul_bike_test.csv",
                ",",
                features,
                "rented bike count"

        );

        var scaledTrainData = new BufferedDataset(
                basePath + "seoul_bike_scaled_train.csv",
                ",",
                features,
                "rented bike count"
        );
        var scaledTestData = new BufferedDataset(
                basePath + "seoul_bike_scaled_test.csv",
                ",",
                features,
                "rented bike count"

        );

        System.out.println("Linear model");
        var linearModel = new PARegressor("PA", 1.0, features.length, 1.0);
        var linearTrainer = new SupervisedTrainer(linearModel, trainData, new MAE());
        linearTrainer.train(null);
        linearTrainer.evaluate(testData);

        System.out.println("\nKernel model");
        var kernelModel = new KernelPARegressor("PA_2", 1000, features.length, 1.0, new GaussianKernel(1.0));
        var kernelTrainer = new SupervisedTrainer(kernelModel, scaledTrainData, new MAE());
        kernelTrainer.train(null);
        kernelTrainer.evaluate(scaledTestData);

        System.out.println();
    }

    public static void exampleBreastCancer() {

        System.out.println("Breast cancer");

        var features = java.util.stream.IntStream.range(3, 32)
                .mapToObj(i -> "V" + i)
                .toArray(String[]::new);

        var trainData = new BufferedDataset(
                basePath + "breast_cancer_scaled_train.csv",
                ",",
                features,
                "diagnosis"

        );
        var testData = new BufferedDataset(
                basePath + "breast_cancer_scaled_test.csv",
                ",",
                features,
                "diagnosis"

        );
        var metric = new Accuracy();
        var model = new PABinaryClassifier("PA_1", 5, features.length);
        var trainer = new SupervisedTrainer(model, trainData, metric);
        trainer.train(null);
        trainer.evaluate(testData);

        System.out.println();
    }

    public static void exampleSST2() {
        System.out.println("Stanford Sentiment Treebank v2");

        var features = java.util.stream.IntStream.range(0, 769)
                .mapToObj(i -> "x" + i)
                .toArray(String[]::new);

        var trainData = new BufferedDataset(
                basePath + "sst2_features_train.csv",
                ";",
                features,
                "label"
        );
        var testData = new BufferedDataset(
                basePath + "sst2_features_test.csv",
                ";",
                features,
                "label"
        );
        var metric = new Accuracy();
        var model = new PABinaryClassifier("PA_1", 1, features.length);
        var trainer = new SupervisedTrainer(model, trainData, metric);
        trainer.train(null);
        trainer.evaluate(testData);

        System.out.println();
    }

    public static void exampleADFraud() {

        System.out.println("Fraud");

        var features = java.util.stream.IntStream.range(1, 30)
                .mapToObj(i -> "V" + i)
                .toArray(String[]::new);

        var trainData = new BufferedDataset(
                basePath + "fraud_scaled_train.csv",
                ",",
                features,
                "class"
        );
        var testData = new BufferedDataset(
                basePath + "fraud_scaled_test.csv",
                ",",
                features,
                "class"
        );

        var model = new PAAnomalyDetector("PA_1", 100, features.length, 5);
        var trainer = new AnomalyDetectionTrainer(model, trainData);
        trainer.train(null);
        trainer.evaluate(testData, new Accuracy());
    }

    public static void exampleDemand() {

        System.out.println("Electricity demand");
        var features = new String[] {
                "demand_lag1",
                "demand_lag2",
                "demand_lag3",
                "demand_lag4",
//                "solar_exposure_lag1",
//                "solar_exposure_lag2",
//                "solar_exposure_lag3",
//                "min_temp_lag1",
//                "min_temp_lag2",
//                "min_temp_lag3",
//                "max_temp_lag1",
//                "max_temp_lag2",
//                "max_temp_lag3",
//                "rainfall_lag1",
//                "rainfall_lag2",
//                "rainfall_lag3",
//                "rrp_lag1",
//                "rrp_lag2",
//                "rrp_lag3",
//                "dummy_school_day",
//                "dummy_holiday",
        };
        var trainData = new BufferedDataset(
                basePath + "demand_train.csv",
                ",",
                features,
                "demand"

        );
        var testData = new BufferedDataset(
                basePath + "demand_test.csv",
                ",",
                features,
                "demand"

        );

        var model = new PARegressor("PA_1", 1, features.length, 10000);
        var trainer = new SupervisedTrainer(model, trainData, new MAPE());
        trainer.train(null);
        trainer.evaluate(testData);

    }

    public static void exampleADPhishing() {

        System.out.println("Phishing");

        var trainData = new BufferedDataset(
                basePath + "ad_phishing_train.csv",
                ",",
                "label"
        );
        var testData = new BufferedDataset(
                basePath + "ad_phishing_test.csv",
                ",",
                "label"
        );

        var model = new PAAnomalyDetector("PA_1", 100, trainData.FEATURE_NAMES.length, 1e4);
        var trainer = new AnomalyDetectionTrainer(model, trainData);
        trainer.train(null);

        // not ideal, it would be best to calculate both metrics in the same pass
        trainer.evaluate(testData, new Sensitivity());
        trainer.evaluate(testData, new Specificity());

        System.out.println();
    }

    public static void exampleBCPhishing() {
        System.out.println("Phishing");

        var trainData = new BufferedDataset(
                basePath + "binary_phishing_train.csv",
                ",",
                "label"
        );

        var testData = new BufferedDataset(
                basePath + "binary_phishing_test.csv",
                ",",
                "label"
        );

        var model = new PABinaryClassifier("PA_1", 100, trainData.FEATURE_NAMES.length);
        var metric = new Accuracy();
        var trainer = new SupervisedTrainer(model, trainData, metric);
        trainer.train(null);

        trainer.evaluate(testData);

        System.out.println();
    }

    public static void main(String[] args) {

        System.out.println("Binary classification\n");
        exampleBreastCancer();
        exampleBCPhishing();
        exampleOccupancy();
        exampleSST2();

        System.out.println("\nRegression/time series\n");
        exampleSeoulBike();
        exampleDemand();

        System.out.println("\nAnomaly detection");
        exampleADPhishing();
        exampleADFraud();
    }

}
