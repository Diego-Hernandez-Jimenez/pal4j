package pal4j.core;

@FunctionalInterface
public interface TauCalculator {
    double calculate(double loss, double squaredNorm);
}
