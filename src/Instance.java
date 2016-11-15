
import java.util.ArrayList;
import java.util.Comparator;

public class Instance implements Comparator {

    public int classification;
    public String className;
    public ArrayList<Integer> features;
    public ArrayList<Double> unbinnedFeatures;
    public double distance;
    public boolean discrete;

    public Instance(ArrayList<Integer> features, String className, boolean discrete) {

        this.features = features;
        this.className = className;
        this.discrete = true;
        this.distance = 0.0f;
    }

    public Instance(ArrayList<Double> unbinnedFeatures, String className) {

        this.unbinnedFeatures = unbinnedFeatures;
        this.features = new ArrayList<>();
        this.className = className;
        this.discrete = false;
        this.distance = 0.0f;
    }

    public void setClassification(int classID) {
        classification = classID;
    }

    public int getClassification() {
        return classification;
    }

    @Override
    public int compare(Object o1, Object o2) {
        Instance i1 = (Instance) o1;
        Instance i2 = (Instance) o2;
        return i1.distance < i2.distance ? 1 : -1;
    }
}
