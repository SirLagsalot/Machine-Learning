
import java.util.ArrayList;

public class DataSet {

    public ArrayList<Instance> data = new ArrayList<>();
    private ArrayList<String> classifications = new ArrayList<>();
    public int length;

    public void addLine(Instance newInstance, String classification) {

        data.add(newInstance);
        classifications.add(classification);
    }

    public String getClassification(int classID) {

        return classifications.get(classID);
    }

    public void addClassification(String addClass) {

        classifications.add(addClass);
    }

}
