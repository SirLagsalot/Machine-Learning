import java.util.ArrayList;
import java.util.Iterator;

public class Instance implements Iterable {

    protected String classification;
    protected ArrayList<String> features;
    protected ArrayList<Double> preProcessed;
    private float distance;

    public Instance(String classification, String... features) {

        this.classification = classification;
        for (String f : features) {
            this.features.add(f);
        }
    }

    public void setDistance(float distance) {

        this.distance = distance;
    }

    public float getDistance() {

        return distance;
    }

    @Override
    public Iterator iterator() {
        return features.iterator();
    }

}
