package pal4j.datautils;

import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VectorOperationsTest {

    @Test
    @Order(1)
    @Tag("Scalar Multiplication")
    @DisplayName("Scalar - vector multiplication")
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
}
