/**
 * Interface to represent Mercer kernels
 * @author Diego Hernández Jiménez
 */
package pal4j.nonlinear;


public interface MercerKernel {

    /**
     * Computes the kernel function value (similarity) between two input vectors.
     *
     * @param x1 First input vector.
     * @param x2 Second input vector.
     * @return A double representing the similarity between x1 and x2 according to the kernel function.
     */
    double transform(double[] x1, double[] x2);

    /**
     * Method to get kernel's name.
     * @return Name of the kernel.
     */
    String getKernelName();
}
