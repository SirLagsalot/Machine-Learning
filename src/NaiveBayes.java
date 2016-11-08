import java.util.ArrayList;

public class NaiveBayes implements Classifier {

    private  DataSet trainingData;
    private int[][] data;
    public LiklihoodTable liklihoodTable;
    int numOfClassifications;
    private ArrayList<LiklihoodTable> lTables;
    public NaiveBayes(DataSet trainingData) {

        this.trainingData = trainingData;
        data = convertTrainingDataToData(trainingData);
        ArrayList<FrequencyTable> fTables = createFrequencyTables(data);
        lTables = createLiklihoodTables(fTables);
    }
    
    //converts the training data into a more usable int array
    private int[][] convertTrainingDataToData(DataSet trainingData){
        return new int[1][1];
    }
    
    //Creates a series of tables for each attribute and maps the frequencys of 
    //each classification to each attribute.
    //We assume that int[][] data is columns then rows, and that the last column is the classifications
    private ArrayList<FrequencyTable> createFrequencyTables(int[][] data){
        ArrayList<FrequencyTable> tables = new ArrayList<>();
        numOfClassifications = getDistinctValueCount(data[data.length-1]);
        
        for (int i = 0; i < data.length-1; i++) {
            tables.add(new FrequencyTable(data[i], data[data.length-1], numOfClassifications, i));
        }
        return tables;
    }
    
    private int getDistinctValueCount(int[] column){
        
        return 1;
    }
    //for each frequencyTable we want to calculate P(x|c), ie the probability of a value 
    //given a classification. Beyond that we want the liklihood table to have information on
    //The probability of any classification and the probability of any attribute.
    private ArrayList<LiklihoodTable> createLiklihoodTables(ArrayList<FrequencyTable> fTables){
        ArrayList<LiklihoodTable> tables = new ArrayList<>();
        
        return tables;
    }
    
    //We assume line is an array of attribute values pre-binned.
    public int classify(int[] line) {
        double maxProbability = 0;
        int classification = -1;
        for (int i = 0; i < numOfClassifications; i++) {
            double probability = probabilityOfClass(i,line);
            if(probability > maxProbability){
                maxProbability = probability;
                classification = i;
            }
        }
        return classification;
    }
    
    public double probabilityOfClass(int classValue, int[] line){
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
    
    public class FrequencyTable{
        int rowCount;
        int columnCount;
        int[][] table;
        int attributePosition;
        public FrequencyTable(int[] attributeValues, int[] classifications, int numberOfClassifications, int attributePosition){
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
    public class LiklihoodTable{
        int attributeId;
        double[][] table;
        public LiklihoodTable(FrequencyTable fTable){
            attributeId = fTable.attributePosition;
            table = new double[fTable.rowCount][fTable.columnCount];
            int totalCount = 0;
            
            int[] classificationTotals = new int[fTable.columnCount-1];
            for (int i = 0; i < classificationTotals.length; i++) {
                for (int j = 0; j < fTable.rowCount; j++) {
                    classificationTotals[i]+=fTable.table[j][i];
                }
            }
            int[] attributeTotals = new int[fTable.rowCount];
            for (int i = 0; i < attributeTotals.length; i++) {
                for (int j = 0; j < fTable.columnCount; j++) {
                    attributeTotals[j]+=fTable.table[i][j];
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
