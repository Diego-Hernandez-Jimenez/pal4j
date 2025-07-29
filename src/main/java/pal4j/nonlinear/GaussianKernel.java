/**
 * Class implementing Gaussian kernel.
 * @author Diego Hernández Jiménez
 */
package pal4j.nonlinear;

import pal4j.datautils.VectorOperations;

public class GaussianKernel implements MercerKernel {

    /**
     * The gamma parameter that controls the width of the Gaussian kernel.
     */
    private final double GAMMA;

    public GaussianKernel(double gamma) {
        this.GAMMA = gamma;
    }

    /**
     * Computes the Gaussian kernel value (similarity) between two input vectors.
     * The Gaussian kernel is computed using the formula:
     *     K(x1, x2) = exp(-γ * ||x1 - x2||^2)
     * @param x1 First input vector.
     * @param x2 Second input vector.
     * @return The computed kernel value, representing the similarity between the input vectors.
     */
    @Override
    public double transform(double[] x1, double[] x2) {
        double[] vectorDifference = VectorOperations.subtract(x1, x2);
        return Math.exp( -this.GAMMA * VectorOperations.dotProduct(vectorDifference, vectorDifference) );
    }

    @Override
    public String getKernelName() {
        return "Gaussian";
    }


}
