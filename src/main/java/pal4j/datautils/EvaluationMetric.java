package pal4j.datautils;

public abstract class EvaluationMetric {

    public double metricCumSum = 0.0;
    public int totalObservations = 0;
    public double finalScore = 0.0;

    public EvaluationMetric(){};

    public abstract void accumulate(double y, double prediction);

//    public double getMetricCumSum() {
//        return this.metricCumSum;
//    }
//
//    public int getTotalObservations() {
//        return this.totalObservations;
//    }
//
//    public double getFinalScore() {
//        return this.finalScore;
//    }
//
    public void reduce() {
        this.finalScore = this.metricCumSum / this.totalObservations;
    }

    public void reset() {
        this.metricCumSum = 0.0;
        this.totalObservations = 0;
        this.finalScore = 0.0;
    }
}
