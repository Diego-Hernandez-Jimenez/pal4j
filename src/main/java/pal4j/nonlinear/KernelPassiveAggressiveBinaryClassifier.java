package pal4j.nonlinear;

import pal4j.core.PassiveAggressiveBinaryClassifier;
import pal4j.datautils.DataRecord;

import java.util.ArrayList;
import java.util.List;

public class KernelPassiveAggressiveBinaryClassifier extends PassiveAggressiveBinaryClassifier {

    private MercerKernel kernel;
    public ArrayList<Double> taus;
    public List<DataRecord> observations;

    public KernelPassiveAggressiveBinaryClassifier(
            String algorithmTypeName,
            double C,
            int numberOfFeatures,
            MercerKernel kernel
    ) {
        super(algorithmTypeName, C, numberOfFeatures);
        this.kernel = kernel;
        this.taus = new ArrayList<Double>();
        this.observations = new ArrayList<DataRecord>();
    }


    @Override
    public double calculateScore(double[] x) {
        double score = 0.0;
        for (int i = 0; i < this.observations.size(); ++i) {
            DataRecord observation = this.observations.get(i);
            score += this.taus.get(i) * observation.y() *  this.kernel.transform(observation.x(), x);
        }
        return score;
    }

    @Override
    protected double calculateTau(double loss, double[] x) {
        double squaredNorm = this.kernel.transform(x, x);
        return this.tauCalculator.calculate(loss, squaredNorm);
    }

    @Override
    public void fit(double[] x, double y) {
        double score = this.calculateScore(x);
        double loss = this.sufferLoss(score, y);
        this.observations.add(new DataRecord(x, y));
        this.taus.add(this.calculateTau(loss, x));
    }

    @Override
    public String toString() {

        return String.format(
                "Task: Regression\nPassive Aggressive model type: %s.\nAggressiveness parameter C: %.2f",
                this.ALGORITHM_TYPE.name, this.getC()
        );
    }

}
