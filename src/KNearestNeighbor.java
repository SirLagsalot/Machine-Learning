
import java.util.ArrayList;

public class KNearestNeighbor implements Classifier {

    private final int length;
    private final int k;
    private final ArrayList<Instance> trainingData;

    public KNearestNeighbor(ArrayList<Instance> trainingData, int k) {

        this.trainingData = trainingData;
        this.k = k;
        this.length = trainingData.get(0).features.size();
    }

    @Override
    public int classify(ArrayList<Integer> featureVector) {

        int classification = -1;

        //calculate distance to each instance in the training set
        for (Instance instance : trainingData) {
            double distance = 0;
            for (int i = 0; i < length; i++) {
                //distance between each fature
                distance += Math.pow(featureVector.get(i) - instance.features.get(i), 2);
            }
            instance.distance = Math.sqrt(distance);
        }

        //get the k smallest distances
        trainingData.sort((instance1, instance2) -> instance1.distance > instance2.distance ? 1 : -1);
        int[] kNearest = new int[k];
        for (int i = 0; i < k; i++) {
            kNearest[i] = trainingData.get(i).classification;
        }

        //get frequency of the k closest classifications
        int[] freq = new int[k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (kNearest[j] == kNearest[i]) {
                    freq[i]++;
                }
            }
        }

        //determine most frequent
        int mostFreq = -1;
        for (int i = 0; i < k; i++) {
            if (freq[i] > mostFreq) {
                classification = kNearest[i];
            }
        }
        return classification;
    }
}
