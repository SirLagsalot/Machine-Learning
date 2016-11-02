import java.util.ArrayList;
import java.util.Iterator;

public class Instance implements Iterable {

    protected String classification;
    protected ArrayList<String> features;


    @Override
    public Iterator iterator() {
        return features.iterator();
    }
}
