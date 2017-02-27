import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

public class FeatXDriver {
    private static FileProcessor fp = new FileProcessor();
    private static FilterFileProcessor ffp = new FilterFileProcessor();
    private static Prob1 p1 = new Prob1();
    private static Prob2 p2 = new Prob2();
    private final static int PNUM = 1;
    private final static int LIMIT = 0;

    public static void main(String[] args){
        File basePath = new File(".");
        String baseDir = basePath.getAbsolutePath();
        baseDir = baseDir.replaceAll("\\.", "");

        fp.readCSV(baseDir + "data/OCRpixels.csv", false);
        fp.splitXAndY();

        // Solve part 1
        String filterFilename = "data/filter_" + Integer.toString(PNUM) + ".csv";
        String filterIdxFilename = "data/filterIdxMap_" + Integer.toString(PNUM) + ".csv";

        File filterFile = new File(filterFilename);
        File filterIdxFile = new File(filterIdxFilename);

        if (!filterFile.exists() || !filterIdxFile.exists()) {
            p1.solve(fp.getX(), fp.getY(), PNUM, LIMIT);

            ffp.writeHashMapCSV(p1.getFilterIdxMap(), filterIdxFilename,
                    "String", "Integer");
            ffp.writeArrayList2dCSV(p1.getValidFilters(), filterFilename, "FilterList");
        } else {
            p1.setFilterIdxMap(ffp.readHashMapCSV(filterIdxFilename));
            p1.setValidFilters(ffp.readArrayList2dCSV(filterFilename, "FilterList"));
        }

        // Solve part 2
        p2.solve(p1.getValidFilters(), p1.getFilterIdxMap(), fp.getX(), fp.getY(), PNUM);
        

//        fp.writeCSV(fp.getDataTable(), baseDir + "data/titanic_next.csv", true);
    }
}
