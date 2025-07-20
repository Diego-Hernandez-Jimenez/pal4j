package pal4j.datautils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferedDataset {
    public final String FILE_PATH;
    public final String SEPARATOR;
    public String[] FEATURE_NAMES;
    public String TARGET_NAME;
    private int[] featureIds;
    private int targetId;

    public BufferedDataset(String filePath, String separator, String[] featureNames, String targetName) {
        this.FILE_PATH = filePath;
        this.SEPARATOR = separator;
        setColumnsMetadata(featureNames, targetName);
    }

    public BufferedDataset(String filePath, String separator, String targetName) {
        this.FILE_PATH = filePath;
        this.SEPARATOR = separator;
        setColumnsMetadata(targetName);
    }


    private void setColumnsMetadata(String[] featureNames, String targetName) {

        try (BufferedReader br = new BufferedReader(new FileReader(this.FILE_PATH))) {
            String header = br.readLine();
            String[] columnNames = header.split(this.SEPARATOR);
            int numberOfFeatures = featureNames.length;
            int[] featureIndices = new int[numberOfFeatures];
            int targetIndex = -1;
            for (int i = 0; i < columnNames.length; i++) {
                String columnName = columnNames[i];
                if (columnName.equals(targetName)) {
                    targetIndex = i;
                }
                for (int j = 0; j < numberOfFeatures; j++) {
                    if (columnName.equals(featureNames[j])) {
                        featureIndices[j] = i;
                    }
                }
            }

            if (targetIndex == -1) {
                throw new IllegalArgumentException("At least one feature or the target variable is not present in the data");
            }
            this.featureIds = featureIndices;
            this.targetId = targetIndex;
            this.FEATURE_NAMES = featureNames;
            this.TARGET_NAME = targetName;

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }

    }

    private void setColumnsMetadata(String targetName) {

        try (BufferedReader br = new BufferedReader(new FileReader(this.FILE_PATH))) {
            String header = br.readLine();
            String[] columnNames = header.split(this.SEPARATOR);
            int numberOfFeatures = columnNames.length - 1;
            this.FEATURE_NAMES = new String[numberOfFeatures];
            int[] featureIndices = new int[numberOfFeatures];
            int targetIndex = -1;
            int offset = 0;
            for (int i = 0; i < columnNames.length; i++) {
                String columnName = columnNames[i];
                if (columnName.equals(targetName)) {
                    targetIndex = i;
                    offset = -1;
                }
                else {
                    featureIndices[i + offset] = i;
                    this.FEATURE_NAMES[i + offset] = columnName;
                }

            }

            if (targetIndex == -1) {
                throw new IllegalArgumentException("At least one feature or the target variable is not present in the data");
            }
            this.featureIds = featureIndices;
            this.targetId = targetIndex;
            this.TARGET_NAME = targetName;

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }

    }



    public int[] getFeatureIds() {
        return this.featureIds;
    }

    public int getTargetId() {
        return this.targetId;
    }

}
