
import java.util.ArrayList;
import java.util.Collections;

public class Tester {

    //k value for k-nearest neighbors algorithm
    private final int k = 5;

    private final ArrayList<Instance> dataInstances;
    private final DataSet dataSet;
    private final String origin;

    public Tester(DataSet dataSet, String origin) {
        this.dataSet = dataSet;
        this.dataInstances = dataSet.data;
        this.origin = origin;
        fiveByTwoTest();
    }

    //Execute a 5x2 cross fold validation on the dataset using each of the algorithms
    private void fiveByTwoTest() {

        double nbAccuracy = 0, tanAccuracy = 0, knnAccuracy = 0, id3Accuracy = 0;
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
            for (Instance instance : set2) {
                //TODO: add statistics tracking by comparing return value to actual classification, tally results and calculate stats after full test
                ArrayList<Integer> testInstance = instance.features;
                if (nb.classify(testInstance) == instance.classification) {
                    nbAccuracy++;
                }
                if (tan.classify(testInstance) == instance.classification) {
                    tanAccuracy++;
                }
                if (kNN.classify(testInstance) == instance.classification) {
                    knnAccuracy++;
                }
                if (id3.classify(testInstance) == instance.classification) {
                    id3Accuracy++;
                }
            }

            //swap training and test sets, repeat trial
            nb = new NaiveBayes(set2);
            tan = new TAN(set2);
            kNN = new KNearestNeighbor(set2, k);
            id3 = new ID3(set2);

            //call classifiers for each instance in the test set
            for (Instance instance : set1) {
                ArrayList<Integer> testInstance = instance.features;
                if (nb.classify(testInstance) == instance.classification) {
                    nbAccuracy++;
                }
                if (tan.classify(testInstance) == instance.classification) {
                    tanAccuracy++;
                }
                if (kNN.classify(testInstance) == instance.classification) {
                    knnAccuracy++;
                }
                if (id3.classify(testInstance) == instance.classification) {
                    id3Accuracy++;
                }
            }
        }
        //calculate accuracy %
        nbAccuracy /= 5000;
        tanAccuracy /= 5000;
        knnAccuracy /= 5000;
        id3Accuracy /= 5000;
        
        //print results
        System.out.println("5 x 2 Cross Validation Test on " + origin + " classifier accuracies:");
        System.out.println("Naive Bayes:\t" + nbAccuracy);
        System.out.println("TAN:\t\t" + tanAccuracy);
        System.out.println("k-Nearest Neighbor:\t" + knnAccuracy);
        System.out.println("Iterative Dichotomiser 3:\t" + id3Accuracy);
    }
}
