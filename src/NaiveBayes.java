
import java.util.ArrayList;

public abstract class NaiveBayes implements Classifier {

    private ArrayList<Instance> trainingData;
    public LiklihoodTable liklihoodTable;
    int numOfClassifications;
    int numberOfFeatures;
    private ArrayList<LiklihoodTable> lTables;
    public int[][] data;

    public double getProbabilityGivenClass(int[] featureColumn, int[] classColumn, int featureValue, int classValue) {
        ArrayList<Integer> limitedFeatureColumn = new ArrayList<>();

        //We take the values of the feature column that have a match in the class column for the desired class value
        for (int i = 0; i < classColumn.length; i++) {
            if (classColumn[i] == classValue) {
                limitedFeatureColumn.add(featureColumn[i]);
            }
        }

        //Take the sum of the items in the limitedFeatureColumn and divide by the total size for the probability
        int match = 0;
        for (int feature : limitedFeatureColumn) {
            if (feature == featureValue) {
                match++;
            }
        }
        return match / limitedFeatureColumn.size();
    }

    public double getProbabilityGivenClass(int[] featureColumn, int[] featureColumn2, int[] classColumn, int featureValue, int featureValue2, int classValue) {
        ArrayList<Integer> limitedFeatureColumn = new ArrayList<>();
        ArrayList<Integer> limitedFeatureColumn2 = new ArrayList<>();

        //We take the values of the feature column that have a match in the class column for the desired class value
        for (int i = 0; i < classColumn.length; i++) {
            if (classColumn[i] == classValue) {
                limitedFeatureColumn.add(featureColumn[i]);
                limitedFeatureColumn2.add(featureColumn2[i]);
            }
        }

        //Take the sum of the items in the limitedFeatureColumn and divide by the total size for the probability
        int match = 0;
        for (int i = 0; i < limitedFeatureColumn.size(); i++) {
            int feature = limitedFeatureColumn.get(i);
            int feature2 = limitedFeatureColumn2.get(i);
            if (feature == featureValue && feature2 == featureValue2) {
                match++;
            }
        }
        return match / limitedFeatureColumn.size();
    }

    public int[] getColumn(int index) {
        return new int[0];//TODO
    }

    public NaiveBayes(ArrayList<Instance> trainingData) {

        this.trainingData = trainingData;
        ArrayList<FrequencyTable> fTables = createFrequencyTables(trainingData);
        lTables = createLiklihoodTables(fTables);
    }

    //converts the training data into a more usable int array
    private int[][] convertTrainingDataToData(ArrayList<Instance> trainingData) {
        Instance sample = trainingData.get(0);
        numberOfFeatures = sample.features.size();
        int[][] data = new int[trainingData.size()][numberOfFeatures + 1];
        for (int i = 0; i < trainingData.size(); i++) {
            Instance instance = trainingData.get(i);
            for (int j = 0; j < numberOfFeatures; j++) {
                data[i][j] = instance.features.get(j);
            }
            //class is last element in the row
            data[i][numberOfFeatures] = instance.classification;
        }

        return data;
    }

    //Creates a series of tables for each attribute and maps the frequencys of 
    //each classification to each attribute.
    //We assume that int[][] data is columns then rows, and that the last column is the classifications
    private ArrayList<FrequencyTable> createFrequencyTables(ArrayList<Instance> trainingData) {
        ArrayList<FrequencyTable> tables = new ArrayList<>();
        numOfClassifications = Utilities.getClassificationCount(trainingData);
        data = convertTrainingDataToData(trainingData);

        for (int i = 0; i < data.length - 1; i++) {
            tables.add(new FrequencyTable(data[i], data[data.length - 1], numOfClassifications, i));
        }
        return tables;
    }

    public int getDistinctValueCount(int[] column) {

        return 1;//TODO
    }

    //for each frequencyTable we want to calculate P(x|c), ie the probability of a value 
    //given a classification. Beyond that we want the liklihood table to have information on
    //The probability of any classification and the probability of any attribute.
    private ArrayList<LiklihoodTable> createLiklihoodTables(ArrayList<FrequencyTable> fTables) {
        ArrayList<LiklihoodTable> tables = new ArrayList<>();

        return tables;
    }

    //We assume line is an array of attribute values pre-binned.
    public int classify(int[] line) {
        double maxProbability = 0;
        int classification = -1;
        for (int i = 0; i < numOfClassifications; i++) {
            double probability = probabilityOfClass(i, line);
            if (probability > maxProbability) {
                maxProbability = probability;
                classification = i;
            }
        }
        return classification;
    }

    public double probabilityOfClass(int classValue, int[] line) {
        double probability = 1;
        for (int i = 0; i < line.length; i++) {
            LiklihoodTable table = lTables.get(i);

            //Probability of class classValue given value of attribute at position i
            probability *= table.table[line[i]][classValue];
            //divide by probability of the given attribute
            //probability /= table.table[line[i]][]
        }
        //multiply by probability of given class.

        return -1;
    }

    public class FrequencyTable {

        int rowCount;
        int columnCount;
        int[][] table;
        int attributePosition;

        public FrequencyTable(int[] attributeValues, int[] classifications, int numberOfClassifications, int attributePosition) {
            int attributeCount = getDistinctValueCount(attributeValues);
            table = new int[attributeCount][numberOfClassifications];
            this.attributePosition = attributePosition;

            for (int i = 0; i < attributeValues.length; i++) {
                int attributeValue = attributeValues[i];
                int classification = classifications[i];
                table[attributeValue][classification]++;
            }

            rowCount = table.length;
            columnCount = table[0].length;
        }
    }

    public class LiklihoodTable {

        int attributeId;
        double[][] table;

        public LiklihoodTable(FrequencyTable fTable) {
            attributeId = fTable.attributePosition;
            table = new double[fTable.rowCount][fTable.columnCount];
            int totalCount = 0;

            int[] classificationTotals = new int[fTable.columnCount - 1];
            for (int i = 0; i < classificationTotals.length; i++) {
                for (int j = 0; j < fTable.rowCount; j++) {
                    classificationTotals[i] += fTable.table[j][i];
                }
            }
            int[] attributeTotals = new int[fTable.rowCount];
            for (int i = 0; i < attributeTotals.length; i++) {
                for (int j = 0; j < fTable.columnCount; j++) {
                    attributeTotals[j] += fTable.table[i][j];
                }
            }

            for (int i = 0; i < fTable.rowCount; i++) {
                for (int j = 0; j < fTable.columnCount; j++) {
                    totalCount += fTable.table[i][j];
                    table[i][j] = fTable.table[i][j] / classificationTotals[j];//the table position at i,j is P(Attribute i | classification j)

                }
            }
        }
    }
}
