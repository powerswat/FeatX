import java.io.File;
import java.util.ArrayList;

public class TestingModule {
    static FileProcessor fp = new FileProcessor();

    private static void test1_NumOfBits(){
        ArrayList<String> rows = fp.getRows();
        int cnt = 0;
        for (int i = 0; i < 5000; i++) {
            String[] elems = rows.get(i).split(",");
            if (elems[1].equals("0")) {
//                System.out.println("Row:" + i);
                cnt++;
            }
        }
        System.out.println(cnt);
    }

    public static void main(String[] args){
        File basePath = new File(".");
        String baseDir = basePath.getAbsolutePath();
        baseDir = baseDir.replaceAll("\\.", "");

        fp.readCSV(baseDir + "data/OCRpixels.csv", false);

        test1_NumOfBits();
    }
}
