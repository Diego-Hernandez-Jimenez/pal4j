/**
 * Class providing methods for common vector operations.
 * @author Diego Hernández Jiménez
 */
package pal4j.datautils;

//import java.util.Arrays;
//import java.util.stream.IntStream;

public class VectorOperations {

    /**
     * Implements l^2 vector norm, ||·||.
     * @param u Input vector.
     * @return The real value corresponding to ||u||.
     */
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

    /**
     * Implements vector dot product.
     * @param u First input vector.
     * @param v Second input vector.
     * @return The inner product of u and v.
     */
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

    /**
     * Implements scalar multiplication, the product of real valued number and a vector
     * @param k Real positive number.
     * @param u Input vector.
     * @return The result of the multiplication k * u.
     */
    public static double[] scalarMultiplication(Number k, double[] u) {
        double scalar = k.doubleValue();
//        return Arrays.stream(u).map(vector -> scalar * vector).toArray();
        for (int i = 0; i < u.length; i++) u[i] = scalar * u[i];
        return u;
    }

//    public static double[] sum(double[] u, double[] v) {
//        double[] out = new double[u.length];
//        for (int i = 0; i < u.length; i++) out[i] = u[i] + v[i];
//        return out;
//    }

    /**
     * Implements subtraction of vectors.
     * @param u First input vector, minuend.
     * @param v Second input vector, subtrahend.
     * @return The result of the subtraction.
     */
    public static double[] subtract(double[] u, double[] v) {
        double[] out = new double[u.length];
        for (int i = 0; i < u.length; i++) out[i] = u[i] - v[i];
        return out;
    }
}
