
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class KNearestNeighbor implements Classifier {

    private final int k;
    private final ArrayList<Instance> trainingData;
    private double[] stdDevs;
    private final int numClasses;
    private static final int alpha = 2;

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

    private double VDM(Instance trainingVector, ArrayList<Integer> testVector) {

        double distance = 0.0;
        int C = numClasses;
        int j = trainingVector.classification;

        for (int i = 0; i < testVector.size(); i++) {   //loop over each feature

            for (int c = 0; c < C; c++) {               //sum over number of classes

                double dis = 0.0;
                ArrayList<Instance> jInstances = getInstances(j);

                //get p(cj|xi)
                int Nxij = 0;
                for (Instance in : jInstances) {
                    if (Objects.equals(in.features.get(i), testVector.get(i))) {
                        Nxij++;
                    }
                }
                int Nxi = 0;
                for (Instance in : trainingData) {
                    //System.out.println("sup");
                    if (Objects.equals(in.features.get(i), testVector.get(i))) {
                        Nxi++;
                    }
                }
                System.out.println("Nxi: " + Nxi + " Nxij: " + Nxij);
                double pcjxi = Nxij / Nxi;

                //num of instances of class j where trainingvector.features.get(i) == testVector.get(i) / 
                //get p(cj|ri)
                int Nrij = 0;

                distance += Math.pow(pcjxi, alpha);
            }
        }

        //sqareroot of the sum over the number of classes of
        //r = training featrue, x = test features
        //Nxi  = num instances in training set that have the value x for the feature i
        //Nxij = num of training vectors from class j that have value x for feature i
        //Nri  = num instances in training set that have the value r for feature i
        //Nrij = num of training vectors from class j that have value r for feature i
        //dvm = sum over c classes |P(cj|xi) - P(cj|ri)|^alpha
        return Math.pow(distance, (1 / alpha));
    }

    private ArrayList<Instance> getInstances(int classID) {

        ArrayList<Instance> classes = new ArrayList();

        for (Instance i : trainingData) {
            if (i.classification == classID) {
                classes.add(i);
            }
        }
        return classes;
    }

    @Override
    public int classify(ArrayList<Integer> testFeatures) {

        //calculate distance to each instance in training set
        for (Instance trainingInstance : trainingData) {
            trainingInstance.distance = calcDistance(testFeatures, trainingInstance.features);
            //trainingInstance.distance = HVDM(testFeatures, trainingInstance.features);
            //trainingInstance.distance = VDM(trainingInstance, testFeatures);
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
