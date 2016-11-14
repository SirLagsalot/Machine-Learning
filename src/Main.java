
public class Main {

    public static void main(String[] args) {

        DataSet data = Reader.readFile("./data/glass.data.txt");
        Tester tester = new Tester(data, data.map.classifications.get(0));

        data = Reader.readFile("./data/breast-cancer-wisconsin.data.txt");
        tester = new Tester(data, data.map.classifications.get(0));

        data = Reader.readFile("./data/house-votes-84.data.txt");
        tester = new Tester(data, data.map.classifications.get(0));

        data = Reader.readFile("./data/iris.data.txt");
        tester = new Tester(data, data.map.classifications.get(0));

        data = Reader.readFile("./data/soybean-small.data.txt");
        tester = new Tester(data, data.map.classifications.get(0));
    }
}
