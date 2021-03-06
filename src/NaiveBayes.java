
import java.util.ArrayList;

public class NaiveBayes implements Classifier {

    private final ArrayList<LiklihoodTable> lTables;

    public int[][] data;
    public int[] classColumn;

    LiklihoodTable liklihoodTable;
    int numOfClassifications;
    int numberOfFeatures;

    public NaiveBayes(ArrayList<Instance> trainingData) {

        data = convertTrainingDataToData(trainingData);
        classColumn = getColumn(data[0].length - 1);//class column is last column.
        ArrayList<FrequencyTable> fTables = createFrequencyTables(trainingData);
        lTables = createLiklihoodTables(fTables);
    }

    @Override
    public int classify(ArrayList<Integer> featureVector) {

        int[] intArray = new int[featureVector.size()];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = featureVector.get(i);
        }
        return classify(intArray);
    }

    //We assume line is an array of attribute values pre-binned.
    private int classify(int[] line) {

        double maxProbability = 0;
        int classification = -1;
        for (int curClass = 0; curClass < numOfClassifications; curClass++) {
            double probability = probabilityOfClass(curClass, line);
            if (probability > maxProbability) {
                maxProbability = probability;
                classification = curClass;
            }
        }
        return classification;
    }

    public final int[] getColumn(int position) {
        int[] column = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            column[i] = data[i][position];
        }
        return column;
    }

    //Gets the probability of an individual class value, returns P(C=value)
    //Done by calculating the number of times the indicated value appears
    //in the class column and dividing by the size of the column
    public double probabilityOfClassValue(int value) {

        int count = 0;
        //count the number of times the value appears in the class column
        for (int i = 0; i < classColumn.length; i++) {
            if (value == classColumn[i]) {
                count++;
            }
        }
        //return count / |classColumn|
        return count / (double) classColumn.length;
    }

    //returns P(Fi=attrValue)
    public double probabilityOfAttrValue(int[] attrVector, int attrValue) {

        double probability = 0.0;
        for (int i = 0; i < attrVector.length; i++) {
            if (attrVector[i] == attrValue) {
                probability++;
            }
        }
        return probability > 0 ? probability / (double) attrVector.length : 1;
    }

    //converts the training data into a more usable 2D int array
    private int[][] convertTrainingDataToData(ArrayList<Instance> trainingData) {

        Instance sample = trainingData.get(0);
        numberOfFeatures = sample.features.size();
        int[][] dataArray = new int[trainingData.size()][numberOfFeatures + 1];

        for (int i = 0; i < trainingData.size(); i++) {
            Instance instance = trainingData.get(i);
            for (int j = 0; j < numberOfFeatures; j++) {
                dataArray[i][j] = instance.features.get(j);
            }
            //class is last element in the row
            dataArray[i][numberOfFeatures] = instance.classification;
        }
        return dataArray;
    }

    //Creates a series of tables for each attribute and maps the frequencys of 
    //each classification to each attribute.
    //We assume that int[][] data is columns then rows, and that the last column is the classifications
    private ArrayList<FrequencyTable> createFrequencyTables(ArrayList<Instance> trainingData) {

        ArrayList<FrequencyTable> tables = new ArrayList<>();
        numOfClassifications = Utilities.getClassificationCount(trainingData);
        for (int i = 0; i < data[0].length; i++) {
            tables.add(new FrequencyTable(getColumn(i), numOfClassifications, i));
        }
        return tables;
    }

    //returns the maximul number of distinct values in a given column
    public int getDistinctValueCount(int[] column) {
        //should be the max in the array + 1 as we assume valid values are 0+ and we 'should' have each value
        int max = 0;
        for (int i = 0; i < column.length; i++) {
            if (column[i] > max) {
                max = column[i];
            }
        }
        return max + 1;
    }

    //for each frequencyTable we want to calculate P(x|c), ie the probability of a value 
    //given a classification. Beyond that we want the liklihood table to have information on
    //The probability of any classification and the probability of any attribute.
    private ArrayList<LiklihoodTable> createLiklihoodTables(ArrayList<FrequencyTable> fTables) {

        ArrayList<LiklihoodTable> tables = new ArrayList<>();
        for (FrequencyTable table : fTables) {
            tables.add(new LiklihoodTable(table));
        }
        return tables;
    }

    //returns P(F_(attr)=attrValue | C=classValue), in the event we've never seen attrValue before,
    //return 1 to not affect probability calculations
    public double probabilityOfAttrGivenClass(int attr, int attrValue, int classValue) {

        LiklihoodTable table = lTables.get(attr);
        return attr < table.table.length ? table.table[attrValue][classValue] : 1;
    }

    //returns the probability of a given class given all attribute values
    //returns P(C=classValue | F1 = line[0] ^ F2=line[1] ^... Fn=line[n-1])
    //note this does not give the exact probability, as it ignores dividing out
    //the probability of attributes, this is because that value is independent of
    //the class chosen and is unneeded to maximize.
    public double probabilityOfClass(int classValue, int[] line) {

        //start with probability of class
        double probability = probabilityOfClassValue(classValue);
        for (int i = 0; i < line.length; i++) {

            //Probability of class classValue given value of attribute at position i
            probability *= probabilityOfAttrGivenClass(i, line[i], classValue);

            //divide by probability of the given attribute
            probability /= probabilityOfAttrValue(getColumn(i), line[i]);
        }
        return probability;
    }

    class FrequencyTable {

        int rowCount;
        int columnCount;
        int[][] table;
        int attributePosition;

        //A Frequency Table is a 2D array which stores the number of occurrences of 
        //each possible attribute paired with a class value
        public FrequencyTable(int[] attributeValues, int numberOfClassifications, int attributePosition) {
            int attributeCount = getDistinctValueCount(attributeValues);
            table = new int[attributeCount][numberOfClassifications];
            this.attributePosition = attributePosition;

            for (int i = 0; i < attributeValues.length - 1; i++) {
                int attributeValue = attributeValues[i];
                int classification = classColumn[i];
                table[attributeValue][classification]++;
            }

            rowCount = table.length;
            columnCount = table[0].length;
        }
    }

    class LiklihoodTable {

        int attributeId;
        double[][] table;

        //A liklihood table gives P(a|c) via lTable[a][c]
        public LiklihoodTable(FrequencyTable fTable) {

            attributeId = fTable.attributePosition;
            table = new double[fTable.rowCount][fTable.columnCount];

            int[] classificationTotals = new int[fTable.columnCount];
            for (int i = 0; i < classificationTotals.length; i++) {
                for (int j = 0; j < fTable.rowCount; j++) {
                    classificationTotals[i] += fTable.table[j][i];
                }
            }
            int[] attributeTotals = new int[fTable.columnCount];
            for (int i = 0; i < fTable.rowCount; i++) {
                for (int j = 0; j < fTable.columnCount; j++) {
                    attributeTotals[j] += fTable.table[i][j];
                }
            }

            for (int i = 0; i < fTable.rowCount; i++) {
                for (int j = 0; j < fTable.columnCount; j++) {
                    table[i][j] = (fTable.table[i][j] + 1) / (double) (classificationTotals[j] + 1);//the table position at i,j is P(Attribute i | classification j)
                }
            }
        }
    }
}
