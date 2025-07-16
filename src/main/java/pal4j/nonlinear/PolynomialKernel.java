package pal4j.nonlinear;

import pal4j.datautils.VectorOperations;

public class PolynomialKernel implements MercerKernel {

    private final int D;
    private final int R;

    public PolynomialKernel(int d) {
        this.D = d;
        this.R = 0;
    }

    public PolynomialKernel(int d, int r) {
        this.D = d;
        this.R = r;
    }

    @Override
    public double transform(double[] x1, double[] x2) {
        return Math.pow( VectorOperations.dotProduct(x1, x2) + this.R, this.D );
    }

}
