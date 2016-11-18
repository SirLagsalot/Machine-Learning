

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class KNearestNeighbor implements Classifier {


    private final int k;
    private final ArrayList<Instance> trainingData;
    private double[] stdDevs;
    private final int numClasses;
    private static final int alpha = 2;
    private double[][][] Naxc;


    public KNearestNeighbor(ArrayList<Instance> trainingData, int k) {

        //Initialization
        this.trainingData = trainingData;
        this.numClasses = Utilities.getClassificationCount(trainingData);
        this.k = k;
        getStdDev(trainingData);//TODO, do we need this???
        //Calculate the value of every N_(a,x,c) to later be used when calculating VDM
        createNaxc();


    }


    // 
    private void createNaxc() {
        int maxAttrRange = getMaxAttrRange();
        Naxc = new double[trainingData.get(0).features.size()][maxAttrRange][numClasses];
        for (int attributePosition = 0; attributePosition < trainingData.get(0).features.size(); attributePosition++) {
            for (int classValue = 0; classValue < numClasses; classValue++) {
                for (int attributeValue = 0; attributeValue < maxAttrRange; attributeValue++) {
                    Naxc[attributePosition][attributeValue][classValue] = probabilityOfClassGivenAttr(attributePosition, classValue, attributeValue);
                }
            }
        }
    }


    private double probabilityOfClassGivenAttr(int attrPosition, int classValue, int attrValue) {
        int[] attrValues = getColumn(attrPosition);
        int[] classValues = getClasses();
        int classAndAttrValue = 0;
        int attrValueCount = 0;
        for (int i = 0; i < attrValues.length; i++) {
            if (attrValues[i] == attrValue) {
                attrValueCount++;
                if (classValues[i] == classValue) {
                    classAndAttrValue++;
                }
            }
        }


        return classAndAttrValue / (double) attrValueCount;
    }


    private int[] getColumn(int index) {
        int[] column = new int[trainingData.size()];
        for (int i = 0; i < trainingData.size(); i++) {
            Instance instance = trainingData.get(i);
            column[i] = instance.features.get(index);
        }
        return column;


    }


    private int[] getClasses() {
        int[] column = new int[trainingData.size()];
        for (int i = 0; i < trainingData.size(); i++) {
            Instance instance = trainingData.get(i);
            column[i] = instance.classification;
        }
        return column;


    }


    private int getMaxAttrRange() {
        int max = 0;
        for (Instance instance : trainingData) {
            for (int feature : instance.features) {
                if (feature > max) {
                    max = feature;
                }
            }
        }
        return max + 1;
    }


    //Gets standard deviation over the dataset
    private void getStdDev(ArrayList<Instance> trainingData) {


        int size = trainingData.get(0).features.size();
        stdDevs = new double[size];
        int[] values = new int[trainingData.size()];
        for (int j = 0; j < size; j++) { //Loops through the features of each instance
            for (int i = 0; i < trainingData.size(); i++) {
                values[i] = trainingData.get(i).features.get(j); // assigns the value of the 
            }
            stdDevs[j] = getStdDev(values);
        }
    }


    //Gets the standard deviation of one instance
    private double getStdDev(int[] values) {


        double mean = getMean(values);
        double temp = 0;
        //Gets Euclidian Distance
        for (double val : values) { 
            temp += (val - mean) * (val - mean);
        }
        return Math.sqrt(temp / values.length);
    }

    // Calculates the mean of an instance
    private double getMean(int[] values) {


        double sum = 0.0;
        for (double val : values) {// sums all values of the features
            sum += val;
        }
        return sum / values.length;
    }

    //These are not used Zach figure out what to do with them
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

    //Calculates the Value Distance Metric
    private double VDM(Instance trainingVector, ArrayList<Integer> testVector) {


        double distance = 0.0;


        int C = numClasses;
        int j = trainingVector.classification;
//        System.out.println("num classes: " + C);


        for (int i = 0; i < testVector.size(); i++) {   //loop over each feature


            for (int c = 0; c < C; c++) {               //sum over number of classes


                distance += Math.pow(Math.abs(Naxc[i][testVector.get(i)][c] - Naxc[i][trainingVector.features.get(i)][c]), 2);
            }
        }


        //sqareroot of the sum over the number of classes of
        //r = training featrue, x = test features
        //Nxi  = num instances in training set that have the value x for the feature i
        //Nxij = num of training vectors from class j that have value x for feature i
        //Nri  = num instances in training set that have the value r for feature i
        //Nrij = num of training vectors from class j that have value r for feature i
        //dvm = sum over c classes |P(cj|xi) - P(cj|ri)|^alpha
        return distance;
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
            //trainingInstance.distance = calcDistance(testFeatures, trainingInstance.features);
            //trainingInstance.distance = HVDM(testFeatures, trainingInstance.features);
            trainingInstance.distance = VDM(trainingInstance, testFeatures);
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


        //Assign values of an instance's classification to a new set
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






