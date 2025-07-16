package pal4j.datautils;

public class Sensitivity extends EvaluationMetric {

    public Sensitivity() {};

    @Override
    public void accumulate(double y, double prediction) {
        if (y == 1) {
            ++this.totalObservations;
            if (y == prediction) {
                ++this.metricCumSum;
            }
        }
    }
}
