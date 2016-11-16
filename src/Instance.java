
import java.util.ArrayList;

public class Instance {

    public int classification;
    public String className;
    public ArrayList<Integer> features;
    public ArrayList<Double> unbinnedFeatures;
    public double distance;
    public boolean discrete;
    public int numClasses;

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
}
