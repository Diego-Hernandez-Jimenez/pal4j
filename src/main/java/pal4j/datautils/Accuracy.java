package pal4j.datautils;

public class Accuracy extends EvaluationMetric{

    public Accuracy() {}

    @Override
    public void accumulate(double y, double prediction) {
        if (y == prediction) ++this.metricCumSum;
        ++this.totalObservations;
    }
}
