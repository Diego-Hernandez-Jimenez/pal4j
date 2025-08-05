package pal4j.datautils;

import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BufferedDatasetTest {
    @Test
    @Order(1)
    @Tag("CSVDelimiter")
    @DisplayName("Appropiate field separator")
    public void checkDelimiter() {
        try {
            var data = new BufferedDataset(
                    "C:\\Users\\Diego\\javaprojects\\PassiveAggresive\\src\\test\\resources\\test_data.csv",
                    ";",
                    new String[] {"x0", "x5", "x10"},
                    "x18"
            );
        } catch (Exception e) {
            assertEquals("The chosen delimiter does not match the one used in the file", e.getMessage());
        }

    }

    @Test
    @Order(2)
    @Tag("ColumnIds")
    @DisplayName("Column indices")
    public void checkIds() {
        var data = new BufferedDataset(
                "C:\\Users\\Diego\\javaprojects\\PassiveAggresive\\src\\test\\resources\\test_data.csv",
                ",",
                new String[] {"x0", "x5", "x10"},
                "x18"
        );
        assertEquals(
                Arrays.toString(new int[] {0, 5, 10}),
                Arrays.toString(data.getFeatureIds())
        );
        assertEquals(18, data.getTargetId());
    }

}
