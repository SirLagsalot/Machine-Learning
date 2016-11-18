
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Utilities {

    //Count the number of classifications
    public static int getClassificationCount(ArrayList<Instance> dataSet) {
        Set set = new HashSet<>();
        for (Instance instance : dataSet) {
            set.add(instance.classification);
        }
        return set.size();
    }
}
