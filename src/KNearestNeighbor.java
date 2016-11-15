
import java.util.ArrayList;
import java.util.Collections;

public class KNearestNeighbor implements Classifier {

    private final int length;
    private final int k;
    private final ArrayList<Instance> trainingData;

    public KNearestNeighbor(ArrayList<Instance> trainingData, int k) {

        this.trainingData = trainingData;
        this.k = k;
        this.length = trainingData.get(0).features.size();
    }

    private double calcDistance(ArrayList<Integer> testFeatures, ArrayList<Integer> trainingFeatures) {

        double distance = 0.0;
        // System.out.println("test length= " + testFeatures.size());
        //System.out.println("train");
        assert testFeatures.size() == trainingFeatures.size();
        //go through all features and sum distance
        for (int i = 0; i < testFeatures.size() - 1; i++) {
            //   System.out.println(testFeatures.get(i));
            //   System.out.println(trainingFeatures.get(i));
            //   distance += Math.pow((testFeatures.get(i) - trainingFeatures.get(i)), 2);
        }
        return Math.sqrt(distance);
    }

    @Override
    public int classify(ArrayList<Integer> testFeatures) {

        int classification = -1;

        for (Instance trainingInstance : trainingData) {

            trainingInstance.distance = calcDistance(testFeatures, trainingInstance.features);
        }

        //sort by distance
        Collections.sort(trainingData, (Instance instance1, Instance instance2) -> {
            if (instance1.distance > instance2.distance) {
                return 1;
            } else if (instance1.distance < instance2.distance) {
                return -1;
            } else {
                return 0;
            }
        });

        int[] kNearest = new int[k];
        for (int i = 0; i < k; i++) {
            kNearest[i] = trainingData.get(i).getClassification();
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
