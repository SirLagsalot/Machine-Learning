
import java.util.ArrayList;
import java.util.Collections;


public class Tester {
    
    private final int k = 5;

    private final ArrayList<Instance> dataSet;
    
    public Tester(ArrayList<Instance> dataSet) {
        this.dataSet = dataSet;
        fiveByTwoTest();
    }
    
    //Execute a 5x2 cross fold validation on the dataset
    private void fiveByTwoTest() {
        
        //split into training and test sets
        //randomly divide in two
        Collections.shuffle(dataSet);
        ArrayList<Instance> trainingSet = new ArrayList<>();
        trainingSet.addAll(dataSet.subList(0, dataSet.size() / 2));
        ArrayList<Instance> testSet = new ArrayList<>();
        testSet.addAll(dataSet.subList(dataSet.size() / 2, dataSet.size()));
        
        NaiveBayes nb = new NaiveBayes(trainingSet);
        TAN tan = new TAN(trainingSet);
        KNearestNeighbor kNN = new KNearestNeighbor(trainingSet, k);
        ID3 id3 = new ID3(trainingSet);
        
        
        
        
    }
}
