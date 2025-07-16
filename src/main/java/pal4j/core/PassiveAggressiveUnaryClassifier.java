//package pal4j.core;
//
//import pal4j.datautils.VectorOperations;
//
//public class PassiveAggressiveUnaryClassifier extends PassiveAggressiveModel<double[], double[]> {
//
//    public double epsilon;
//
//    public PassiveAggressiveUnaryClassifier(String algorithmTypeName, double C, int numberOfFeatures, double epsilon) {
//        super(algorithmTypeName, C, numberOfFeatures);
//        this.epsilon = epsilon;
//    }
//
//    @Override
//    public double[] calculateScore(double[] x) {
//        return this.weights;
//    }
//
//    @Override
//    public double[] predict(double[] score) {
//        return score;
//    }
//
//    @Override
//    protected double sufferLoss(double[] score, double[] y) {
//        return Math.max( 0.0, VectorOperations.l2Norm(VectorOperations.subtract(this.weights, y)) - this.epsilon);
//    }
//
//    @Override
//    protected double calculateTau(double loss, double[] x) {
//        double squaredNorm = 1.0;
//        return this.tauCalculator.calculate(loss, squaredNorm);
//    }
//
//    @Override
//    protected double[] calculateUpdateVector(double[] score, double[] x, double[] y) {
//        double k = 1.0 / VectorOperations.l2Norm(VectorOperations.subtract(y, score));
//        double[] u = VectorOperations.subtract(score, y);
//        return VectorOperations.scalarMultiplication(k, u);
//    }
//}
