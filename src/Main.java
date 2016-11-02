import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> data = Reader.readFile("./data/breast-cancer-wisconsin.data.txt");
        for (String s : data) {
            System.out.println(s);
        }
        data = Sanitizer.sanitize(data);
    }
}
