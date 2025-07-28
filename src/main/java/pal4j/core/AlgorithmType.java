/**
 * Enum with the possible types of Passive-Aggressive algorithms. They basically differ in the way the value of tau is calculated.
 * See Crammer et al. (2006) for more details.
 */
package pal4j.core;

public enum AlgorithmType {

    /**
     * Original passive-aggressive method to calculate the value of tau.
     */
    PA("PA"),

    /**
     * PA-I method to calculate the value of tau. It is derived from a modified PA objective function that includes a slack term
     * and an aggressiveness parameter that controls its influence on the objective function.
     */
    PA_1("PA-I"),

    /**
     * PA-II method to calculate the value of tau. It is derived from a modified PA objective function that includes a quadratic slack term
     * and an aggressiveness parameter that controls its influence on the objective function.
     */
    PA_2("PA-II");

    public final String name;

    AlgorithmType(String name) {
        this.name = name;
    }

}

