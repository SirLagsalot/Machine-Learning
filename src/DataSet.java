
import java.util.ArrayList;

public class DataSet {

    public ArrayList<Instance> data = new ArrayList<>();
    public int numClasses;

    public DataSet(ArrayList<Instance> data, int numClasses) {
        this.data = data;
        this.numClasses = numClasses;
    }
}
