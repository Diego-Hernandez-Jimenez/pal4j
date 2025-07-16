package pal4j.nonlinear;

import pal4j.datautils.VectorOperations;

public class GaussianKernel implements MercerKernel {

    private final double GAMMA;

    public GaussianKernel(double gamma) {
        this.GAMMA = gamma;
    }

    @Override
    public double transform(double[] x1, double[] x2) {
        double[] vectorDifference = new double[x1.length];
        for (int i = 0; i < vectorDifference.length; i++) {
            vectorDifference[i] = x1[i] - x2[i];
        }
        return Math.exp( -this.GAMMA * Math.pow(VectorOperations.l2Norm(vectorDifference), 2) );
    }
}
