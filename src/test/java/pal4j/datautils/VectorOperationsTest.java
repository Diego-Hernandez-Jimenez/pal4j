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
        var u = new double[]{1., 2., 2.};
        var v = new double[]{0., 1., 4.};

        try {
//            var result = VectorOperations.scalarMultiplication(k, u);
//            var solution = new double[]{4., 8., 8.};
            assertEquals(
                    Arrays.toString(VectorOperations.scalarMultiplication(k, u)),
                    Arrays.toString(new double[]{4., 8., 8.})
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
        var u = new double[]{3., 2., 0., -1., -1., 1.};
        try {
            assertEquals(VectorOperations.l2Norm(u), 4.);

        } catch (Exception e) {
            fail("Error in the l2 norm: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    @Tag("Subtraction")
    @DisplayName("Vector subtraction")
    public void checkSubtract() {
        var u = new double[]{3., 2., 0., -1., -1., 1.};
        var v = new double[]{10., 2., -3., -3., 1., 5.};
        try {
            assertEquals(
                    Arrays.toString(VectorOperations.subtract(u, v)),
                    Arrays.toString(new double[] {-7., 0., 3., 2., -2, -4.})
            );
            assertEquals(
                    Arrays.toString(VectorOperations.subtract(v, u)),
                    Arrays.toString(new double[] {7., 0., -3., -2., 2, 4.})
            );

        } catch (Exception e) {
            fail("Error in the vector subtraction: " + e.getMessage());
        }
    }
}
