import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

public class FeatXDriver {
    private static FileProcessor fp = new FileProcessor();
    private static Prob1 p1 = new Prob1();
    private static Prob2 p2 = new Prob2();

    public static void main(String[] args){
        File basePath = new File(".");
        String baseDir = basePath.getAbsolutePath();
        baseDir = baseDir.replaceAll("\\.", "");

        fp.readCSV(baseDir + "data/OCRpixels.csv", false);
        fp.splitXAndY();

        // Solve part 1
        p1.solve(fp.getX(), fp.getY(), 2);

        // Solve part 2
        p2.solve(p1.getValidFilters(), p1.getFilterIdxMap(), fp.getX(), fp.getY());
        

//        fp.writeCSV(fp.getDataTable(), baseDir + "data/titanic_next.csv", true);
    }
}
