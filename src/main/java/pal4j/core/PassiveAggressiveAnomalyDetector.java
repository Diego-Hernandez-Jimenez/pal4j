/**
 * Concrete class for the (linear) anomaly detection task with passive-aggressive optimization. It's based on Crammer et al. (2006),
 * specifically on the version with the learned epsilon.
 * @author Diego Hernández Jiménez
 */

package pal4j.core;

import pal4j.datautils.VectorOperations;

public class PassiveAggressiveAnomalyDetector extends PassiveAggressiveModel {

    /**
     * Learned "tolerance" parameter or radius. Maximum discrepancy allowed when making predictions.
     */
    public double epsilon = 0.0;

    /**
     * Upper bound on epsilon.
     */
    public double B;

    /**
     * Constructor of anomaly detector model. Inherited from PassiveAggressiveModel parent class.
     * @param algorithmTypeName Short name of learning strategy. It can be "PA", "PA_1" or "PA_2".
     * @param C Aggressiveness parameter. It's used only when the algorithm type is "PA_1" or "PA_2".
     * @param numberOfFeatures Number of features present in the model. It's necessary to initialize the weights vector.
     * @param B Upper bound on epsilon, which is the maximum discrepancy allowed when making predictions.
     */
    public PassiveAggressiveAnomalyDetector(String algorithmTypeName, double C, int numberOfFeatures, double B) {
        super(algorithmTypeName, C, numberOfFeatures + 1);
        this.weights[numberOfFeatures] = B;
        this.setB(B);
    }

    /**
     * Setter for B
     * @param B Real positive value representing upper bound on epsilon.
     */
    private void setB(double B) {
        if (B < 0.0) {
            throw new IllegalArgumentException("B cannot be negative");
        } else {
            this.B = B;
        }
    }

    /**
     * Score for the given input observation. It equals the distance between the "normal" vector w and the given instance.
     * @param x Input observation, feature vector.
     * @return Score.
     */
    @Override
    public double calculateScore(double[] x) {
        return VectorOperations.l2Norm(VectorOperations.subtract(this.weights, x));
    }

    /**
     * Classification rule to decide the label of an observation. It works similar to binary classification, we just discriminate
     * between normal and abnormal observations.
     * @param score Precalculated raw score used for prediction.
     * @return Predicted label associated with the score, which in turn is associated with a given data observation.
     */
    @Override
    public double predict(double score) {
        return Math.signum(score - this.epsilon);
    }

    /**
     * Calculates the hinge loss value.
     * @param score Score given by the model to a given input.
     * @param y Target value associated with the input observation.
     * @return Loss value.
     */
    @Override
    protected double sufferLoss(double score, double y) {
        return Math.max( 0.0, score - this.B);
    }

    /**
     * Method to calculate tau. In uniclass prediction (anomaly detection), the squared norm is just 1.
     * @param loss Precalculated loss.
     * @param x Instance.
     * @return The value of tau.
     */
    @Override
    protected double calculateTau(double loss, double[] x) {
        double squaredNorm = 1.0;
        return this.tauCalculator.calculate(loss, squaredNorm);
    }

    /**
     * Method to get the direction vector that is added to the weights to update the model.
     * @param score Score given by the model to a given input.
     * @param x Input observation, feature vector.
     * @param y Target value associated with the input observation. It can be a label or a quantity.
     * @return Update vector.
     */
    @Override
    protected double[] calculateUpdateVector(double score, double[] x, double y) {
//        double k = 1.0 / VectorOperations.l2Norm(VectorOperations.subtract(x, this.weights));
        double k = 1.0 / score;
        double[] u = VectorOperations.subtract(x, this.weights);
        return VectorOperations.scalarMultiplication(k, u);
    }

    /**
     * Method to update the weights vector and the parameter epsilon in each iteration.
     * Implementation based on Crammer et al. (2006).
     * @param x Instance.
     * @param y Target variable. This meaning applies only in the classification and regression setting.
     */
    @Override
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
