package pal4j;


import pal4j.core.PassiveAggressiveBinaryClassifier;
import pal4j.core.Trainer;
import pal4j.datautils.BufferedDataset;
import pal4j.datautils.Sensitivity;

public class Main {
    public static void hello() {
        System.out.println("hello");
    }
    public static void main(String[] args) {

//        var features = java.util.stream.IntStream.range(3, 32)
//                .mapToObj(i -> "V" + i)
//                .toArray(String[]::new);
//
//        var df = new BufferedDataset(
//                "src/main/resources/breast_cancer_dataset.csv",
//                ";",
//                features,
//                "diagnosis"
//
//        );
//        var metric = new AccuracyMetric();
//        var model = new PassiveAggressiveBinaryClassifier("PA_1", 5.0, features.length);
////        var model = new KernelPassiveAggressiveBinaryClassifier("PA_2", 0.5, features.length, new GaussianKernel(1));
//        var trainer = new Trainer(model, df, metric);
//        trainer.train(null);
//        trainer.evaluate(new BufferedDataset("src/main/resources/breast_cancer_dataset.csv", ";", features, "diagnosis"));


        var features = new String[] {"Temperature", "Humidity", "Light", "CO2", "HumidityRatio"};
        var df = new BufferedDataset(
                "src/main/resources/training_occupancy_dataset.csv",
                ",",
                features,
                "Occupancy"

        );
        var metric = new Sensitivity();
        var model = new PassiveAggressiveBinaryClassifier("PA_1", 0.1, features.length);
        var trainer = new Trainer(model, df, metric);
        trainer.train(null);
//        trainer.evaluate(new BufferedDataFrame("src/main/resources/test_occupancy_dataset.csv", ",", features, "Occupancy"));

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
//        var metric = new AccuracyMetric();
////        var model = new PassiveAggressiveBinaryClassifier("PA_2", 10, df.getFeatureIds().length);
//        var model = new KernelPassiveAggressiveBinaryClassifier("PA_2", 0.5, df.getFeatureIds().length, new GaussianKernel(1.0));
//        var trainer = new Trainer(model, df, metric);
//        trainer.train(null);
        // trainer.evaluate(new BufferedDataset("src/main/resources/breast_cancer_dataset.csv", ";", "diagnosis"));


//        var features = new String[] {
//                "temperature",
//                "humidity",
//                "wind speed",
//                "visibility",
//                "solar radiation",
//                "rainfall",
//                "snowfall",
//                "dummy_holiday",
//                "dummy_functioning_day",
//                "dummy_winter",
//                "dummy_spring",
//                "dummy_summer",
//                "dummy_autumn"
//        };
//        var df = new BufferedDataset(
//                "src/main/resources/seoul_bike_dataset.csv",
//                ",",
//                features,
//                "rented bike count"
//
//        );
//
//        var metric = new MAEMetric();
//        var model = new KernelPassiveAggressiveRegressor("PA_2", 1000, features.length, 1.0, new GaussianKernel(1.0));
////        var model = new PassiveAggressiveRegressor("PA", 1.0, features.length, 1.0);
//        var trainer = new Trainer(model, df, metric);
//        trainer.train(null);

//        trainer.evaluate(new BufferedDataset("src/main/resources/seoul_bike_dataset.csv", ",", features, "rented bike count"));
//        System.out.println(Arrays.toString(trainer.model.weights));



    }

}
