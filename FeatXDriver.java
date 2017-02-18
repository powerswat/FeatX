import java.io.File;
import java.util.ArrayList;

/**
 * Created by youngsukcho on 2017. 2. 17..
 */
public class FeatXDriver {
    static FileProcessor fp = new FileProcessor();
    static DataPreProcessor dpp = new DataPreProcessor();

    public static void main(String[] args){
        File basePath = new File(".");
        String baseDir = basePath.getAbsolutePath();
        baseDir = baseDir.replaceAll("\\.", "");

        ArrayList<ArrayList<String>> readData
                = fp.readCSV(baseDir + "data/titanic.csv");

    }
}
