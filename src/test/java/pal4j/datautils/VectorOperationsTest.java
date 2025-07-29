package pal4j.datautils;

import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VectorOperationsTest {

    @Test
    @Order(1)
    @Tag("ScalarMultiplication")
    @DisplayName("Scalar-vector multiplication")
    public void checkScalarProduct() {
        var k = 4;
        var u = new double[] {1., 2., 2.};

        try {
            assertEquals(
                    Arrays.toString(new double[]{4., 8., 8.}),
                    Arrays.toString(VectorOperations.scalarMultiplication(k, u))
            );

        } catch (Exception e) {
            fail("Error in the scalar product: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    @Tag("L2Norm")
    @DisplayName("L2 Norm")
    public void checkL2Norm() {
        var u = new double[] {3., 2., 0., -1., -1., 1.};
        try {
            assertEquals(4., VectorOperations.l2Norm(u));

        } catch (Exception e) {
            fail("Error in the l2 norm: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    @Tag("Subtraction")
    @DisplayName("Vector subtraction")
    public void checkSubtract() {
        var u = new double[] {3., 2., 0., -1., -1., 1.};
        var v = new double[] {10., 2., -3., -3., 1., 5.};
        try {
            assertEquals(
                    Arrays.toString(new double[] {-7., 0., 3., 2., -2, -4.}),
                    Arrays.toString(VectorOperations.subtract(u, v))
            );
            assertEquals(
                    Arrays.toString(new double[] {7., 0., -3., -2., 2, 4.}),
                    Arrays.toString(VectorOperations.subtract(v, u))
            );

        } catch (Exception e) {
            fail("Error in the vector subtraction: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    @Tag("DotProduct")
    @DisplayName("Inner/dot product")
    public void checkDotProduct() {
        var u = new double[] {3., 2., 0., -1., -1., 1.};
        var v = new double[] {10., 2., -3., -3., 1., 5.};
        var z = new double [] {1., 3.};

        try {
            assertEquals(41.0, VectorOperations.dotProduct(u, v));

        } catch (Exception e) {
            fail("Error in the vector subtraction: " + e.getMessage());
        }

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {VectorOperations.dotProduct(u, z);});
        assertEquals("Mismatch of vector sizes: 6 vs 2", exception.getMessage());

    }
}
