
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ID3 implements Classifier {

    private final ArrayList<Instance> trainingData;

    private ArrayList<Integer[]> test = new ArrayList();
    private Tree decisionTree;

    public ID3(ArrayList<Instance> trainingData) {

        this.trainingData = trainingData;
//        ArrayList<Integer[]> testSet = new ArrayList();
//
//        Integer[] test2 = {0, 0};
//        Integer[] test3 = {0, 0};
//        Integer[] test4 = {1, 1};
//        Integer[] test5 = {2, 1};
//        Integer[] test6 = {2, 1};
//        Integer[] test7 = {2, 0};
//        Integer[] test8 = {1, 1};
//        Integer[] test9 = {0, 0};
//        Integer[] test10 = {0, 1};
//        Integer[] test11 = {2, 1};
//        Integer[] test12 = {0, 1};
//        Integer[] test13 = {1, 1};
//        Integer[] test14 = {1, 1};
//        Integer[] test15 = {2, 0};
//
//        testSet.add(test2);
//        testSet.add(test3);
//        testSet.add(test4);
//        testSet.add(test5);
//        testSet.add(test6);
//        testSet.add(test7);
//        testSet.add(test8);
//        testSet.add(test9);
//        testSet.add(test10);
//        testSet.add(test11);
//        testSet.add(test12);
//        testSet.add(test13);
//        testSet.add(test14);
//        testSet.add(test15);
//        System.out.println(gain(testSet));
    }

    @Override
    public int classify(ArrayList<Integer> featureVector) {
        findRootNode();
        System.out.println("");
        return -1;
    }

    //Calculate information gain to set root node
    public void findRootNode() {
        ArrayList<Integer[]> vals = new ArrayList();
        ArrayList<Double> gains/*bro gaaaaiiinnnsss*/ = new ArrayList();
        for (int j = 0; j < trainingData.get(0).features.size(); j++) {

            for (Instance i : trainingData) {
                Integer[] temp = {i.features.get(j), i.features.get(i.classification)};
                vals.add(temp);
            }
            gains.add(gain(vals));
            vals.clear();
        }
      
        decisionTree = new Tree(new Node(false, max(gains)));
    }

    public int max(ArrayList<Double> gains) {
        Double max = gains.get(0);
        int ind = 0;
        for (int i = 1; i < gains.size(); i++) {
            if (max < gains.get(i)) {
                max = gains.get(i);
                ind = i;
            }
        }
        return ind;
    }

    //Information gain calculation
    public double gain(ArrayList<Integer[]> vals) {
        //List declarations
        ArrayList<Integer> classVals = new ArrayList();
        ArrayList<Integer> attrVals = new ArrayList();
        ArrayList<Integer> unique;

        //Separate the input list
        for (Integer[] i : vals) {
            attrVals.add(i[0]);
            classVals.add(i[1]);
        }

        unique = getUniqueAttrValues(attrVals); //Get unique values
        int sum = vals.size();

        double gain = entropy(countClass(classVals)); // Entropy(S)
        ArrayList<Integer> tempClasses;

        for (Integer i : unique) { // - (sumation(|sv|/|s|) * Entropy(sv))
            tempClasses = getClassCountForIndAttr(i, vals);
            gain -= (((double) countAttr(i, vals) / sum) * entropy(tempClasses));
        }

        return gain;
    }

    //Count number of classifications for an individual value of an attribute
    public ArrayList<Integer> getClassCountForIndAttr(Integer attr, ArrayList<Integer[]> vals) {
        HashMap<Integer, Integer> tempCount = new HashMap(); //Used to obtain only unique values
        for (Integer[] i : vals) { // add a unique value or increment counter on existing
            if (attr.equals(i[0])) { // if the index equals the value of the attribute we're looking for
                if (tempCount.containsKey(i[1])) {
                    tempCount.put(i[1], tempCount.get(i[1]) + 1);
                } else {
                    tempCount.put(i[1], 1);
                }
            }
        }

        Iterator it = tempCount.entrySet().iterator();
        ArrayList<Integer> count = new ArrayList();
        while (it.hasNext()) { //Edit out the key for returning only values
            HashMap.Entry pair = (HashMap.Entry) it.next();
            count.add((Integer) pair.getValue());
            it.remove();
        }
        return count;
    }

    //Entropy calculation
    public double entropy(ArrayList<Integer> portions) {
        double entropy = 0;
        int total = sum(portions);
        for (Integer i : portions) {
            entropy += (0 - (((double) i / total) * (Math.log((double) i / total) / Math.log(2)))); //Math.log(2) used to convert to log2 from log10
        }
        return entropy;
    }

    //Get all uniqe values for specific attribute
    public ArrayList<Integer> getUniqueAttrValues(ArrayList<Integer> vals) {
        ArrayList<Integer> unique = new ArrayList();
        for (Integer i : vals) {
            if (!unique.contains(i)) {
                unique.add(i);
            }
        }
        return unique;
    }

    //Count the number of a specific attribute in the dataset
    public Integer countAttr(Integer val, ArrayList<Integer[]> vals) {
        Integer count = 0;
        for (Integer[] i : vals) {
            if (val.equals(i[0])) {
                count++;
            }
        }
        return count;
    }

    //Count the number of classifications for gain
    public ArrayList<Integer> countClass(ArrayList<Integer> vals) {
        ArrayList<Integer> res = new ArrayList();
        HashMap<Integer, Integer> temp = new HashMap(); //Used for unique values
        for (Integer i : vals) { // add a unique value or increment counter on existing
            if (temp.containsKey(i)) {
                temp.put(i, temp.get(i) + 1);
            } else {
                temp.put(i, 1);
            }
        }

        Iterator it = temp.entrySet().iterator();
        while (it.hasNext()) { //Edit out the key for returning only values
            HashMap.Entry pair = (HashMap.Entry) it.next();
            res.add((Integer) pair.getValue());
            it.remove();
        }
        return res;
    }

    //Find the total number of values for entropy/gain
    public int sum(ArrayList<Integer> vals) {
        int sum = 0;
        for (int i = 0; i < vals.size(); i++) {
            sum += vals.get(i);

        }
        return sum;
    }

    //Tree class
    private class Tree {

        Node root;

        public Tree(Node root) {
            this.root = root;
        }

        //Print the tree for testing
        public void printTree() {

        }

    }

    //Node with varying number of children
    private class Node {

        boolean isLeaf;
        int attributeNum;
        ArrayList<Node> children;

        public Node(boolean isLeaf, int attributeNum) {
            this.isLeaf = isLeaf;
            this.attributeNum = attributeNum;
        }
    }
}
