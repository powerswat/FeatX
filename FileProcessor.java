import java.io.*;
import java.util.ArrayList;

/**
 * Created by youngsukcho on 2017. 2. 17..
 */
public class FileProcessor {
    private static String[] columns;
    private static ArrayList<String[]> dataTable;
    private static String[] types;

    public void readCSV(String filename, boolean isFirstTitle){
        ArrayList<String> rows = new ArrayList<String>();
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataTable = splitRow(rows);
    }

    private ArrayList<String[]> splitRow(ArrayList<String> rows){
        ArrayList<String[]> res = new ArrayList<String[]>();

        for (String row: rows)
            res.add(row.split(","));

        return res;
    }

    public void fitDataType(){
        findType();
    }

    public void findType(){
        String[] row = dataTable.get(0);
        types = new String[row.length];
        for (int i = 0; i < row.length; i++) {
            if (row[i].replaceAll("[-+0-9]", "").equals(""))
                types[i] = "int";
            else if (row[i].replaceAll("[-+0-9]", "").equals("."))
                types[i] = "double";
            else if (row[i].replaceAll("[A-Za-z]", "").length() != row[i].length())
                types[i] = "String";
            else
                System.out.println("Error");
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

    public static ArrayList<String[]> getDataTable() {
        return dataTable;
    }
}
