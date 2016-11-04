import java.util.ArrayList;
import java.util.Iterator;

public class Instance implements Iterable {

    protected String classification;
    protected ArrayList<Feature> features;
    private float distance;

    public Instance(String classification, Feature... features) {

        this.classification = classification;
        for (Feature f : features) {
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
