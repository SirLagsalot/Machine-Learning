
import java.util.ArrayList;
import java.util.Collections;

public class KNearestNeighbor implements Classifier {

    private final int k;
    private final ArrayList<Instance> trainingData;

    public KNearestNeighbor(ArrayList<Instance> trainingData, int k) {

        this.trainingData = trainingData;
        this.k = k;
    }

    private double calcDistance(ArrayList<Integer> testFeatures, ArrayList<Integer> trainingFeatures) {

        double distance = 0.0;
        assert testFeatures.size() == trainingFeatures.size();

        //go through all features and sum distance
        for (int i = 0; i < testFeatures.size() - 1; i++) {
            distance += Math.pow((testFeatures.get(i) - trainingFeatures.get(i)), 2);
        }
        return Math.sqrt(distance);
    }

    //well fk me mate valuedifferencemetric is kinda hard...
    private double valueDifferenceMetric(ArrayList<Integer> testFeatures, ArrayList<Integer> trainingFeatures) {

        double distance = 0.0;
        //vdm(x,y) = sum over classes (p(a,x,c) -p(a,y,c))^q 
        //N(a,x) = num instances in training set with value x for attribute a
        //N(a,x,c) num instances in training set with value x for attribute a and output class c
        //C is the number of output classes in domain
        //q is constantant (1 or 2)?
        //P(a,x,c) is the conditional probability that the output class is c given that attribute a has the value x : P(c|Xa)
        //P(a,x,c) = N(a,x,c) / N (a,x)
        //N(a,x) = sum over C of N(a,x,c)

        return distance;
    }

    @Override
    public int classify(ArrayList<Integer> testFeatures) {

        //calculate distance to each instance in training set
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

        int[] kNearestClasses = new int[k];
        for (int i = 0; i < k; i++) {
            kNearestClasses[i] = trainingData.get(i).getClassification();
        }

        //get frequency of the k closest classifications
        int[] freq = new int[k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (kNearestClasses[j] == kNearestClasses[i]) {
                    freq[i]++;
                }
            }
        }

        //determine most frequent
        int classification = -1, mostFreq = -1;
        for (int i = 0; i < k; i++) {
            if (freq[i] > mostFreq) {
                classification = kNearestClasses[i];
            }
        }
        return classification;
    }
}
