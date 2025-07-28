/**
 * Class implementing the accuracy metric.
 * @author Diego Hernández Jiménez
 */
package pal4j.datautils;

public class Accuracy extends EvaluationMetric {

    public Accuracy() {}

    /**
     * Accumulation step. Adds one unit to the global store if the prediction is correct and updates the count of observations.
     * @param y Target label.
     * @param prediction Predicted label.
     */
    @Override
    public void accumulate(double y, double prediction) {
        if (y == prediction) ++this.metricCumSum;
        ++this.totalObservations;
    }

    @Override
    public String getName() {
        return "Accuracy";
    }
}
