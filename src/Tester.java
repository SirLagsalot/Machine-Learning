
import java.util.ArrayList;
import java.util.Collections;

public class Tester {

    //k value for k-nearest neighbors algorithm
    private final int k = 5;

    private final ArrayList<Instance> dataInstances;
    private final DataSet dataSet;
    

    public Tester(DataSet dataSet) {
        this.dataSet = dataSet;
        this.dataInstances = dataSet.data;
        fiveByTwoTest();
    }

    //Execute a 5x2 cross fold validation on the dataset using each of the algorithms
    private void fiveByTwoTest() {

        //run 5 times, 2 trails each time
        for (int i = 0; i < 5; i++) {
            
            //randomly split dataSet into a test set and a trainging set
            Collections.shuffle(dataInstances);
            ArrayList<Instance> set1 = new ArrayList<>();
            set1.addAll(dataInstances.subList(0, dataInstances.size() / 2));
            ArrayList<Instance> set2 = new ArrayList<>();
            set2.addAll(dataInstances.subList(dataInstances.size() / 2, dataInstances.size()));

            NaiveBayes nb = new NaiveBayes(set1);
            TAN tan = new TAN(set1);
            KNearestNeighbor kNN = new KNearestNeighbor(set1, k);
            ID3 id3 = new ID3(set1);

            //call classifiers for each instance in the test set
            for (Instance instance : set2) {                                        //TODO: add statistics tracking by comparing return value to actual classification, tally results and calculate stats after full test
                ArrayList<Integer> testInstance = instance.features;
                nb.classify(testInstance);
                tan.classify(testInstance);
                kNN.classify(testInstance);
                id3.classify(testInstance);
            }

            //swap training and test sets, repeat trial
            nb = new NaiveBayes(set2);
            tan = new TAN(set2);
            kNN = new KNearestNeighbor(set2, k);
            id3 = new ID3(set2);

            //call classifiers for each instance in the test set
            for (Instance instance : set1) {
                ArrayList<Integer> testInstance = instance.features;
                nb.classify(testInstance);
                tan.classify(testInstance);
                kNN.classify(testInstance);
                id3.classify(testInstance);
            }
        }
    }
}
