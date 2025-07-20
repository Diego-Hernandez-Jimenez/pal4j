package pal4j.core;

import pal4j.datautils.VectorOperations;

import java.util.Arrays;

public class PassiveAggressiveAnomalyDetector extends PassiveAggressiveModel {
    public double epsilon = 0.0;
    public double B;

    public PassiveAggressiveAnomalyDetector(String algorithmTypeName, double C, int numberOfFeatures, double B) {
        super(algorithmTypeName, C, numberOfFeatures + 1);
        this.weights[numberOfFeatures] = B;
         this.B = B;
    }
//    public PassiveAggressiveAnomalyDetector(String algorithmTypeName, double C, int numberOfFeatures, double epsilon) {
//        super(algorithmTypeName, C, numberOfFeatures);
//         this.epsilon = epsilon;
//    }

    @Override
    public double calculateScore(double[] x) {
        return VectorOperations.l2Norm(VectorOperations.subtract(this.weights, x));
    }

    @Override
    public double predict(double score) {
        return Math.signum(score - this.epsilon);
    }

    @Override
    protected double sufferLoss(double score, double y) {
        return Math.max( 0.0, score - this.B);
    }

    @Override
    protected double calculateTau(double loss, double[] x) {
        double squaredNorm = 1.0;
        return this.tauCalculator.calculate(loss, squaredNorm);
    }

    @Override
    protected double[] calculateUpdateVector(double score, double[] x, double y) {
//        double k = 1.0 / VectorOperations.l2Norm(VectorOperations.subtract(x, this.weights));
        double k = 1.0 / score;
        double[] u = VectorOperations.subtract(x, this.weights);
        return VectorOperations.scalarMultiplication(k, u);
    }

    public void fit(double[] x, double y) {
        double score = this.calculateScore(x);
        double loss = this.sufferLoss(score, y);
        double tau = this.calculateTau(loss, x);
        double[] updateVector = this.calculateUpdateVector(score, x, y);
        int extraFeatureId = this.weights.length - 1;
        for (int i = 0; i < x.length; i++) {
            this.weights[i] += tau * updateVector[i];
        }
//        this.weights[extraFeatureId] = Math.sqrt( Math.pow(this.B, 2) - Math.pow(this.epsilon, 2) );
        this.epsilon = Math.sqrt( Math.pow(this.B, 2) - Math.pow(this.weights[extraFeatureId], 2) );
    }

}
