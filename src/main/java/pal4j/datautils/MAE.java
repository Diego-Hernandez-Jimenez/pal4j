/**
 * Class implementing Mean Absolute Error metric.
 * @author Diego Hernández Jiménez
 */
package pal4j.datautils;

public class MAE extends EvaluationMetric {

    public MAE() {}

    /**
     * Accumulation step. Adds the absolute difference between the prediction and the target and updates the count of observations.
     * @param y Target value.
     * @param prediction Predicted value.
     */
    @Override
    public void accumulate(double y, double prediction) {
        this.metricCumSum += Math.abs(y - prediction);
        ++this.totalObservations;
    }

    @Override
    public String getName() {
        return "MAE";
    }

}
