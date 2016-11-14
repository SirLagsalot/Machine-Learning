
public class Main {

    public static void main(String[] args) {

        DataSet data = Reader.readFile("./data/glass.data.txt");
        Tester tester = new Tester(data, "glass");

        data = Reader.readFile("./data/breast-cancer-wisconsin.data.txt");
        tester = new Tester(data, "breast-cancer-wisconsin");

        data = Reader.readFile("./data/house-votes-84.data.txt");
        tester = new Tester(data, "house-votes-84");

        data = Reader.readFile("./data/iris.data.txt");
        tester = new Tester(data, "iris");

        data = Reader.readFile("./data/soybean-small.data.txt");
        tester = new Tester(data, "soybean-small");
    }
}
