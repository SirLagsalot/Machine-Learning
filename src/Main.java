
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {

        DataSet dataSet;
        Tester tester;

        dataSet = Reader.readFile("./data/glass.data.txt");
        tester = new Tester(dataSet, "glass");

        dataSet = Reader.readFile("./data/breast-cancer-wisconsin.data.txt");
        tester = new Tester(dataSet, "breast-cancer-wisconsin");

        dataSet = Reader.readFile("./data/house-votes-84.data.txt");
        tester = new Tester(dataSet, "house-votes-84");

        dataSet = Reader.readFile("./data/iris.data.txt");
        tester = new Tester(dataSet, "iris");

        dataSet = Reader.readFile("./data/soybean-small.data.txt");
        tester = new Tester(dataSet, "soybean-small");
//        ArrayList<Integer> test = new ArrayList();
//        test.add(2);
//        test.add(0);
//        test.add(0);
//        test.add(0);
//        test.add(0);
//        ID3 id3 = new ID3(new ArrayList());
//        System.out.println(id3.classify(test));
    }
}
