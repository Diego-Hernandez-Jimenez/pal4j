/**
 * Class implementing Mean Absolute Percentage Error metric.
 * @author Diego Hernández Jiménez
 */
package pal4j.datautils;

public class MAPE extends EvaluationMetric{


    public MAPE() {}

    /**
     * Accumulation step. Adds the absolute proportional difference between the prediction and the target and updates the count of observations.
     * @param y Target value.
     * @param prediction Predicted value.
     */
    @Override
    public void accumulate(double y, double prediction) {
        this.metricCumSum += 100 * Math.abs(y - prediction) / y;
        ++this.totalObservations;
    }

    @Override
    public String getName() {
        return "MAPE";
    }

}
