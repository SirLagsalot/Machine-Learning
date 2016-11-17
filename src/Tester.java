
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Tester {

    private final int k = 3;

    private final ArrayList<Instance> dataInstances;
    private final String origin;
    private final int numClasses;
    private ArrayList<Double> binWidths = new ArrayList();

    public Tester(DataSet dataSet, String origin) {

        this.dataInstances = dataSet.data;
        this.origin = origin;
        this.numClasses = dataSet.numClasses;
        fiveByTwoTest();
    }

    private void normalize(Instance instance) {

        if (!instance.discrete) {

            ArrayList<Double> features = instance.unbinnedFeatures;
            ArrayList<Integer> binnedFeatures = new ArrayList();
            for (int i = 0; i < features.size(); i++) {
                double binMe = features.get(i);
                int binned = 0;
                double count = 0.0;
                //get bin for binMe
                for (int b = 0; b < binWidths.size(); b++) {
                    count += binWidths.get(b);
                    if (count > binMe) {
                        binned = b;
                        break;
                    }
                }
                // System.out.println("binned value: " + binned);
                binnedFeatures.add(binned);
            }
            instance.features = binnedFeatures;
        }
    }

    private void normalize(ArrayList<Instance> instances) {

        if (!instances.get(0).discrete) {

            //split data into arrays of columns
            double[][] features = new double[instances.get(0).unbinnedFeatures.size()][instances.size()];
            for (int i = 0; i < instances.get(0).unbinnedFeatures.size(); i++) {
                for (int j = 0; j < instances.size(); j++) {
                    features[i][j] = instances.get(j).unbinnedFeatures.get(i);
                }
            }

            //bin data
            int[][] binnedValues = new int[instances.get(0).unbinnedFeatures.size()][instances.size()];
            for (int i = 0; i < instances.get(0).unbinnedFeatures.size(); i++) {
                if (!containsDoubles(features[i])) {
                    binnedValues[i] = doubleArrayToIntArray(features[i]);
                } else {
                    binnedValues[i] = bin(features[i], i);
                }
            }

            //add binned data to each data instance
            for (int i = 0; i < instances.size(); i++) {
                Instance instance = instances.get(i);
                instance.features.clear();
                for (int j = 0; j < instances.get(0).unbinnedFeatures.size(); j++) {
                    instances.get(i).features.add(binnedValues[j][i]);
                }
            }
        }
    }

    private int[] doubleArrayToIntArray(double[] column) {
        int[] intColumn = new int[column.length];
        for (int i = 0; i < column.length; i++) {
            intColumn[i] = (int) column[i];
        }
        return intColumn;
    }

    private boolean containsDoubles(double[] column) {
        for (int i = 0; i < column.length; i++) {
            if (column[i] - (int) column[i] != 0) {
                return true;
            }
        }
        return false;
    }

    private int[] bin(double[] values, int b) {

        //use Sturge's Rule to calculate number of bins
        int numBins = (int) (1 + 3.322 * Math.log10(values.length));

        double[] sortedValues = Arrays.copyOf(values, values.length);
        Arrays.sort(sortedValues);

        //calculate bin width
        binWidths.add((sortedValues[sortedValues.length - 1] - sortedValues[0]) / numBins + 0.00001);
        int[] binnedValues = new int[values.length];

        //assign values to bins
        for (int i = 0; i < values.length; i++) {
            double val = values[i];
            binnedValues[i] = (int) (val / binWidths.get(b)) % numBins;
        }
        return binnedValues;
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
            //normalize(set1);

            ArrayList<Instance> set2 = new ArrayList<>();
            set2.addAll(dataInstances.subList(dataInstances.size() / 2, dataInstances.size()));
            //normalize(set2);

            NaiveBayes nb = new NaiveBayes(set1);
            TAN tan = new TAN(set1);
            KNearestNeighbor kNN = new KNearestNeighbor(set1, k, numClasses);
            //ID3 id3 = new ID3(set1);

            //call classifiers for each instance in the test set
            normalize(set1);
            for (Instance instance : set2) {
                ArrayList<Integer> testInstance = instance.features;                                                        //TODO: Logging for each classification maybe...
                normalize(instance);
                if (nb.classify(testInstance) == instance.classification) {
                    nbAccuracy++;
                }
                if (tan.classify(testInstance) == instance.classification) {
                    tanAccuracy++;
                }
                if (kNN.classify(testInstance) == instance.classification) {
                    knnAccuracy++;
                }
//                if (id3.classify(testInstance) == instance.classification) {
//                    id3Accuracy++;
//                }
            }
            // System.exit(0);
            normalize(set2);
            //swap training and test sets, repeat trial
            nb = new NaiveBayes(set2);
            tan = new TAN(set2);
            kNN = new KNearestNeighbor(set2, k, numClasses);
            //  id3 = new ID3(set2);

            //call classifiers for each instance in the test set
            for (Instance instance : set1) {
                normalize(instance);
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
//                if (id3.classify(testInstance) == instance.classification) {
//                    id3Accuracy++;
//                }
            }
        }
        //System.out.println("knnacc" + knnAccuracy);
        //calculate accuracy %
        int trials = dataInstances.size() * 5;
        nbAccuracy /= trials;
        tanAccuracy /= trials;
        knnAccuracy /= trials;
        id3Accuracy /= trials;

        //print results
        System.out.println("\n\n5 x 2 Cross Validation Test on " + origin + " classifier accuracies");
        System.out.println("____________________________________");
        System.out.println("Naive Bayes:               " + new DecimalFormat("#.##").format(nbAccuracy));
        System.out.println("Tree Augmented Naive Bayes:" + new DecimalFormat("#.##").format(tanAccuracy));
        System.out.println("k-Nearest Neighbor:        " + new DecimalFormat("#.##").format(knnAccuracy));
        System.out.println("Iterative Dichotomiser 3:  " + new DecimalFormat("#.##").format(id3Accuracy));
    }

    private static void printDataSet(ArrayList<Instance> data, boolean binned) {

        if (binned || data.get(0).discrete) {

            for (Instance in : data) {
                ArrayList<Integer> binnedData = in.features;
                for (Integer i : binnedData) {
                    System.out.print(i + " ");
                }
                System.out.println("");
            }
        } else {

            for (Instance in : data) {
                ArrayList<Double> binnedData = in.unbinnedFeatures;
                for (Double i : binnedData) {
                    // System.out.print(i + " ");
                    System.out.print(new DecimalFormat("#.##").format(i) + " ");
                }
                System.out.println("");
            }
        }
    }
}
