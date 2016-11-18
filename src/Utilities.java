
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Utilities {

    public static ArrayList<Instance> dataSet;
    
    public static void setDataSet(ArrayList<Instance> dataSet) {
        Utilities.dataSet = dataSet;
    }
    //Count the number of classifications
    public static int getClassificationCount(ArrayList<Instance> dataSet) {
        Set set = new HashSet<>();
        for (Instance instance : dataSet) {
            set.add(instance.classification);
        }
        return set.size();
    }

    public static int[] getColumn(int index) {

        int[] column = new int[dataSet.size()];
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            column[i] = instance.features.get(index);
        }
        return column;
    }
}
