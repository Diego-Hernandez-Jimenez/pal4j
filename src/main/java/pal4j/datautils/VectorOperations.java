package pal4j.datautils;

//import java.util.Arrays;
//import java.util.stream.IntStream;

public class VectorOperations {

    public static double l2Norm(double[] u) {
        int sizeU = u.length;
        if (sizeU == 0) {
            return 0.0;
        } else {
//            double cumSumSquares = Arrays.stream(u).map(vector -> Math.pow(vector, 2)).sum();
            double cumSumSquares = 0.0;
            for (double entry : u) cumSumSquares += Math.pow(entry, 2);
            return Math.sqrt(cumSumSquares);
        }

    }
    public static double dotProduct(double[] u, double[] v) {
        int sizeU = u.length;
        int sizeV = v.length;
        if (sizeU != sizeV) {
            throw new IllegalArgumentException("Mismatch of vector sizes: " + sizeU + " vs " + sizeV);
        } else {
//            return IntStream.range(0, sizeU).mapToDouble(i -> u[i] * v[i]).sum();
            double cumSumProd = 0.0;
            for (int i = 0; i < sizeU; i++) cumSumProd += u[i] * v[i];
            return cumSumProd;
        }
    }

    public static double[] scalarMultiplication(Number k, double[] u) {
        double scalar = k.doubleValue();
//        return Arrays.stream(u).map(vector -> scalar * vector).toArray();
        for (int i = 0; i < u.length; i++) u[i] = scalar * u[i];
        return u;
    }

    public static double[] sum(double[] u, double[] v) {
        double[] out = new double[u.length];
        for (int i = 0; i < u.length; i++) out[i] = u[i] + v[i];
        return out;
    }

    public static double[] subtract(double[] u, double[] v) {
        double[] out = new double[u.length];
        for (int i = 0; i < u.length; i++) out[i] = u[i] - v[i];
        return out;
    }
}
