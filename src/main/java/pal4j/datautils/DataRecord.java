/**
 * Record to store information of data observations, composed of a vector of features, x, and a target variable, y.
 */
package pal4j.datautils;

public record DataRecord(double[] x, double y) {}
