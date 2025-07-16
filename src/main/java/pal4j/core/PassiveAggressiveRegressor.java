package pal4j.core;

import pal4j.datautils.VectorOperations;

public class PassiveAggressiveRegressor extends PassiveAggressiveModel {

    private double epsilon;

    public PassiveAggressiveRegressor(String algorithmTypeName, double C, int numberOfFeatures, double epsilon) {
        super(algorithmTypeName, C, numberOfFeatures);
        this.setEpsilon(epsilon);
    }

    public void setEpsilon(double epsilon) {
        if (epsilon < 0.0) {
            throw new IllegalArgumentException("Epsilon cannot be negative");
        } else {
            this.epsilon = epsilon;
        }
    }

    public double getEpsilon() {
        return this.epsilon;
    }

    @Override
    public double calculateScore(double[] x) {
        return VectorOperations.dotProduct(this.weights, x);
    }

    @Override
    public double predict(double score) {
        return score;
    }

    @Override
    protected double sufferLoss(double score, double y) {
        return Math.max( 0.0, Math.abs(score - y) - this.epsilon);
    }

    @Override
    protected double[] calculateUpdateVector(double score, double[] x, double y) {
        return VectorOperations.scalarMultiplication( Math.signum( y - score ), x );
    }

    @Override
    public String toString() {

        return String.format(
                "Task: Regression\nPassive Aggressive model type: %s.\nAggressiveness parameter C: %.2f\nEpsilon: %.2f",
                this.ALGORITHM_TYPE.name, this.getC(), this.epsilon
        );
    }
}
