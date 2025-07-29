package pal4j.datautils;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EvaluationMetricTest {

    @Test
    @Order(1)
    @Tag("ClassificationMetrics")
    @DisplayName("Classification metrics")
    public void checkClassificationMetrics() {
        var y = new double[] {1. ,1., 1., -1., -1., 1., -1., -1., -1., 1. ,1., -1., 1., -1., 1., -1., -1., 1., -1., 1.};
        var predictions = new double [] {1. ,1., 1., -1., -1., 1., -1., -1., -1., 1. ,1., -1., 1., -1., 1., -1., -1., 1., -1., 1.};
        var accuracy = new Accuracy();
        var sensitivity = new Sensitivity();
        var specificity = new Specificity();

        for (int i = 0; i < y.length; i++) {
            accuracy.accumulate(y[i], predictions[i]);
            sensitivity.accumulate(y[i], predictions[i]);
            specificity.accumulate(y[i], predictions[i]);
        }
        accuracy.reduce();
        sensitivity.reduce();
        specificity.reduce();

        assertEquals(1., accuracy.getFinalScore());
        assertEquals(1., sensitivity.getFinalScore());
        assertEquals(1., specificity.getFinalScore());
    }

    @Test
    @Order(1)
    @Tag("ClassificationMetrics")
    @DisplayName("Classification metrics")
    public void checkRegressionMetrics() {
        var y = new double[] {0.62, 2.04, 8.85, -0.34, 5.97, 3.46, 5.61, 8.16, -2.32, -1.27, 8.71, -7.05, 3.97, -1.03, 0.11, 3.2, 6.42, 5.38, 7.52, -5.54};
        var predictions = new double [] {9.41, 1.68, 2.56, -6.53, -3.93, -5.51, 7.46, -7.27, -7.37, -4.79, -6.56, -4.66, 7.24, -6.83, -6.23, 5.9, -0.61, -3.27, 4.92, -9.98};
        var mae = new MAE();
        var mape = new MAPE();

        for (int i = 0; i < y.length; i++) {
            mae.accumulate(y[i], predictions[i]);
            mape.accumulate(y[i], predictions[i]);
        }
        mae.reduce();
        mape.reduce();

        assertEquals(6.242, mae.getFinalScore(), 0.001);
        assertEquals(278.5792, mape.getFinalScore(), 0.0001);
    }
}
