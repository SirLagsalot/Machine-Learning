
import java.util.ArrayList;
import java.util.Iterator;

public class Instance implements Iterable {

    protected int classification;
    protected ArrayList<Integer> features;
    protected ArrayList<Double> unbinnedFeatures;


    @Override
    public Iterator iterator() {
        return features.iterator();
    }

}
