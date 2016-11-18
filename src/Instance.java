
import java.util.ArrayList;

public class Instance {

    public ArrayList<Integer> featureInd;
    public ArrayList<Integer> features;
    public ArrayList<Double> unbinnedFeatures;
    public int classification, numClasses;
    public String className;
    public double distance;
    public boolean discrete;

    //Clone an instance
    public Instance(Instance i) {

        this.classification = i.classification;
        this.className = i.className;
        this.features = new ArrayList();
        for (Integer instance : i.features) {
            features.add(instance);
        }
        this.featureInd = new ArrayList();
        for (Integer instance : i.featureInd) {
            featureInd.add(instance);
        }
        this.unbinnedFeatures = i.unbinnedFeatures;
        this.distance = i.distance;
        this.discrete = i.discrete;
        this.numClasses = i.numClasses;
    }

    //Add new binned instance
    public Instance(ArrayList<Integer> features, String className, boolean discrete) {

        featureInd = new ArrayList();
        this.features = features;
        this.className = className;
        this.discrete = true;
        this.distance = 0.0;
    }

    //Add new unbinned instance
    public Instance(ArrayList<Double> unbinnedFeatures, String className) {

        this.unbinnedFeatures = unbinnedFeatures;
        featureInd = new ArrayList();
        this.features = new ArrayList<>();
        this.className = className;
        this.discrete = false;
        this.distance = 0.0;
    }
}
