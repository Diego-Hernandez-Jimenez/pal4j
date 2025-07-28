/**
 * Class implementing specificity metric.
 * @author Diego Hernández Jiménez
 */
package pal4j.datautils;

public class Specificity extends EvaluationMetric {

    public Specificity() {};

    /**
     * Accumulation step. Adds one unit to the global store if the prediction is negative (label = -1) and correct and updates the count of observations.
     * @param y Target label.
     * @param prediction Predicted label.
     */
    @Override
    public void accumulate(double y, double prediction) {
        if (y == -1) {
            ++this.totalObservations;
            if (y == prediction) {
                ++this.metricCumSum;
            }
        }
    }

    @Override
    public String getName() {
        return "Specificity";
    }
}
