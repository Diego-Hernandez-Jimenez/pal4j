/**
 * Fundamental abstraction for Passive-Aggressive (PA) learning algorithms.
 * It provides attributes and methods common to all PA methods.
 * @author Diego Hernández Jiménez

 */
package pal4j.core;

import pal4j.datautils.VectorOperations;

public abstract class PassiveAggressiveModel {

    public final AlgorithmType ALGORITHM_TYPE;
    private double C;
    public double[] weights;
    protected TauCalculator tauCalculator;

    /**
     * Constructor for PassiveAggressiveModel
     * @param algorithmTypeName Short name of learning strategy. It can be "PA", "PA_1" or "PA_2".
     * @param C Aggressiveness parameter. It's used only when the algorithm type is "PA_1" or "PA_2".
     * @param numberOfFeatures Number of features present in the model. It's necessary to initialize the weights vector.
     */
    public PassiveAggressiveModel(String algorithmTypeName, double C, int numberOfFeatures) {
        this.ALGORITHM_TYPE = AlgorithmType.valueOf(algorithmTypeName);
        this.setC(C);
        this.setWeights(numberOfFeatures);
        this.setTauCalculator();
    }

    /**
     * Score or output value given by the model to the input observation. This raw score is not the final prediction of the model.
     * @param x Input observation, feature vector.
     * @return Real value corresponding to the score of x. In binary classification, for example it's the margin.
     */
    public abstract double calculateScore(double[] x);

    /**
     * Decision function to make prediction on input. Depending on the setting (classification, regression, anomaly detection), the rule may vary.
     * @param score Precalculated raw score used for prediction.
     * @return Real value representing the final prediction of the model. It can be a label or a quantity.
     */
    public abstract double predict(double score);

    /**
     * Calculation of loss value for a given input instance. This discrepancy is measured using the score of the model and the target value.
     * @param score Score given by the model to a given input.
     * @param y Target value associated with the input observation. It can be a label or a quantity.
     * @return Loss value.
     */
    protected abstract double sufferLoss(double score, double y);

    /**
     * Method to get the direction vector that is added to the weights to update the model. The way the vector is obtained depends on the method.
     * @param score Score given by the model to a given input.
     * @param x Input observation, feature vector.
     * @param y Target value associated with the input observation. It can be a label or a quantity.
     * @return Update vector.
     */
    protected abstract double[] calculateUpdateVector(double score, double[] x, double y);

    /**
     * Setter for aggressiveness parameter
     * @param C Real positive value representing aggressiveness parameter
     */
    public void setC(double C) {
        if (C < 0.0) {
            throw new IllegalArgumentException("C cannot be negative");
        } else if (this.ALGORITHM_TYPE.equals(AlgorithmType.PA)) {
            System.out.println("C parameter will be ignored");
            this.C = 0.0;
        }else {
            this.C = C;
        }
    }

    public double getC() {
        return this.C;
    }

    /**
     * Setter to initialize the weights vector
     * @param numberOfFeatures Number of features present in the model. It determines the length of the weights array.
     */
    private void setWeights(int numberOfFeatures) {
        if (numberOfFeatures <= 0) {
            throw new IllegalArgumentException("number of features cannot be less or equal to 0");
        } else {
            this.weights = new double[numberOfFeatures];
        }
    }


//    public double[] getWeights() {
//        return this.weights;
//    }

    /**
     * Setter for tau calculator. Depending on the algorithm's type, the parameter tau (support vector) will be calculated in one way or another.
     * Implementation based on Crammer et al. (2006).
     */
    protected void setTauCalculator() {
        switch (this.ALGORITHM_TYPE) {
            case PA -> this.tauCalculator = (loss, squaredNorm) -> loss / squaredNorm;
            case PA_1 -> this.tauCalculator = (loss, squaredNorm) -> Math.min( this.C, loss / squaredNorm );
            case PA_2 -> this.tauCalculator = (loss, squaredNorm) -> Math.min( this.C, loss / ( squaredNorm + 1/(2 * this.C) ) );
        }
    }

    /**
     * Method to calculate tau. All PA methods employ the loss and a squared norm term, but the way the latter is calculated may differ.
     * @param loss Precalculated loss.
     * @param x Instance.
     * @return The value of tau.
     */
    protected double calculateTau(double loss, double[] x) {
        double squaredNorm = VectorOperations.dotProduct(x, x); // Math.pow(VectorOperations.l2Norm(x), 2);
        return this.tauCalculator.calculate(loss, squaredNorm);
    }

    /**
     * Method to update the weights vector in each iteration.
     * Implementation based on Crammer et al. (2006).
     * @param x Instance.
     * @param y Target variable. This meaning applies only in the classification and regression setting.
     */
    public void fit(double[] x, double y) {
        double score = this.calculateScore(x);
        double loss = this.sufferLoss(score, y);
        double tau = this.calculateTau(loss, x);
        double[] updateVector = this.calculateUpdateVector(score, x, y);
        for (int i = 0; i < x.length; i++) {
            this.weights[i] += tau * updateVector[i];
        }
    }


}
