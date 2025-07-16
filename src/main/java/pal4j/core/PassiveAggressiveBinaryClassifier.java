package pal4j.core;

import pal4j.datautils.VectorOperations;

public class PassiveAggressiveBinaryClassifier extends PassiveAggressiveModel {

    public PassiveAggressiveBinaryClassifier(String algorithmType, double C, int numberFeatures) {
        super(algorithmType, C, numberFeatures);
    }

    public double calculateScore(double[] x) {
        return VectorOperations.dotProduct(this.weights, x);
    }

    @Override
    public double predict(double score) {
        return Math.signum(score);
    }

    @Override
    protected double sufferLoss(double score, double y) {
        return Math.max(0.0, 1 - y * score);
    }

    @Override
    protected double[] calculateUpdateVector(double score, double[] x, double y) {
        return VectorOperations.scalarMultiplication(y, x);
    }

    @Override
    public String toString() {

        return String.format(
                "Task: Binary classification\nPassive Aggressive model type: %s.\nAggressiveness parameter C: %.2f",
                this.ALGORITHM_TYPE.name, this.getC()
        );
    }


}
