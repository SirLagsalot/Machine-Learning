
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Tester {

    private final int k = 3;    //value for kNN

    private final ArrayList<Instance> dataInstances;
    private final ArrayList<Bin> bins = new ArrayList();
    private final String origin;

    private class Bin {

        int attrPosition;
        ArrayList<Double> binWidths = new ArrayList();
    }

    public Tester(DataSet dataSet, String origin) {

        this.dataInstances = dataSet.data;
        this.origin = origin;
        fiveByTwoTest();
    }

    //process a single instance to place continuous values in discrete bins of computed size
    private void normalize(Instance instance) {

        if (!instance.discrete) {

            ArrayList<Double> features = instance.unbinnedFeatures;
            ArrayList<Integer> binnedFeatures = new ArrayList();

            for (int i = 0; i < features.size(); i++) {

                double binMe = features.get(i);
                Bin bin = getBin(i);
                if (bin != null) {
                    int preBinnedSize = binnedFeatures.size();
                    for (int j = 0; j < bin.binWidths.size(); j++) {
                        if (binMe < bin.binWidths.get(j)) {
                            binnedFeatures.add(j);
                            break;
                        }
                    }
                    if (binnedFeatures.size() == preBinnedSize) {
                        if (bin.binWidths.size() == 0) {
                            binnedFeatures.add(0);
                        } else {
                            binnedFeatures.add(bin.binWidths.size() - 1);
                        }
                    }
                } else {
                    binnedFeatures.add((int) binMe);
                }
            }
            instance.features = binnedFeatures;
        }
    }

    private Bin getBin(int attrIndex) {
        for (Bin bin : bins) {
            if (bin.attrPosition == attrIndex) {
                return bin;
            }
        }
        return null;
    }

    //process dataset to place continuous values in discrete bins of computed size
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

    //cast entire column of integers to doubles
    private int[] doubleArrayToIntArray(double[] column) {

        int[] intColumn = new int[column.length];
        for (int i = 0; i < column.length; i++) {
            intColumn[i] = (int) column[i];
        }
        return intColumn;
    }

    //determine if the provided column contrains double or integer values
    private boolean containsDoubles(double[] column) {

        for (int i = 0; i < column.length; i++) {
            if (column[i] - (int) column[i] != 0) {
                return true;
            }
        }
        return false;
    }

    //assign the input values to bins beginning with index 0
    private int[] bin(double[] values, int featureIndex) {

        //use Sturge's Rule to calculate number of bins
        int numBins = (int) (1 + 3.322 * Math.log10(values.length));

        double[] sortedValues = Arrays.copyOf(values, values.length);
        int itemsPerBin = (values.length / numBins) + 1;
        Arrays.sort(sortedValues);

        //calculate bin width
        //binWidths.add((sortedValues[sortedValues.length - 1] - sortedValues[0]) / numBins + 0.00001);
        int[] binnedValues = new int[values.length];
        Bin bin = new Bin();
        bin.attrPosition = featureIndex;
        int position = -1;
        int binNumber = 0;
        //todo make sure to fix the 0,0,0,0,0,...,0,.011,.012 case so that you don't have 5 bins but rather just 2
        boolean sameStreak = false;
        for (int i = 0; i < numBins; i++) {
            for (int j = 0; j < itemsPerBin; j++) {
                position++;

                if (position < sortedValues.length) {
                    binnedValues[position] = binNumber;
                } else {
                    position--;
                }
            }
            binNumber++;
            //here we are 'in between' bins
            if (sortedValues[position] != sortedValues[position - 1]) {
                bin.binWidths.add((sortedValues[position] + sortedValues[position - 1]) / (double) 2);
            }

        }
        bins.add(bin);

        int returnValues[] = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            returnValues[i] = binValue(values[i], bin);
        }
        //bin the values based on binwidth

//
//        //assign values to bins
//        for (int i = 0; i < values.length; i++) {
//            double val = values[i];
//            binnedValues[i] = (int) (val / binWidths.get(featureIndex)) % numBins;
//        }
        return returnValues;
    }

    public int binValue(double value, Bin bin) {
        for (int i = 0; i < bin.binWidths.size(); i++) {
            if (value < bin.binWidths.get(i)) {
                return i;
            }
        }
        return bin.binWidths.size();
    }

    //Execute a 5x2 cross fold validation on the dataset using each of the algorithms
    private void fiveByTwoTest() {

        try {
            PrintWriter writer = new PrintWriter(origin + "-test.txt", "UTF-8");
            
            writer.println("*******************************");
            writer.println(" ACCURACIES ARE AT THE BOTTOM");
            writer.println("*******************************");
            
            writer.println("Testing file '" + origin + ".data'");
            writer.println();
            writer.println("Performing 5x2 cross validation");

            double nbAccuracy = 0, tanAccuracy = 0, knnAccuracy = 0, id3Accuracy = 0;

            //run 5 times, 2 trails each time
            for (int i = 0; i < 5; i++) {
                writer.println((i + 1) + ".1: Training data");
                bins.clear();
                //randomly split dataSet into a test set and a trainging set
                Collections.shuffle(dataInstances);

                ArrayList<Instance> set1 = new ArrayList<>();
                set1.addAll(dataInstances.subList(0, dataInstances.size() / 2));
                ArrayList<Instance> set2 = new ArrayList<>();
                set2.addAll(dataInstances.subList(dataInstances.size() / 2, dataInstances.size()));

                normalize(set1);
                printDataSet(set1, true, writer);
                writer.println();
                //normalize(set2);

                writer.println("\tBuilding classifier Naive Bayes with training set");
                NaiveBayes nb = new NaiveBayes(set1);
                writer.println("\tBuilding classifier TAN with training set");
                TAN tan = new TAN(set1);
                writer.println("\tBuilding classifier kNN with training set");
                KNearestNeighbor kNN = new KNearestNeighbor(set1, k);
                writer.println("\tBuilding classifier ID3 with training set");
                ID3 id3 = new ID3(set1);

                for (Instance instance : set2) {

                    normalize(instance);
                    ArrayList<Integer> testInstance = instance.features;
                    int classification = nb.classify(testInstance);

                    writer.println();
                    writer.println("\t\t Testing Naive Bayes:");
                    writer.println("\t\t\t Classified class: " + classification);
                    writer.println("\t\t\t Actual class: " + instance.classification);

                    if (classification == instance.classification) {
                        writer.println("\t\t\t Success!");
                        nbAccuracy++;
                    } else {
                        writer.println("\t\t\t Failure!");
                    }

                    classification = tan.classify(testInstance);
                    writer.println();
                    writer.println("\t\t Testing TAN:");
                    writer.println("\t\t\t Classified class: " + classification);
                    writer.println("\t\t\t Actual class: " + instance.classification);

                    if (classification == instance.classification) {
                        writer.println("\t\t\t Success!");
                        tanAccuracy++;
                    } else {
                        writer.println("\t\t\t Failure!");
                    }
                    classification = kNN.classify(testInstance);
                    writer.println();
                    writer.println("\t\t Testing kNN:");
                    writer.println("\t\t\t Classified class: " + classification);
                    writer.println("\t\t\t Actual class: " + instance.classification);

                    if (classification == instance.classification) {
                        writer.println("\t\t\t Success!");
                        knnAccuracy++;
                    } else {
                        writer.println("\t\t\t Failure!");
                    }

                    classification = id3.classify(testInstance);
                    writer.println();
                    writer.println("\t\t Testing ID3:");
                    writer.println("\t\t\t Classified class: " + classification);
                    writer.println("\t\t\t Actual class: " + instance.classification);

                    if (classification == instance.classification) {
                        writer.println("\t\t\t Success!");
                        id3Accuracy++;
                    } else {
                        writer.println("\t\t\t Failure!");
                    }
                }

                bins.clear();
                set1 = new ArrayList<>();
                set1.addAll(dataInstances.subList(0, dataInstances.size() / 2));
                set2 = new ArrayList<>();
                set2.addAll(dataInstances.subList(dataInstances.size() / 2, dataInstances.size()));
                normalize(set2);
                //swap training and test sets, repeat trial
                
                writer.println((i + 1) + ".2: Training data");
                printDataSet(set2, true, writer);
                writer.println();
                
                writer.println("\tBuilding classifier Naive Bayes with training set");
                nb = new NaiveBayes(set2);
                writer.println("\tBuilding classifier TAN with training set");
                tan = new TAN(set2);
                writer.println("\tBuilding classifier kNN with training set");
                kNN = new KNearestNeighbor(set2, k);
                writer.println("\tBuilding classifier ID3 with training set");
                id3 = new ID3(set2);

                //call classifiers for each instance in the test set
                for (Instance instance : set1) {

                    normalize(instance);
                    ArrayList<Integer> testInstance = instance.features;

                    int classification = nb.classify(testInstance);

                    writer.println();
                    writer.println("\t\t Testing Naive Bayes:");
                    writer.println("\t\t\t Classified class: " + classification);
                    writer.println("\t\t\t Actual class: " + instance.classification);

                    if (classification == instance.classification) {
                        writer.println("\t\t\t Success!");
                        nbAccuracy++;
                    } else {
                        writer.println("\t\t\t Failure!");
                    }

                    classification = tan.classify(testInstance);
                    writer.println();
                    writer.println("\t\t Testing TAN:");
                    writer.println("\t\t\t Classified class: " + classification);
                    writer.println("\t\t\t Actual class: " + instance.classification);

                    if (classification == instance.classification) {
                        writer.println("\t\t\t Success!");
                        tanAccuracy++;
                    } else {
                        writer.println("\t\t\t Failure!");
                    }
                    classification = kNN.classify(testInstance);
                    writer.println();
                    writer.println("\t\t Testing kNN:");
                    writer.println("\t\t\t Classified class: " + classification);
                    writer.println("\t\t\t Actual class: " + instance.classification);

                    if (classification == instance.classification) {
                        writer.println("\t\t\t Success!");
                        knnAccuracy++;
                    } else {
                        writer.println("\t\t\t Failure!");
                    }

                    classification = id3.classify(testInstance);
                    writer.println();
                    writer.println("\t\t Testing ID3:");
                    writer.println("\t\t\t Classified class: " + classification);
                    writer.println("\t\t\t Actual class: " + instance.classification);

                    if (classification == instance.classification) {
                        writer.println("\t\t\t Success!");
                        id3Accuracy++;
                    } else {
                        writer.println("\t\t\t Failure!");
                    }
                }
            }

            //calculate accuracy %
            int trials = dataInstances.size() * 5;
            nbAccuracy /= trials;
            tanAccuracy /= trials;
            knnAccuracy /= trials;
            id3Accuracy /= trials;

            //print results
            writer.println();
            writer.println("\n\n5 x 2 Cross Validation Test on " + origin + " classifier accuracies");
            writer.println("____________________________________");
            writer.println("Naive Bayes:                  " + new DecimalFormat("#.##").format(nbAccuracy * 100) + "%");
            writer.println("Tree Augmented Naive Bayes:   " + new DecimalFormat("#.##").format(tanAccuracy * 100) + "%");
            writer.println("k-Nearest Neighbor:           " + new DecimalFormat("#.##").format(knnAccuracy * 100) + "%");
            writer.println("Iterative Dichotomiser 3:     " + new DecimalFormat("#.##").format(id3Accuracy * 100) + "%");
           
            writer.close();
        } catch (Exception e) {

        }
    }

    private static void printDataSet(ArrayList<Instance> data, boolean binned, PrintWriter writer) {

        if (binned || data.get(0).discrete) {

            for (Instance in : data) {
                ArrayList<Integer> binnedData = in.features;
                for (Integer i : binnedData) {
                    writer.print(i + " ");
                }
                writer.println("");
            }
        } else {

            for (Instance in : data) {
                ArrayList<Double> binnedData = in.unbinnedFeatures;
                for (Double i : binnedData) {
                    // System.out.print(i + " ");
                    writer.print(new DecimalFormat("#.##").format(i) + " ");
                }
                writer.println("");
            }
        }
    }
}
