
import java.util.ArrayList;

public class DataSet {

    public ArrayList<Instance> data;
    public Mapper map;

    public class Mapper {

        public String[] classifications;

        public String getClassification(int classification) {
            return classifications[classification];
        }
    }

}
