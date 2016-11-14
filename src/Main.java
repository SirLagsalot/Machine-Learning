

public class Main {

    public static void main(String[] args) {   
        DataSet data = Reader.readFile("./data/glass.data.txt");
        
        Tester tester = new Tester(data, data.map.classifications.get(0));
    }
}
