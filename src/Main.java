
public class Main {

    //Main method dictates order of data file read in and testing
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
    }
}
