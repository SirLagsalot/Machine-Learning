
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ID3 implements Classifier {

    private final ArrayList<Instance> trainingData;

    private ArrayList<Integer[]> test = new ArrayList();

    public ID3(ArrayList<Instance> trainingData) {

        this.trainingData = trainingData;
        ArrayList<Integer[]> testSet = new ArrayList();

        Integer[] test2 = {0, 0};
        Integer[] test3 = {1, 0};
        Integer[] test4 = {0, 1};
        Integer[] test5 = {0, 1};
        Integer[] test6 = {0, 1};
        Integer[] test7 = {1, 0};
        Integer[] test8 = {1, 1};
        Integer[] test9 = {0, 0};
        Integer[] test10 = {0, 1};
        Integer[] test11 = {0, 1};
        Integer[] test12 = {1, 1};
        Integer[] test13 = {1, 1};
        Integer[] test14 = {0, 1};
        Integer[] test15 = {1, 0};

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
        System.out.println(gain(testSet));
    }

    @Override
    public int classify(ArrayList<Integer> featureVector) {

        return -1;
    }

    public double gain(ArrayList<Integer[]> vals) {
        ArrayList<Integer> classVals = new ArrayList();
        ArrayList<Integer> attrVals = new ArrayList();
        ArrayList<Integer> unique;
        for (Integer[] i : vals) {
            attrVals.add(i[0]);
            classVals.add(i[1]);
        }

        unique = getUniqueAttrValues(attrVals);
        int sum = vals.size();

        double gain = entropy(countClass(classVals));
        ArrayList<Integer> tempClasses;

        for (Integer i : unique) {
            tempClasses = getClassCountForIndAttr(i, vals);
            gain -= (((double) countAttr(i, vals) / sum) * entropy(tempClasses));
        }

        return gain;
    }

    public ArrayList<Integer> getClassCountForIndAttr(Integer attr, ArrayList<Integer[]> vals) {
        HashMap<Integer, Integer> tempCount = new HashMap();
        for (Integer[] i : vals) {
            if (attr.equals(i[0])) {
                if (tempCount.containsKey(i[1])) {
                    tempCount.put(i[1], tempCount.get(i[1]) + 1);
                } else {
                    tempCount.put(i[1], 1);
                }
            }
        }

        Iterator it = tempCount.entrySet().iterator();
        ArrayList<Integer> count = new ArrayList();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            count.add((Integer) pair.getValue());
            it.remove();
        }
        return count;
    }

    public double entropy(ArrayList<Integer> portions) {
        double entropy = 0;
        int total = sum(portions);
        for (Integer i : portions) {
            entropy += (0 - (((double) i / total) * (Math.log((double) i / total) / Math.log(2))));
        }
        return entropy;
    }

    public ArrayList<Integer> getUniqueAttrValues(ArrayList<Integer> vals) {
        ArrayList<Integer> unique = new ArrayList();
        for (Integer i : vals) {
            if (!unique.contains(i)) {
                unique.add(i);
            }
        }
        return unique;
    }

    public Integer countAttr(Integer val, ArrayList<Integer[]> vals) {
        Integer count = 0;
        for (Integer[] i : vals) {
            if (val.equals(i[0])) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<Integer> countClass(ArrayList<Integer> vals) {
        ArrayList<Integer> res = new ArrayList();
        HashMap<Integer, Integer> temp = new HashMap();
        for (Integer i : vals) {
            if (temp.containsKey(i)) {
                temp.put(i, temp.get(i) + 1);
            } else {
                temp.put(i, 1);
            }
        }
        Iterator it = temp.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            res.add((Integer) pair.getValue());
            it.remove();
        }
        return res;
    }

    public int sum(ArrayList<Integer> vals) {
        int sum = 0;
        for (int i = 0; i < vals.size(); i++) {
            sum += vals.get(i);

        }
        return sum;
    }
}
