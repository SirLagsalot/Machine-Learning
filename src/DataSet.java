
import java.util.ArrayList;

public class DataSet {

    public ArrayList<Instance> data = new ArrayList<>();
    public int length;

    public DataSet(ArrayList<Instance> data) {
        this.data = data;
        this.length = data.size();
    }
}
