package pal4j.datautils;

public class MAE extends EvaluationMetric{

    public MAE() {}

    @Override
    public void accumulate(double y, double prediction) {
        this.metricCumSum += Math.abs(y - prediction);
        ++this.totalObservations;
    }

    public void accumulate(double[] y, double[] prediction) {
        int len = y.length;
        double out = 0.0;
        for (int i = 0; i < len; i++) out += Math.abs(y[i] - prediction[i]);

        this.metricCumSum += out / len;
        ++this.totalObservations;
    }
}
