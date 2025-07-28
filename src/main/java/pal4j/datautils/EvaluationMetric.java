/**
 * Fundamental abstraction for performance measures or metrics.
 */

package pal4j.datautils;

public abstract class EvaluationMetric {

    protected double metricCumSum = 0.0;
    protected int totalObservations = 0;
    protected double finalScore = 0.0;

    public EvaluationMetric(){};

    /**
     * Accumulation step. For a given target and prediction, a performance score is obtained and added to the global score metricCumSum.
     * The performance is calculated differently depending on the metric.
     * @param y Target .
     * @param prediction Predicted value.
     */
    public abstract void accumulate(double y, double prediction);

    /**
     * Reduction step. The global score is divided by the number of seen observations to get a relative score (average).
     */
    public void reduce() {
        this.finalScore = this.metricCumSum / this.totalObservations;
    }

    /**
     * Method to reinitialize the parameters.
     */
    public void reset() {
        this.metricCumSum = 0.0;
        this.totalObservations = 0;
        this.finalScore = 0.0;
    }

    public abstract String getName();

    public double getMetricCumSum() {
        return this.metricCumSum;
    }

    public int getTotalObservations() {
        return this.totalObservations;
    }

    public double getFinalScore() {
        return this.finalScore;
    }
}
