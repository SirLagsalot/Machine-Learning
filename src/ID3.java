
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ID3 implements Classifier {

    private final ArrayList<Instance> trainingData;

    private ArrayList<Integer[]> test = new ArrayList();
    private Tree decisionTree;

    public ID3(ArrayList<Instance> trainingData) {

        this.trainingData = trainingData;
        ArrayList<Integer[]> testSet = new ArrayList();

        Integer[] test2 = {0, 0, 0, 0, 0};
        Integer[] test3 = {0, 0, 0, 1, 0};
        Integer[] test4 = {1, 0, 0, 0, 1};
        Integer[] test5 = {2, 1, 0, 0, 1};
        Integer[] test6 = {2, 2, 1, 0, 1};
        Integer[] test7 = {2, 2, 1, 1, 0};
        Integer[] test8 = {1, 2, 1, 1, 1};
        Integer[] test9 = {0, 1, 0, 0, 0};
        Integer[] test10 = {0, 2, 1, 0, 1};
        Integer[] test11 = {2, 1, 1, 0, 1};
        Integer[] test12 = {0, 1, 1, 1, 1};
        Integer[] test13 = {1, 1, 0, 1, 1};
        Integer[] test14 = {1, 0, 1, 0, 1};
        Integer[] test15 = {2, 1, 0, 1, 0};

        testSet.add(test2);
        testSet.add(test3);
        testSet.add(test4);
        testSet.add(test5);
        testSet.add(test6);
        testSet.add(test7);
        testSet.add(test8);
        testSet.add(test9);
        testSet.add(test10);
        testSet.add(test11);
        testSet.add(test12);
        testSet.add(test13);
        testSet.add(test14);
        testSet.add(test15);
        test = testSet;
        convertForTest(test);
        classify(new ArrayList<Integer>());
//        classify(new ArrayList<Integer>());
//        System.out.println(gain(testSet));
    }

    public void convertForTest(ArrayList<Integer[]> test) {
        trainingData.clear();
        for (int j = 0; j < test.size(); j++) {
            ArrayList<Integer> vals = new ArrayList();
            for (int i = 0; i < test.get(0).length; i++) {
                vals.add(test.get(j)[i]);
            }
            Instance temp = new Instance(vals, "golf", true);
            temp.classification = 4;
            trainingData.add(temp);
        }
    }

    @Override
    public int classify(ArrayList<Integer> featureVector) {
        makeTree();

        printData(decisionTree.root.children.get(0).dataSet);
        System.out.println(decisionTree.root.children.get(2).attributeNum);

        return -1;
    }

    public void makeTree() {
        findRootNode();
        placeChildren(decisionTree.root);
        id3(decisionTree.root);

    }

    public void placeNextAttr(Node place) {
        
    }

    public void id3(Node root) {

        for (Node i : root.children) {
            fillNode(i.dataSet, i);
        }
    }

    public void fillNode(ArrayList<Instance> data, Node node) {
        ArrayList<Integer[]> vals = new ArrayList();
        ArrayList<Double> gains/*bro gaaaaiiinnnsss*/ = new ArrayList();
        ArrayList<Integer> unique = new ArrayList();
        for (int j = 0; j < data.get(0).features.size() - 1; j++) {

            for (Instance i : data) {
                Integer[] temp = {i.features.get(j), i.features.get(i.classification)};
                vals.add(temp);
                unique.add(temp[0]);
            }
            gains.add(gain(vals));
            vals.clear();
        }
        
        node.attributeNum = max(gains);
        node.isLeaf = false;
        node.pathVals = getUniqueAttrValues(unique);
    }

    public void placeChildren(Node parent) {
        for (int i = 0; i < parent.pathVals.size(); i++) {
            Node node = new Node(splitData(parent.attributeNum, i, parent.dataSet));
            parent.children.add(node);
        }
    }

    public void printData(ArrayList<Instance> data) {
        for (Instance i : data) {
            for (Integer j : i.features) {
                System.out.print(j + " ");
            }
            System.out.println("");
        }
    }

    public ArrayList<Instance> splitData(int attr, int attrVal, ArrayList<Instance> data) {
        ArrayList<Instance> tempData = new ArrayList();
        for (Instance i : data) {
            if (i.features.get(attr).equals(attrVal)) {
                tempData.add(i);
            }
        }
        return tempData;
    }

    //Calculate information gain to set root node
    public void findRootNode() {
        ArrayList<Integer[]> vals = new ArrayList();
        ArrayList<Double> gains/*bro gaaaaiiinnnsss*/ = new ArrayList();
        ArrayList<Integer> unique = new ArrayList();
        for (int j = 0; j < trainingData.get(0).features.size() - 1; j++) {

            for (Instance i : trainingData) {
                Integer[] temp = {i.features.get(j), i.features.get(i.classification)};
                vals.add(temp);
                unique.add(temp[0]);
            }
            gains.add(gain(vals));
            vals.clear();
        }

        //For old test set
//        for(int i = 0; i < test.get(0).length - 1; i++){
//            for(Integer[] j: test){
//                Integer[] tmp = {j[i], j[test.get(0).length - 1]};
//                vals.add(tmp);
//                unique.add(j[0]);
//            }
//            gains.add(gain(vals));
//            vals.clear();
//        }
//        for (Double d : gains) {
//            System.out.println(d);
//        }
        decisionTree = new Tree(new Node(false, max(gains), getUniqueAttrValues(unique), trainingData));
        for (Integer i : decisionTree.root.pathVals) {
            System.out.println(i);
        }
        //System.out.println(decisionTree.root.attributeNum);
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

    public boolean checkForPureSet(ArrayList<Integer[]> vals) {

        return false;
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

        boolean isLeaf = false;
        int attributeNum;
        double gain;
        ArrayList<Node> children = new ArrayList();
        ArrayList<Integer> pathVals;
        ArrayList<Instance> dataSet;

        public Node(ArrayList<Instance> dataSet) {
            this.dataSet = dataSet;
        }

        public Node(boolean isLeaf, int attributeNum, ArrayList<Integer> pathVals, ArrayList<Instance> dataSet) {
            this.isLeaf = isLeaf;
            this.attributeNum = attributeNum;
            this.pathVals = pathVals;
            this.dataSet = dataSet;
        }

    }
}
