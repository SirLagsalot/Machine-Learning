import java.util.*;

public class KNearestNeighbor implements Classifier {

    public static int k = 3;
    private ArrayList<Instance> trainingData, testData;

    public KNearestNeighbor(ArrayList<Instance> trainingData, ArrayList<Instance> testData, int k) {

        this.trainingData = trainingData;
        this.testData = testData;
        this.k = k;
    }

    @Override
    public String classify() {

        String classification = "";
        int size = trainingData.size();

        assert size > k : "invalid value of k";

        //calculate distance to each feature value
            //for each feature in vector, take difference in value
            //calculate square root of squared distances across vector



        //sort by distance
        testData.sort((instance1, instance2) -> instance1.getDistance() > instance2.getDistance() ? 1 : -1);

        //get k smallest distances
        ArrayList<Instance> kNN = new ArrayList<>();
        int[] freq = new int[k];
        for (int i = 0; i < k; i++) {
            kNN.add(testData.get(i));
        }

        //resolve class
        for (int i = 0; i < k; i++) {
            freq[i] = Collections.frequency(kNN, kNN.get(i).classification);
        }

        int mostFreq = -1;
        for (int i = 0; i < k; i++) {
            if (freq[i] > mostFreq) {
                classification = kNN.get(i).classification;
            }
        }

        return classification;
    }
}
