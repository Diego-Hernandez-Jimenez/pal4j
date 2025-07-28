/**
 * Concrete class for the binary nonlinear classification task with passive-aggressive optimization and kernel.
 * @author Diego Hernández Jiménez
 */

package pal4j.nonlinear;

import pal4j.core.PassiveAggressiveBinaryClassifier;
import pal4j.datautils.DataRecord;

import java.util.ArrayList;
import java.util.List;

public class KernelPassiveAggressiveBinaryClassifier extends PassiveAggressiveBinaryClassifier {

    /**
     * Mercer kernel for computing similarity. It allows nonlinear classification.
     */
    private final MercerKernel KERNEL;

    /**
     * Tau values associated with each observation. This array grows with training.
     */
    public List<Double> taus;

    /**
     * List of observations encountered during training.
     */
    public List<DataRecord> observations;

    /**
     * Constructor for kernel PA binary classification model. Inherited from PassiveAggressiveBinaryClassifier parent class.
     * @param algorithmTypeName Name of the algorithm type.
     * @param C Aggressiveness parameter. It's used only when the algorithm type is "PA_1" or "PA_2".
     * @param numberOfFeatures Number of features present in the model. It's necessary to initialize the weights vector.
     * @param kernel Mercer kernel for computing similarity.
     */
    public KernelPassiveAggressiveBinaryClassifier(
            String algorithmTypeName,
            double C,
            int numberOfFeatures,
            MercerKernel kernel
    ) {
        super(algorithmTypeName, C, numberOfFeatures);
        this.KERNEL = kernel;
        this.taus = new ArrayList<>();
        this.observations = new ArrayList<>();
    }


    /**
     * Score for the given input observation. It's calculated as a linear combination of training instances
     * @param x Input observation, feature vector.
     * @return Score.
     */
    @Override
    public double calculateScore(double[] x) {
        double score = 0.0;
        for (int i = 0; i < this.observations.size(); ++i) {
            DataRecord observation = this.observations.get(i);
            score += this.taus.get(i) * observation.y() *  this.KERNEL.transform(observation.x(), x);
        }
        return score;
    }

    /**
     * Method to calculate tau. The squared is still equivalent to the inner product of x with itself, but this time in kernel space.
     * @param loss Precalculated loss.
     * @param x Instance.
     * @return The value of tau.
     */
    @Override
    protected double calculateTau(double loss, double[] x) {
        double squaredNorm = this.KERNEL.transform(x, x);
        return this.tauCalculator.calculate(loss, squaredNorm);
    }

    /**
     * Method to update the model in each iteration. The weights vector is ignored in this context, learning is possible by
     * storing the data about the present observation (x and y) and the calculated tau value.
     * @param x Instance.
     * @param y Target label.
     */
    @Override
    public void fit(double[] x, double y) {
        double score = this.calculateScore(x);
        double loss = this.sufferLoss(score, y);
        this.observations.add(new DataRecord(x, y));
        this.taus.add(this.calculateTau(loss, x));
    }


    /**
     * Displays basic information about the instantiated model.
     * @return Message with metadata about the model.
     */
    @Override
    public String toString() {

        return String.format(
                "Task: Regression\nPassive Aggressive model type: %s.\nAggressiveness parameter C: %.2f\n kernel:%s",
                this.ALGORITHM_TYPE.name, this.getC(), this.KERNEL.getKernelName()
        );
    }


}
