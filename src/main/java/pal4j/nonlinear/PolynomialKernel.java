/**
 * Class implementing polynomial kernel.
 * @author Diego Hernández Jiménez
 */
package pal4j.nonlinear;

import pal4j.datautils.VectorOperations;

public class PolynomialKernel implements MercerKernel {

    /**
     * Degree of the polynomial
     */
    private final int D;

    /**
     * Intercept of the polynomial function.
     */
    private final int R;

    public PolynomialKernel(int d) {
        this.D = d;
        this.R = 0;
    }

    public PolynomialKernel(int d, int r) {
        this.D = d;
        this.R = r;
    }

    /**
     * Computes the polynomial kernel value (similarity) between two input vectors.
     *
     * The polynomial kernel is computed using the formula:
     *
     *     K(x1, x2) = (x1*x1 + R)^D
     *
     * @param x1 First input vector.
     * @param x2 Second input vector.
     * @return The computed kernel value, representing the similarity between the input vectors.
     */
    @Override
    public double transform(double[] x1, double[] x2) {
        return Math.pow( VectorOperations.dotProduct(x1, x2) + this.R, this.D );
    }

    @Override
    public String getKernelName() {
        return "Polynomial";
    }

}
