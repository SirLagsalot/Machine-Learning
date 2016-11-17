
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class KNearestNeighbor implements Classifier {

    private final int k;
    private final ArrayList<Instance> trainingData;
    private double[] stdDevs;
    private final int numClasses;

    public KNearestNeighbor(ArrayList<Instance> trainingData, int k, int numClasses) {

        this.trainingData = trainingData;
        this.numClasses = numClasses;
        this.k = k;
        getStdDev(trainingData);
    }

    private void getStdDev(ArrayList<Instance> trainingData) {

        int size = trainingData.get(0).features.size();
        stdDevs = new double[size];
        int[] values = new int[trainingData.size()];
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < trainingData.size(); i++) {
                values[i] = trainingData.get(i).features.get(j);
            }
            stdDevs[j] = getStdDev(values);
        }
    }

    private double getStdDev(int[] values) {

        double mean = getMean(values);
        double temp = 0;
        for (double val : values) {
            temp += (val - mean) * (val - mean);
        }
        return Math.sqrt(temp / values.length);
    }

    private double getMean(int[] values) {

        double sum = 0.0;
        for (double val : values) {
            sum += val;
        }
        return sum / values.length;
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

    private double HVDM(ArrayList<Integer> trainingFeatures, ArrayList<Integer> testFeatures) {

        double distance = 0.0;
        for (int i = 0; i < testFeatures.size(); i++) {
            distance += (Math.pow(trainingFeatures.get(i) - testFeatures.get(i), 2) / (4 * stdDevs[i]));
        }
        return Math.sqrt(distance);
    }

    private double VDM(ArrayList<Integer> trainingFeatures, ArrayList<Integer> testFeatures) {

        double distance = 0.0;

        return distance;
    }

    //well fk me mate valuedifferencemetric is kinda hard...
    //  private double valueDifferenceMetric(ArrayList<Integer> trainingFeatures) {
//        double distance = 0.0;
//        int q = 1;
//        int C = numClasses;
//
//        //for one feature..
//        //
//        double pAXC[] = new double[C];
//        int nAX = 0;     //P(c|xa)
//        int[] nAXC = new int[C];
//        //get num istance in t that have val x for attribute a and output class c
//        for (int c = 0; c < numClasses; c++) {
//            for (Instance in : trainingData) {
//                if (in.classification == c && Objects.equals(in.features.get(c), trainingFeatures.get(c))) {
//                    nAXC[c]++;
//                }
//            }
//            nAX += nAXC[c];
//        }
//        for (int c = 0; c < C; c++) {
//            pAXC[c] = (nAXC[c] / nAX);
//        }
//        System.out.println("pAXC" + Arrays.toString(pAXC));
    //HVDM(x,y) = sqrt(sum over features (da^2(xa,ya)))
    //da(xa, ya) = |x - y| / 4 sigma a
    //for each attribute in feature vector, get stdDev
    //get P(a,x,c)= N(a,x,c) / N(a,x)
    //N(a,x) = sum over C N(a,x,c)
    //N(a,x,c) = num instances in training set with val x for attribute a and output class c
    //vdm(x,y) = sum over classes (p(a,x,c) -p(a,y,c))^q 
    //N(a,x) = num instances in training set with value x for attribute a
    //N(a,x,c) num instances in training set with value x for attribute a and output class c
    //C is the number of output classes in domain
    //q is constantant (1 or 2)?
    //P(a,x,c) is the conditional probability that the output class is c given that attribute a has the value x : P(c|Xa)
    //P(a,x,c) = N(a,x,c) / N (a,x)
    //N(a,x) = sum over C of N(a,x,c)
    // return distance;
    //  }
    @Override
    public int classify(ArrayList<Integer> testFeatures) {

        //calculate distance to each instance in training set
        for (Instance trainingInstance : trainingData) {
            //trainingInstance.distance = calcDistance(testFeatures, trainingInstance.features);
            trainingInstance.distance = HVDM(testFeatures, trainingInstance.features);
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
