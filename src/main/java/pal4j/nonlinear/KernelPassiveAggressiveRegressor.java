package pal4j.nonlinear;

import pal4j.core.PassiveAggressiveRegressor;
import pal4j.datautils.DataRecord;

import java.util.ArrayList;
import java.util.List;

public class KernelPassiveAggressiveRegressor extends PassiveAggressiveRegressor {

    private MercerKernel kernel;
    public ArrayList<Double> taus;
    public List<DataRecord> observations;
    public ArrayList<Double> scores;

    public KernelPassiveAggressiveRegressor(
            String algorithmTypeName,
            double C,
            int numberOfFeatures,
            double epsilon,
            MercerKernel kernel
    ) {
        super(algorithmTypeName, C, numberOfFeatures, epsilon);
        this.kernel = kernel;
        this.taus = new ArrayList<>();
        this.observations = new ArrayList<>();
        this.scores = new ArrayList<>();
    }


    @Override
    public double calculateScore(double[] x) {
        double score = 0.0;
        for (int i = 0; i < this.observations.size(); ++i) {
            DataRecord observation = this.observations.get(i);
            score += Math.signum(observation.y() - this.scores.get(i)) * this.taus.get(i) *  this.kernel.transform(observation.x(), x);
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
//        System.out.println(score);
        double loss = this.sufferLoss(score, y);
//        System.out.println(loss);
        this.observations.add(new DataRecord(x, y));
        this.taus.add(this.calculateTau(loss, x));
        this.scores.add(score);
    }

    @Override
    public String toString() {

        return String.format(
                "Task: Regression\nPassive Aggressive model type: %s.\nAggressiveness parameter C: %.2f\nEpsilon: %.2f",
                this.ALGORITHM_TYPE.name, this.getC(), this.getEpsilon()
        );
    }

}
