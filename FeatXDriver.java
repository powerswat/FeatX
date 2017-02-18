import java.util.ArrayList;

/**
 * Created by youngsukcho on 2017. 2. 17..
 */
public class FeatXDriver {
    static FileProcessor fp = new FileProcessor();
    static DataPreProcessor dpp = new DataPreProcessor();

    public static void main(String[] args){
        ArrayList<ArrayList<String>> readData = fp.readCSV("./../data/titanic.csv");

    }
}
