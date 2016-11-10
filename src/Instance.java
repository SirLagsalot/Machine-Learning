
import java.util.ArrayList;
import java.util.Iterator;

public class Instance implements Iterable {

    protected int classification;
    protected ArrayList<String> features;

    public Instance(int classification, String... features) {

        this.classification = classification;
        for (String f : features) {
            this.features.add(f);
        }
    }

    @Override
    public Iterator iterator() {
        return features.iterator();
    }

}
