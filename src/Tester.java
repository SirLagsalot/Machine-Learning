
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Tester {

    private final int k = 3;    //value for kNN

    private final ArrayList<Instance> dataInstances;
    private final ArrayList<Bin> bins = new ArrayList();
    private final String origin;

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
                        if (bin.binWidths.isEmpty()) {
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

    //return the correct bin for a given attribute
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
        int[] binnedValues = new int[values.length];
        Bin bin = new Bin();
        bin.attrPosition = featureIndex;
        int position = 0;
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
                bin.binWidths.add((sortedValues[position] + sortedValues[position - 1]) / 2.0);
            }

        }
        bins.add(bin);

        int returnValues[] = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            returnValues[i] = binValue(values[i], bin);
        }
        return returnValues;
    }

    private int binValue(double value, Bin bin) {

        for (int i = 0; i < bin.binWidths.size(); i++) {
            if (value < bin.binWidths.get(i)) {
                return i;
            }
        }
        return bin.binWidths.size();
    }

    //Execute a 5x2 cross fold validation on the dataset using each of the algorithms
    private void fiveByTwoTest() {

        Mapper map = new Mapper(dataInstances);
        try {
            try (PrintWriter writer = new PrintWriter(origin + "-test.txt", "UTF-8")) {
                printWithConditions("*******************************", writer, 0);
                printWithConditions(" ACCURACIES ARE AT THE BOTTOM", writer, 0);
                printWithConditions("*******************************", writer, 0);
                printWithConditions("Testing file '" + origin + ".data'", writer, 0);
                printWithConditions("", writer, 0);
                printWithConditions("Performing 5x2 cross validation", writer, 0);
                double nbAccuracy = 0, tanAccuracy = 0, knnAccuracy = 0, id3Accuracy = 0;
                //run 5 times, 2 trails each time
                for (int i = 0; i < 5; i++) {
                    printWithConditions((i + 1) + ".1: Training data", writer, i);
                    bins.clear();
                    //randomly split dataSet into a test set and a trainging set
                    Collections.shuffle(dataInstances);

                    ArrayList<Instance> set1 = new ArrayList<>();
                    set1.addAll(dataInstances.subList(0, dataInstances.size() / 2));
                    ArrayList<Instance> set2 = new ArrayList<>();
                    set2.addAll(dataInstances.subList(dataInstances.size() / 2, dataInstances.size()));

                    normalize(set1);
                    printDataSet(set1, true, writer, i);
                    printWithConditions("", writer, i);

                    printWithConditions("\tBuilding classifier Naive Bayes with training set", writer, i);
                    NaiveBayes nb = new NaiveBayes(set1);
                    printWithConditions("\tBuilding classifier TAN with training set", writer, i);
                    TAN tan = new TAN(set1);
                    printWithConditions("\tBuilding classifier kNN with training set", writer, i);
                    KNearestNeighbor kNN = new KNearestNeighbor(set1, k);
                    printWithConditions("\tBuilding classifier ID3 with training set", writer, i);
                    ID3 id3 = new ID3(set1);


                    for (Instance instance : set2) {

                        normalize(instance);
                        ArrayList<Integer> testInstance = instance.features;
                        int classification = nb.classify(testInstance);

                        printWithConditions("", writer, i);
                        printWithConditions("\t\t Testing Naive Bayes:", writer, i);
                        printWithConditions("\t\t\t Classified class: " + map.maps.get(classification), writer, i);
                        printWithConditions("\t\t\t Actual class: " + instance.className, writer, i);

                        if (classification == instance.classification) {
                            printWithConditions("\t\t\t Success!", writer, i);
                            nbAccuracy++;
                        } else {
                            printWithConditions("\t\t\t Failure!", writer, i);
                        }

                        classification = tan.classify(testInstance);
                        printWithConditions("", writer, i);
                        printWithConditions("\t\t Testing TAN:", writer, i);
                        printWithConditions("\t\t\t Classified class: " + map.maps.get(classification), writer, i);
                        printWithConditions("\t\t\t Actual class: " + instance.className, writer, i);

                        if (classification == instance.classification) {
                            printWithConditions("\t\t\t Success!", writer, i);
                            tanAccuracy++;
                        } else {
                            printWithConditions("\t\t\t Failure!", writer, i);
                        }
                        classification = kNN.classify(testInstance);
                        printWithConditions("", writer, i);
                        printWithConditions("\t\t Testing kNN:", writer, i);
                        printWithConditions("\t\t\t Classified class: " + map.maps.get(classification), writer, i);
                        printWithConditions("\t\t\t Actual class: " + instance.className, writer, i);

                        if (classification == instance.classification) {
                            printWithConditions("\t\t\t Success!", writer, i);
                            knnAccuracy++;
                        } else {
                            printWithConditions("\t\t\t Failure!", writer, i);
                        }

                        classification = id3.classify(testInstance);
                        printWithConditions("", writer, i);
                        printWithConditions("\t\t Testing ID3:", writer, i);
                        printWithConditions("\t\t\t Classified class: " + map.maps.get(classification), writer, i);
                        printWithConditions("\t\t\t Actual class: " + instance.className, writer, i);

                        if (classification == instance.classification) {
                            printWithConditions("\t\t\t Success!", writer, i);
                            id3Accuracy++;
                        } else {
                            printWithConditions("\t\t\t Failure!", writer, i);
                        }

                    }

                    //swap training and test sets, repeat trial
                    bins.clear();
                    set1 = new ArrayList<>();
                    set1.addAll(dataInstances.subList(0, dataInstances.size() / 2));
                    set2 = new ArrayList<>();
                    set2.addAll(dataInstances.subList(dataInstances.size() / 2, dataInstances.size()));

                    normalize(set2);

                    nb = new NaiveBayes(set2);
                    tan = new TAN(set2);
                    kNN = new KNearestNeighbor(set2, k);
                    id3 = new ID3(set2);


                    //call classifiers for each instance in the test set
                    for (Instance instance : set1) {

                        normalize(instance);
                        ArrayList<Integer> testInstance = instance.features;

                        int classification = nb.classify(testInstance);

                        if (classification == instance.classification) {
                            nbAccuracy++;
                        } else {
                        }

                        classification = tan.classify(testInstance);

                        if (classification == instance.classification) {
                            tanAccuracy++;
                        } else {
                        }
                        classification = kNN.classify(testInstance);

                        if (classification == instance.classification) {
                            knnAccuracy++;
                        } else {
                        }

                        classification = id3.classify(testInstance);

                        if (classification == instance.classification) {
                            id3Accuracy++;
                        } else {
                        }
                    }
                }   //calculate accuracy %
                int trials = dataInstances.size() * 5;
                nbAccuracy /= trials;
                tanAccuracy /= trials;
                knnAccuracy /= trials;
                id3Accuracy /= trials;
                //print results

                printWithConditions("\n\n5 x 2 Cross Validation Test on " + origin + " classifier accuracies", writer, 0);
                printWithConditions("____________________________________", writer, 0);
                printWithConditions("Naive Bayes:                  " + new DecimalFormat("#.##").format(nbAccuracy * 100) + "%", writer, 0);
                printWithConditions("Tree Augmented Naive Bayes:   " + new DecimalFormat("#.##").format(tanAccuracy * 100) + "%", writer, 0);
                printWithConditions("k-Nearest Neighbor:           " + new DecimalFormat("#.##").format(knnAccuracy * 100) + "%", writer, 0);
                printWithConditions("Iterative Dichotomiser 3:     " + new DecimalFormat("#.##").format(id3Accuracy * 100) + "%", writer, 0);
            }

        } catch (Exception ex) {
            System.out.println("Error encountered: " + ex);
            System.exit(-1);
        }
    }

    //Write training set data to file
    private static void printDataSet(ArrayList<Instance> data, boolean binned, PrintWriter writer, int j) {
        if (j == 0) {
            if (binned || data.get(0).discrete) {
                for (Instance in : data) {
                    ArrayList<Integer> binnedData = in.features;
                    for (Integer i : binnedData) {
                        writer.print(i + " ");
                    }
                    printWithConditions("", writer, j);
                }
            } else {
                for (Instance in : data) {
                    ArrayList<Double> binnedData = in.unbinnedFeatures;
                    for (Double i : binnedData) {
                        // System.out.print(i + " ");
                        writer.print(new DecimalFormat("#.##").format(i) + " ");
                    }
                    printWithConditions("", writer, j);
                }
            }
        }
    }

    private class Bin {

        int attrPosition;
        ArrayList<Double> binWidths = new ArrayList();
    }

    public Map getUnique(ArrayList<Instance> dataset) {
        Map<Integer, String> unique = new HashMap();
        for (Instance inst : dataset) {
            if (!unique.containsKey(inst.classification)) {
                unique.put(inst.classification, inst.className);
            }
        }
        return unique;
    }

    public static void printWithConditions(String string, PrintWriter w, int i) {
        try {
            if (i == 0) {
                w.println(string);
            }
        } catch (Exception e) {

        }
    }
    
    public class Mapper{
        ArrayList<String> maps = new ArrayList();
        
        public Mapper(ArrayList<Instance> dataSet){
            int classificationCount = Utilities.getClassificationCount(dataSet);
            for (int i = 0; i < classificationCount; i++) {
                for (Instance instance : dataSet) {
                    if (instance.classification == i) {
                        maps.add(instance.className);
                        break;
                    }
                }
            }
            maps.add("new class");
        }
        
        public class Classification{
            int classNum;
            String className;
            public Classification(int classN, String name){
                this.classNum = classN;
                this.className = name;
            }
        }
    }
}
