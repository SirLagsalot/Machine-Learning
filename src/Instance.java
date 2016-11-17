
import java.util.ArrayList;

public class Instance {

    public int classification;
    public String className;
    public ArrayList<Integer> featureInd;
    public ArrayList<Integer> features;
    public ArrayList<Double> unbinnedFeatures;
    public double distance;
    public boolean discrete;
    public int numClasses;
    
    public Instance(){
        
    }
    
    public Instance(Instance i){
        
        classification = i.classification;
        className = i.className;
        features = new ArrayList();
        for(Integer inst: i.features){
            features.add(inst);
        }
        featureInd = new ArrayList();
        for(Integer inst: i.featureInd){
            featureInd.add(inst);
        }
        unbinnedFeatures = i.unbinnedFeatures;
        distance = i.distance;
        discrete = i.discrete;
        numClasses = i.numClasses;
    }

    public Instance(ArrayList<Integer> features, String className, boolean discrete) {

        featureInd = new ArrayList();
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
