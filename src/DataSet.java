
import java.util.ArrayList;

public class DataSet {

    public ArrayList<Instance> data = new ArrayList();
    public Mapper map = new Mapper();

    public class Mapper {

        public ArrayList<String> classifications = new ArrayList();

        public String getClassification(int classification) {
            return classifications.get(classification);
        }
    }
}
