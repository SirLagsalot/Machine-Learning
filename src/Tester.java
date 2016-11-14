
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
      //  fiveByTwoTest();
    }

    public ArrayList<Instance> normalize(ArrayList<Instance> instances) {

        if (!instances.get(0).discrete) {
            double[][] features = new double[instances.get(0).unbinnedFeatures.size()][instances.size()];
            for (int i = 0; i < instances.get(0).unbinnedFeatures.size(); i++) {
                for (int j = 0; j < instances.size(); j++) {
                    features[i][j] = instances.get(j).unbinnedFeatures.get(i);
                }
            }
            double[] binWidth = new double[features.length];

            for (int i = 0; i < binWidth.length; i++) {
                binWidth[i] = bin(features[i]);
                System.out.println("Bin width = " + binWidth[i]);
            }
            //apply bins
        }
        return instances;

        //group data by attribute
//        double[][] features = new double[instances.get(0).unbinnedFeatures.size()][instances.size()];
//        for (int i = 0; i < instances.get(0).features.size(); i++) {
//            for (int j = 0; j < instances.size(); j++) {
//                features[i][j] = instances.get(j).unbinnedFeatures.get(i);
//            }
//        }
        //get min and max value for each data attribute
//        double min[] = new double[features[0].length];
//        double max[] = new double[features[0].length];
//        
//        for (int i = 0; i < features.length - 1; i++) {
//            min[i] = 9999;
//            max[i] = 0;
//            for (int j = 0; j < features[0].length - 1; j++) {
//                if (features[i][j] < min[i]) {
//                    min[i] = features[i][j];
//                }
//                if (features[i][j] > max[i]) {
//                    max[i] = features[i][j];
//                }
//            }
//        }
        //sort attributes
//        double[] medians = new double[features.length];
//        for (int i = 0; i < features.length - 1; i++) {
//            Arrays.sort(features[i]);
//            medians[i] = getMedian(features[i]);
//        }
//        
//        
//
//        //calculate inter quartile range
//        int lower = 9999, upper = 0;
//
//        //get statistics
//        for (int i = 0; i < features.length - 1; i++) {
//
//        }
//
//        return instances;
    }

    private int bin(double[] values) {

        for (double d: values) {
            System.out.print(d + ", ");
        }
        System.out.println("");
        //sort values
        Arrays.sort(values);

        //get median
        double median = getMedian(values);
        //System.out.println("Median" + median);

        //split into two arrays above and below median
        double[] lower = new double[values.length / 2 - 1];
        double[] upper = new double[values.length / 2 - 1];
        for (int i = 0; i < values.length - 1; i++) {
            if (values[i] < median) {
                lower[i] = values[i];
            }
            if (values[i] > median) {
                upper[i] = values[i];
            }
        }

        //get meadian of sub arrays
        double upperMedian = getMedian(upper);
        double lowerMedian = getMedian(lower);

        //IQR = distance between medians
        double IQR = upperMedian - lowerMedian;
        //System.out.println("iqr: " + IQR);

        //numBins = 2 * IQR * n^(-1/3)
        double h = (2.0 * IQR * Math.pow(values.length, -1 / 3));
       // System.out.println(h);
        return 0;
    }

    private double getMedian(double[] m) {
        int middle = m.length / 2;
//        for (double d : m) {
//            System.out.print(d + ", ");  
//        }
        System.out.println("");
        if (m.length % 2 == 1) {
            return m[middle];
        } else {
            return (m[middle - 1] + m[middle]) / 2.0;
        }
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
            set1 = normalize(set1);
            ArrayList<Instance> set2 = new ArrayList<>();
            set2.addAll(dataInstances.subList(dataInstances.size() / 2, dataInstances.size()));
            set2 = normalize(set2);

            NaiveBayes nb = new NaiveBayes(set1);
            TAN tan = new TAN(set1);
            KNearestNeighbor kNN = new KNearestNeighbor(set1, k);
            ID3 id3 = new ID3(set1);

            //call classifiers for each instance in the test set
            for (Instance instance : set2) {
                ArrayList<Integer> testInstance = instance.features;                                                            //TODO: Logging for each classification maybe...
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
        System.out.println("5 x 2 Cross Validation Test on " + origin + " classifier accuracies");
        System.out.println("________________________________");
        System.out.println("Naive Bayes:               " + new DecimalFormat("#.##").format(nbAccuracy));
        System.out.println("Tree Augmented Naive Bayes:" + new DecimalFormat("#.##").format(tanAccuracy));
        System.out.println("k-Nearest Neighbor:        " + new DecimalFormat("#.##").format(knnAccuracy));
        System.out.println("Iterative Dichotomiser 3:  " + new DecimalFormat("#.##").format(id3Accuracy));
    }
}
