import java.io.File;
import java.util.ArrayList;

/**
 * Created by youngsukcho on 2017. 2. 17..
 */
public class FeatXDriver {
    static FileProcessor fp = new FileProcessor();

    public static void main(String[] args){
        File basePath = new File(".");
        String baseDir = basePath.getAbsolutePath();
        baseDir = baseDir.replaceAll("\\.", "");

        fp.readCSV(baseDir + "data/titanic.csv", true);
        fp.fitDataType();
        fp.writeCSV(fp.getDataTable(), baseDir + "data/titanic_next.csv", true);
    }
}
