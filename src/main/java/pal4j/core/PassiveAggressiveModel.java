package pal4j.core;


import pal4j.datautils.VectorOperations;

public abstract class PassiveAggressiveModel {

    public final AlgorithmType ALGORITHM_TYPE;
    private double C;
    public double[] weights;
    protected TauCalculator tauCalculator;

    public PassiveAggressiveModel(String algorithmTypeName, double C, int numberOfFeatures) {
        this.ALGORITHM_TYPE = AlgorithmType.valueOf(algorithmTypeName);
        this.setC(C);
        this.setWeights(numberOfFeatures);
        this.setTauCalculator();
    }

    public abstract double calculateScore(double[] x);
    public abstract double predict(double score);
    protected abstract double sufferLoss(double score, double y);
    protected abstract double[] calculateUpdateVector(double score, double[] x, double y);


    public void setC(double C) {
        if (C < 0.0) {
            throw new IllegalArgumentException("C cannot be negative");
        } else if (this.ALGORITHM_TYPE.equals(AlgorithmType.PA)) {
            System.out.println("C parameter will be ignored");
            this.C = 0.0;
        }else {
            this.C = C;
        }
    }

    public double getC() {
        return this.C;
    }

    private void setWeights(int numberOfFeatures) {
        if (numberOfFeatures <= 0) {
            throw new IllegalArgumentException("number of features cannot be less or equal to 0");
        } else {
            this.weights = new double[numberOfFeatures];
        }
    }


//    public double[] getWeights() {
//        return this.weights;
//    }

    protected void setTauCalculator() {
        switch (this.ALGORITHM_TYPE) {
            case PA -> this.tauCalculator = (loss, squaredNorm) -> loss / squaredNorm;
            case PA_1 -> this.tauCalculator = (loss, squaredNorm) -> Math.min( this.C, loss / squaredNorm );
            case PA_2 -> this.tauCalculator = (loss, squaredNorm) -> Math.min( this.C, loss / ( squaredNorm + 1/(2 * this.C) ) );
        }
    }

    protected double calculateTau(double loss, double[] x) {
        double squaredNorm = VectorOperations.dotProduct(x, x); // Math.pow(VectorOperations.l2Norm(x), 2);
        return this.tauCalculator.calculate(loss, squaredNorm);
    }


    public void fit(double[] x, double y) {
        double score = this.calculateScore(x);
        double loss = this.sufferLoss(score, y);
        double tau = this.calculateTau(loss, x);
        double[] updateVector = this.calculateUpdateVector(score, x, y);
        for (int i = 0; i < x.length; i++) {
            this.weights[i] += tau * updateVector[i];
        }
    }


}
