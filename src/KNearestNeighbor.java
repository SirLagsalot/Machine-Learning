
import java.util.ArrayList;
import java.util.Collections;

public class KNearestNeighbor implements Classifier {

    private final int k;                                //the number or neighbors to use for calculation
    private final ArrayList<Instance> trainingData;     //the training set
    private final int numClasses;                       //the number of unique classes in the training set
    private static final int alpha = 2;                 //exponent for VDM calculation
    private double[][][] Naxc;                          //conditional probability tables

    public KNearestNeighbor(ArrayList<Instance> trainingData, int k) {

        //Initialization
        this.trainingData = trainingData;
        this.numClasses = Utilities.getClassificationCount(trainingData);
        this.k = k;
        createNaxc();
    }

    //Calculate the value of every N_(a,x,c) to later be used when calculating VDM
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

    //Calculates the Value Distance Metric
    private double VDM(Instance trainingVector, ArrayList<Integer> testVector) {

        double distance = 0.0;

        int C = numClasses;
        int j = trainingVector.classification;
//        System.out.println("num classes: " + C);

        for (int i = 0; i < testVector.size(); i++) {   //loop over each feature

            for (int c = 0; c < C; c++) {               //sum over number of classes

                distance += Math.pow(Math.abs(Naxc[i][testVector.get(i)][c] - Naxc[i][trainingVector.features.get(i)][c]), alpha);
            }
        }
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
            kNearestClasses[i] = trainingData.get(i).classification;
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
