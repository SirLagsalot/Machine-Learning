
public class Main {

    public static void main(String[] args) {

        DataSet dataSet = Reader.readFile("./data/glass.data.txt");
        Tester tester = new Tester(dataSet, "glass");
        tester.normalize(dataSet.data);
//        dataSet = Reader.readFile("./data/breast-cancer-wisconsin.data.txt");
//        tester = new Tester(data, "breast-cancer-wisconsin");
//
//        dataSet = Reader.readFile("./data/house-votes-84.data.txt");
//        tester = new Tester(data, "house-votes-84");
//
//        dataSet = Reader.readFile("./data/iris.data.txt");
//        tester = new Tester(data, "iris");
//
//        dataSet = Reader.readFile("./data/soybean-small.data.txt");
//        tester = new Tester(data, "soybean-small");
    }
}
