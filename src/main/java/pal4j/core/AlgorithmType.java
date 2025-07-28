/**
 * Enum with the possible types of Passive-Aggressive algorithms. They basically differ in the way the value of tau is calculated.
 * See Crammer et al. (2006) for more details.
 */
package pal4j.core;

public enum AlgorithmType {
    PA("PA"),
    PA_1("PA-I"),
    PA_2("PA-II");

    public final String name;

    AlgorithmType(String name) {
        this.name = name;
    }

}

