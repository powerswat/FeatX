import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;

public class FileProcessor {
    private static final int NUM_PIXELS = 256;
    private static int NUM_DATA;

    private static String[] columns;
    private static BigInteger[] X;
    private static int[] y;
    private static ArrayList<String> rows;

    public void readCSV(String filename, boolean isFirstTitle){
        rows = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String str = "";
            boolean isTitleDone = false;
            while ((str = br.readLine()) != null){
                str = str.trim();
                if (!isFirstTitle)
                    isTitleDone = true;
                else if (!isTitleDone && isFirstTitle) {
                    columns = str.split(",");;
                    isTitleDone = true;
                    continue;
                }

                if (str.equals(""))
                    continue;

                rows.add(str);
            }
            NUM_DATA = rows.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void splitXAndY(){
        X = new BigInteger[NUM_DATA];
        y = new int[NUM_DATA];
        for (int i = 0; i < rows.size(); i++) {
            String row = rows.get(i).replaceAll(",", "");
            y[i] = row.charAt(0) - '0';
            X[i] = new BigInteger(row.substring(1, NUM_PIXELS+1), 2);
        }
    }

    public void writeCSV(ArrayList<String[]> data, String filename, boolean isColumnInFile) {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(filename));

            if (isColumnInFile) {
                for (int i = 0; i < columns.length - 1; i++)
                    br.write(columns[i] + ",");
                br.write(columns[columns.length-1] + "\n");
            }

            for (int i = 0; i < data.size(); i++) {
                String[] row = data.get(i);
                for (int j = 0; j < row.length-1; j++) {
                    br.write(row[j] + ",");
                }
                if (i == data.size()-1)
                    br.write(row[row.length-1]);
                else
                    br.write(row[row.length-1] + "\n");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] getColumns() {
        return columns;
    }

    public static BigInteger[] getX() {
        return X;
    }

    public static int[] getY() {
        return y;
    }

    public static ArrayList<String> getRows() {
        return rows;
    }
}
