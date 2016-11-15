
public class Main {

    public static void main(String[] args) {

        DataSet dataSet;

        //  dataSet = Reader.readFile("./data/glass.data.txt");
        //  Tester tester = new Tester(dataSet, "glass");
//        dataSet = Reader.readFile("./data/breast-cancer-wisconsin.data.txt");
//        tester = new Tester(dataSet, "breast-cancer-wisconsin");
//
//        dataSet = Reader.readFile("./data/house-votes-84.data.txt");
//        tester = new Tester(dataSet, "house-votes-84");
//
//        dataSet = Reader.readFile("./data/iris.data.txt");
//        tester = new Tester(dataSet, "iris");
//
        dataSet = Reader.readFile("./data/soybean-small.data.txt");
        Tester tester = new Tester(dataSet, "soybean-small");
    }
}
