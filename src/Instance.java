
import java.util.ArrayList;
import java.util.Comparator;

public class Instance implements Comparable {

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
        this.distance = 0.0;
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
    public int compareTo(Object instance) {
        
        Instance in = (Instance) instance;
        if (in.distance == this.distance) {
            return 0;
        }
        assert (int) (in.distance - this.distance) != 0;
        return (int) (in.distance - this.distance);
        //return this.distance < ((Instance) instance).distance ? 1 : -1;
    }
}
