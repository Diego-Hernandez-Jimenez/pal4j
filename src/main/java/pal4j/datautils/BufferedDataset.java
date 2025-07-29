/**
 * A dataset class designed for online learning scenarios, where the entire dataset is not loaded into memory.
 * @author Diego Hernández Jiménez
 */

package pal4j.datautils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferedDataset {

    /**
     * Path to dataset.
     */
    public final String FILE_PATH;

    /**
     * Field delimiter in the file, usually "," or ";".
     */
    public final String SEPARATOR;

    /**
     * Names of predictors or, more generally, features to learn from.
     */
    public String[] FEATURE_NAMES;

    /**
     * Name of target variable, used only in supervised setting, but not in anomaly detection training.
     */
    public String TARGET_NAME;

    /**
     * Column indices of the features in the dataset.
     */
    private int[] featureIds;

    /**
     * Column index of the target variable.
     */
    private int targetId;

    /**
     * Constructor for the supervised setting where the user provides both the feature names and the target name.
     * @param filePath Path to text file.
     * @param separator Field delimiter in the file.
     * @param featureNames Names of the features to use as predictors.
     * @param targetName Name of the target variable.
     */
    public BufferedDataset(String filePath, String separator, String[] featureNames, String targetName) {
        this.FILE_PATH = filePath;
        this.SEPARATOR = separator;
        setColumnsMetadata(featureNames, targetName);
    }

    /**
     * Constructor for the supervised setting where the user provides only the target name.
     * It is assumed that the user wants the remaining fields in the file to be used as features.
     * @param filePath Path to text file.
     * @param separator Field delimiter in the file.
     * @param targetName Name of the target variable.
     */
    public BufferedDataset(String filePath, String separator, String targetName) {
        this.FILE_PATH = filePath;
        this.SEPARATOR = separator;
        setColumnsMetadata(targetName);
    }

    /**
     * Method to get indices from column names (features and targets). Both the names and the indices are stored in arrays.
     * @param featureNames Names of the features to use as predictors.
     * @param targetName Name of the target variable.
     */
    private void setColumnsMetadata(String[] featureNames, String targetName) {

        try (BufferedReader br = new BufferedReader(new FileReader(this.FILE_PATH))) {
            String header = br.readLine().replace(" ", "");
            String[] columnNames = header.split(this.SEPARATOR);
            if (columnNames.length == 1) {
                throw new IllegalArgumentException("The chosen delimiter does not match the one used in the file");
            }
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

    /**
     * Method to get indices from column names (features and targets). Both the names and the indices are stored in arrays.
     * Feature names are not provided, it is assumed that all columns except targetName are feautes.
     * @param targetName Name of the target variable.
     */
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
