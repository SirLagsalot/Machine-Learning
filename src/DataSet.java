
import java.util.ArrayList;

public class DataSet {

    public ArrayList<Instance> data = new ArrayList();
    private ArrayList<String> classifications = new ArrayList();
    public int length;
    
    public String getClassification(int classID) {
        return classifications.get(classID);
    }
    
    public void addClassification(String addClass) {
        classifications.add(addClass);
        //System.out.println(classifications.size());
    }
    
    public ArrayList<String> getClassifications(){
        return classifications;
    }
    
}
