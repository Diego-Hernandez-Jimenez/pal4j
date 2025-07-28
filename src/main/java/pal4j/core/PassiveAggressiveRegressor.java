/**
 * Concrete class for the (linear) regression task with passive-aggressive optimization.
 * @author Diego Hernández Jiménez
 */

package pal4j.core;

import pal4j.datautils.VectorOperations;

public class PassiveAggressiveRegressor extends PassiveAggressiveModel {

    /**
     * "Tolerance" parameter. Maximum discrepancy allowed when making predictions.
     */
    private double epsilon;

    /**
     * Constructor for PA regression model. Inherited from PassiveAggressiveModel parent class.
     * @param algorithmTypeName Short name of learning strategy. It can be "PA", "PA_1" or "PA_2".
     * @param C Aggressiveness parameter. It's used only when the algorithm type is "PA_1" or "PA_2".
     * @param numberOfFeatures Number of features present in the model. It's necessary to initialize the weights vector.
     * @param epsilon Tolerance parameter. Maximum discrepancy allowed when making predictions.
     */
    public PassiveAggressiveRegressor(String algorithmTypeName, double C, int numberOfFeatures, double epsilon) {
        super(algorithmTypeName, C, numberOfFeatures);
        this.setEpsilon(epsilon);
    }

    /**
     * Setter for the epsilon parameter.
     * @param epsilon Non-negative real value.
     */
    private void setEpsilon(double epsilon) {
        if (epsilon < 0.0) {
            throw new IllegalArgumentException("Epsilon cannot be negative");
        } else {
            this.epsilon = epsilon;
        }
    }

    public double getEpsilon() {
        return this.epsilon;
    }

    /**
     * Score for the given input observation.
     * @param x Input observation, feature vector.
     * @return Score.
     */
    @Override
    public double calculateScore(double[] x) {
        return VectorOperations.dotProduct(this.weights, x);
    }

    /**
     * Numerical prediction of the regression model in the same units as the target variable y.
     * @param score Precalculated raw score used for prediction.
     * @return Predicted value associated with the score, which in turn is associated with a given data observation.
     */
    @Override
    public double predict(double score) {
        return score;
    }

    /**
     * Calculates the hinge loss value.
     * @param score Score given by the model to a given input.
     * @param y Target value associated with the input observation.
     * @return Loss value.
     */
    @Override
    protected double sufferLoss(double score, double y) {
        return Math.max( 0.0, Math.abs(score - y) - this.epsilon);
    }

    /**
     * Calculates direction vector used to update weights. Implementation is based on Crammer et al. (2006).
     * @param score Score given by the model to a given input.
     * @param x Input observation, feature vector.
     * @param y Target value associated with the input observation.
     * @return Update vector.
     */
    @Override
    protected double[] calculateUpdateVector(double score, double[] x, double y) {
        return VectorOperations.scalarMultiplication( Math.signum( y - score ), x );
    }

    /**
     * Displays basic information about the instantiated model.
     * @return Message with metadata about the model.
     */
    @Override
    public String toString() {

        return String.format(
                "Task: Regression\nPassive Aggressive model type: %s.\nAggressiveness parameter C: %.2f\nEpsilon: %.2f",
                this.ALGORITHM_TYPE.name, this.getC(), this.epsilon
        );
    }
}
