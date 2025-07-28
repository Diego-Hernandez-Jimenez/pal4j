/**
 * Functional interface for calculators of tau.
 * @author Diego Hernández Jiménez
 */
package pal4j.core;

@FunctionalInterface
public interface TauCalculator {

    /**
     * Method to obtain the value of tau, which corresponds to the Lagrange multiplier.
     * @param loss Precalculated loss of a given model for a given input.
     * @param squaredNorm Quantity representing the squared norm of some vector. Its value depends on the model.
     * @return The value of tau.
     */
    double calculate(double loss, double squaredNorm);
}
