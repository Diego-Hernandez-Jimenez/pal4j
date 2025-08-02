/**
 * Concrete class for the binary (linear) classification task with passive-aggressive optimization.
 * @author Diego Hernández Jiménez
 */

package pal4j.core;

import pal4j.datautils.VectorOperations;

public class PABinaryClassifier extends PassiveAggressiveModel {

    /**
     * Constructor for PA binary classification model. Inherited from PassiveAggressiveModel parent class.
     * @param algorithmTypeName Short name of learning strategy. It can be "PA", "PA_1" or "PA_2".
     * @param C Aggressiveness parameter. It's used only when the algorithm type is "PA_1" or "PA_2".
     * @param numberOfFeatures Number of features present in the model. It's necessary to initialize the weights vector.
     */
    public PABinaryClassifier(String algorithmTypeName, double C, int numberOfFeatures) {
        super(algorithmTypeName, C, numberOfFeatures);
    }

    /**
     * Score for the given input observation. It equals the margin.
     * @param x Input observation, feature vector.
     * @return Score, margin.
     */
    @Override
    public double calculateScore(double[] x) {
        return VectorOperations.dotProduct(this.weights, x);
    }

    /**
     * Classification rule to decide the label of an observation.
     * @param score Precalculated raw score used for prediction.
     * @return Predicted label associated with the score, which in turn is associated with a given data observation.
     */
    @Override
    public double predict(double score) {
        return Math.signum(score);
    }

    /**
     * Calculates the hinge loss value.
     * @param score Score given by the model to a given input.
     * @param y Label associated with the input observation.
     * @return Loss value.
     */
    @Override
    protected double sufferLoss(double score, double y) {
        return Math.max(0.0, 1 - y * score);
    }

    /**
     * Calculates direction vector used to update weights. Implementation is based on Crammer et al. (2006).
     * @param score Score given by the model to a given input.
     * @param x Input observation, feature vector.
     * @param y Label associated with the input observation.
     * @return Update vector.
     */
    @Override
    protected double[] calculateUpdateVector(double score, double[] x, double y) {
        return VectorOperations.scalarMultiplication(y, x);
    }

    /**
     * Displays basic information about the instantiated model.
     * @return Message with metadata about the model.
     */
    @Override
    public String toString() {

        return String.format(
                "Task: Binary classification\nPassive Aggressive model type: %s.\nAggressiveness parameter C: %.2f",
                this.ALGORITHM_TYPE.name, this.getC()
        );
    }


}
