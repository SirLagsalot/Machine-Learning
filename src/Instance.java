
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Instance implements Iterable, Comparator {

    protected int classification = -1;
    protected ArrayList<Integer> features;
    protected ArrayList<Double> unbinnedFeatures;
    protected double distance = 0;
    protected boolean discrete;

    @Override
    public Iterator iterator() {
        return features.iterator();
    }

    @Override
    public int compare(Object o1, Object o2) {
        Instance i1 = (Instance) o1;
        Instance i2 = (Instance) o2;
        return i1.distance < i2.distance ? 1 : -1;
    }
}
