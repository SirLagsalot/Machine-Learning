import java.util.ArrayList;

public class NaiveBayes implements Classifier {

    private  DataSet trainingData;
    private int[][] data;
    public LiklihoodTable liklihoodTable;

    public NaiveBayes(DataSet trainingData) {

        this.trainingData = trainingData;
        data = convertTrainingDataToData(trainingData);
        ArrayList<FrequencyTable> fTables = createFrequencyTables(data);
        ArrayList<LiklihoodTable> lTables = createLiklihoodTables(fTables);
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
        int numOfClassifications = getDistinctValueCount(data[data.length-1]);
        
        for (int i = 0; i < data.length-1; i++) {
            tables.add(new FrequencyTable(data[i], data[data.length-1], numOfClassifications));
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
    
    @Override
    public String classify() {

        String classification = "";
        return classification;
    }
    
    public class FrequencyTable{
        int rowCount;
        int columnCount;
        int[][] table;
        public FrequencyTable(int[] attributeValues, int[] classifications, int numberOfClassifications){
            table = new int[getDistinctValueCount(attributeValues)+1][numberOfClassifications+1];
            
        }
    }
    public class LiklihoodTable{
        public LiklihoodTable(FrequencyTable fTable){
            double[][][] table = new double[fTable.rowCount+1][fTable.columnCount+1][2];
            
        }
    }
}
